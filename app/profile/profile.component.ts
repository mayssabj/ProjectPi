import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { LoginService, User } from '../login.service';
import { Router } from '@angular/router';
import { catchError, finalize } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: User = {
    id: 0,
    nom: '',
    email: '',
    age: '',
    role: '',
    avatarUrl: ''
  };

  editMode = false;
  profileForm!: FormGroup;
  isLoading = true;
  errorMessage = '';

  // Updated stats with more specific typing
  stats = {
    mealsServed: 0,
    averageRating: 0,
    menuVariety: 0
  };

  // Role options for dropdown
  roleOptions = [
    { value: 'User', label: 'User' },
    { value: 'Medcin', label: 'Medcin' },
    { value: 'Staff', label: 'Staff' },
    { value: 'Admin', label: 'Admin' }
  ];

  constructor(
    private loginService: LoginService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.initializeForm();
  }

  // Initialize form with validators
  private initializeForm(): void {
    this.profileForm = this.fb.group({
      nom: ['', [
        Validators.required, 
        Validators.minLength(2), 
        Validators.maxLength(50)
      ]],
      email: ['', [
        Validators.required, 
        Validators.email
      ]],
      age: ['', [
        Validators.required, 
        Validators.min(18), 
        Validators.max(120)
      ]],
      role: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const userId = this.loginService.getUserIdFromToken();
    
    if (!userId) {
      this.handleError('Unable to retrieve user ID');
      return;
    }

    this.fetchUserProfile(userId);
  }

  // Centralized method to fetch user profile
  private fetchUserProfile(userId: number): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.loginService.getUserById(userId)
      .pipe(
        catchError(error => {
          this.handleError('Failed to load user details', error);
          return of(null);
        }),
        finalize(() => this.isLoading = false)
      )
      .subscribe(userData => {
        if (userData) {
          this.user = userData;
          this.updateForm();
        }
      });
  }

  // Safely get form control with improved error handling
  getFormControl(name: string): FormControl {
    const control = this.profileForm.get(name);
    if (!control) {
      console.error(`Form control not found: ${name}`);
      throw new Error(`Control not found: ${name}`);
    }
    return control as FormControl;
  }

  // Update form with user data
  private updateForm(): void {
    this.profileForm.patchValue({
      nom: this.user.nom,
      email: this.user.email,
      age: this.user.age,
      role: this.user.role
    });
  }

  // Toggle edit mode
  toggleEditMode(): void {
    this.editMode = !this.editMode;
    if (this.editMode) {
      this.updateForm();
    }
  }

  // Save profile changes
  saveChanges(): void {
    const userId = this.loginService.getUserIdFromToken();
    if (userId === null) {
      this.handleError('User ID is missing. Please log in again.');
      return;
    }
  
    if (this.profileForm.invalid) {
      this.markFormGroupTouched(this.profileForm);
      this.errorMessage = 'Please correct the errors in the form.';
      return;
    }
  
    const updatedUser: User = {
      ...this.user,
      ...this.profileForm.value,
      id: userId
    };
  
    this.isLoading = true;
    this.errorMessage = '';
  
    this.loginService.updateUserProfile(userId.toString(), updatedUser)
      .pipe(
        catchError(error => {
          console.error('Update error:', error);
          
          // Reload user data from database
          this.fetchUserProfile(userId);
          
          return of(null);
        }),
        finalize(() => this.isLoading = false)
      )
      .subscribe(response => {
        if (response) {
          this.user = updatedUser;
          this.editMode = false;
        }
      });
  }

  // Mark all controls as touched to show validation errors
  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  // Cancel edit mode
  cancelEdit(): void {
    this.editMode = false;
    this.updateForm();
    this.errorMessage = '';
  }

  // Logout functionality
  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

  // Handle avatar upload
  uploadAvatar(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (file) {
      const reader = new FileReader();
      reader.onload = (e: ProgressEvent<FileReader>) => {
        this.user.avatarUrl = e.target?.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  // Centralized error handling method
  private handleError(message: string, error?: any): void {
    console.error(message, error);
    this.errorMessage = message;
    this.isLoading = false;
  }
}
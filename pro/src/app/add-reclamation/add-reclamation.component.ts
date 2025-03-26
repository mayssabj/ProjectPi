import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ReclamationService } from '../reclamation.service';
import { LoginService } from '../login.service'; // Import LoginService

@Component({
  selector: 'app-add-reclamation',
  templateUrl: './add-reclamation.component.html',
  styleUrls: ['./add-reclamation.component.scss']
})
export class AddReclamationComponent implements OnInit {
  reclamationForm: FormGroup;
  currentUserId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private reclamationService: ReclamationService,
    private loginService: LoginService, // Inject LoginService
    private router: Router
  ) {
    // Get the current user ID from the token
    this.currentUserId = this.loginService.getUserIdFromToken();

    // Initialize the form with validators
    this.reclamationForm = this.fb.group({
      subject: ['', [
        Validators.required, 
        Validators.minLength(5), 
        Validators.maxLength(100)
      ]],
      description: ['', [
        Validators.required, 
        Validators.minLength(10), 
        Validators.maxLength(500)
      ]],
      status: ['PENDING'], // Add status
      user: { id_user: this.currentUserId } // Use dynamic user ID
    });
  }

  ngOnInit(): void {
    // Check if user is logged in
    if (!this.currentUserId) {
      // Redirect to login if no user ID is found
      this.router.navigate(['/login']);
    }
  }

  onSubmit(): void {
    // Check if the form is valid
    if (this.reclamationForm.valid) {
      // Prepare the reclamation data
      const reclamationData = this.reclamationForm.value;

      // Call the service to create a new reclamation
      this.reclamationService.createReclamation(reclamationData).subscribe(
        (response) => {
          // Handle successful creation
          console.log('Reclamation created successfully', response);
          
          // Optional: Show success message
          alert('Complaint submitted successfully!');
          
          // Navigate back to reclamation list or dashboard
          this.router.navigate(['/reclamations']);
        },
        (error) => {
          // Handle error
          console.error('Detailed error creating reclamation', error);
          
          // Optional: Show error message
          alert(`Failed to submit complaint: ${error.message || 'Unknown error'}`);
        }
      );
    } else {
      // Mark all fields as touched to show validation errors
      Object.keys(this.reclamationForm.controls).forEach(field => {
        const control = this.reclamationForm.get(field);
        control?.markAsTouched({ onlySelf: true });
      });
    }
  }

  // Getter methods to easily access form controls in the template
  get subject() { return this.reclamationForm.get('subject'); }
  get description() { return this.reclamationForm.get('description'); }

  // Method to cancel and go back
  onCancel(): void {
    this.router.navigate(['/reclamations']);
  }
}
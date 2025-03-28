import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-20px)' }),
        animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({ opacity: 0, transform: 'translateY(-20px)' }))
      ])
    ])
  ]
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup; // Remove the potential undefined type
  loading = false;
  isSubmitted = false;
  errorMessage = '';
  showPassword = false;

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private router: Router
  ) {
    // Initialize the form in the constructor instead of ngOnInit
    this.signupForm = this.formBuilder.group({
      nom: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      age: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(3)]],
      role: ['User'],
      rememberMe: [false]
    });
  }

  ngOnInit(): void {
    // We still initialize animations in ngOnInit
    this.initFoodAnimation();
  }

  get formControls() {
    return this.signupForm.controls;
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    this.isSubmitted = true;
    
    if (this.signupForm.invalid) {
      return;
    }
    
    this.loading = true;
    
    const credentials = {
      nom: this.formControls['nom'].value,
      email: this.formControls['email'].value,
      age: this.formControls['age'].value,
      mdp: this.formControls['password'].value,
      role: this.formControls['role'].value,
      link_image: this.imageLocalPath || ''
    };
    
    this.loginService.register(credentials).subscribe({
      next: (data) => {
        console.log(credentials.link_image);
        console.log(credentials.email);
        console.log(credentials.mdp);
        console.log(data);
        this.loading = false;
        
        // Determine where to navigate based on role
        const role = this.loginService.getRole();
        switch (role) {
          case 'ADMIN':
            this.router.navigate(['/admin/dashboard']);
            break;
          case 'USER':
            this.router.navigate(['/menu']);
            break;
          case 'STAFF':
            this.router.navigate(['/staff/orders']);
            break;
          default:
            this.router.navigate(['/home']);
        }
      },
      error: (error: { status: number; }) => {
        this.loading = false;
        if (error.status === 401) {
          this.errorMessage = 'Invalid email or password';
        } else {
          this.errorMessage = 'Connection error. Please try again.';
        }
        
        // Create shaking animation for form
        const loginCard = document.querySelector('.login-card');
        loginCard?.classList.add('shake');
        setTimeout(() => {
          loginCard?.classList.remove('shake');
        }, 500);
      }
    });
  }
  
  clearError(): void {
    this.errorMessage = '';
  }
  
  initFoodAnimation(): void {
    // Animation for food icons
    // Ensure gsap is available from the global window object
    const gsap = (window as any).gsap;
    if (!gsap) {
      console.warn('GSAP library not loaded');
      return;
    }

    // Animation for food icons
    const foodIcons = document.querySelectorAll('.food-icon');
    
    foodIcons.forEach((icon, index) => {
      // Set initial positions
      const delay = index * 2;
      
      gsap.set(icon, {
        x: Math.random() * 100 - 50,
        y: Math.random() * 50,
        opacity: 0,
        scale: 0.5
      });
      
      // Create floating animation
      gsap.to(icon, {
        duration: 3 + Math.random() * 2,
        y: '-=30',
        x: '+=15',
        rotation: Math.random() * 10 - 5,
        opacity: 0.8,
        scale: 0.8,
        delay: delay,
        repeat: -1,
        yoyo: true,
        ease: 'power1.inOut'
      });
    });
  }

  roleOptions = ['User', 'Staff', 'Admin', 'Medcin'];
imagePreview: string | null = null;

selectRole(role: string): void {
  this.signupForm.patchValue({ role: role });
}

onImageSelected(event: any): void {
  const file = event.target.files[0];
  if (file) {
    // Get the local file path
    const localFilePath = (event.target as HTMLInputElement).value;
    
    // Create a FileReader to generate preview
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result as string;
      this.imageLocalPath = localFilePath; // Store the local file path
      console.log('Image Preview:', this.imagePreview);
      console.log('Local File Path:', localFilePath);
    };
    reader.readAsDataURL(file);

    // Optional: Log the file details
    console.log('File Details:', {
      name: file.name,
      type: file.type,
      size: file.size,
      localPath: localFilePath
    });
  }
}

// Add these properties to your component class
imageLocalPath: string | null = null;



getPasswordStrengthClass(): string {
  const password = this.formControls['password'].value;
  if (!password) return '';
  if (password.length < 6) return 'weak';
  if (password.length < 10) return 'medium';
  return 'strong';
}

getPasswordStrengthText(): string {
  const password = this.formControls['password'].value;
  if (!password) return '';
  if (password.length < 6) return 'Weak Password';
  if (password.length < 10) return 'Medium Strength';
  return 'Strong Password';
}

getRoleIcon(role: string): string {
  switch(role) {
    case 'User': return 'fas fa-user';
    case 'Staff': return 'fas fa-users';
    case 'Admin': return 'fas fa-crown';
    case 'Medcin': return 'fas fa-briefcase-medical';
    default: return 'fas fa-user';
  }
}
}
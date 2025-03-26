import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
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
export class LoginComponent implements OnInit {
  loginForm: FormGroup; // Remove the potential undefined type
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
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]],
      rememberMe: [false]
    });
  }

  ngOnInit(): void {
      const userId = this.loginService.getUserIdFromToken();
      console.log('User ID:', userId);
    
      if (userId) {
        this.loginService.getUserById(userId).subscribe({
          next: (user) => {
            // Handle user data
            console.log('User details:', user);
          },
          error: (error) => {
            console.error('Error fetching user', error);
          }
        });
      } else {
        console.error('No user ID found');
      }
    
    // We still initialize animations in ngOnInit
    this.initFoodAnimation();
  }

  get formControls() {
    return this.loginForm.controls;
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  /*onSubmit(): void {
    this.isSubmitted = true;
    
    if (this.loginForm.invalid) {
      return;
    }
    
    this.loading = true;
    
    const credentials = {
      email: this.formControls['email'].value,
      mdp: this.formControls['password'].value
    };
    
    this.loginService.login(credentials).subscribe({
      next: (data) => {
        console.log(credentials.email);
        console.log(credentials.mdp);
        console.log(data);
        this.loading = false;
        
        // Determine where to navigate based on role
        const role = this.loginService.getRole();
        this.router.navigate(['/profile']);
        /*
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
  }*/

    onSubmit(): void {
      this.isSubmitted = true;
    
    if (this.loginForm.invalid) {
      return;
    }
    
    this.loading = true;
    
    const credentials = {
      email: this.formControls['email'].value,
      mdp: this.formControls['password'].value
    };
      this.loginService.login(credentials).subscribe({
        next: (data) => {
          // The user ID might not be immediately available
          const userId = this.loginService.getUserIdFromToken();
          
          if (userId) {
            // Ensure user data is fetchable before navigating
            this.loginService.getUserById(userId).subscribe({
              next: () => {
                this.router.navigate(['/profile']);
              },
              error: (error) => {
                console.error('Failed to fetch user', error);
                // Fallback navigation or error handling
              }
            });
          } else {
            console.error('No user ID found');
          }
        },
        error: (error) => {
          // Error handling
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
}
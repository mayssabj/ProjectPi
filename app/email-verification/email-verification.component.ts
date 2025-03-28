import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-email-verification',
  templateUrl: './email-verification.component.html',
  styleUrls: ['./email-verification.component.scss'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-20px)' }),
        animate('500ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ]
})
export class EmailVerificationComponent implements OnInit {
  isLoading = true;
  isVerified = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private routerService: Router, // Renamed to avoid conflict
    private signupService: LoginService
  ) { }

  // Add public methods for navigation
  navigateToLogin() {
    this.routerService.navigate(['/login']);
  }

  navigateToSignup() {
    this.routerService.navigate(['/signup']);
  }

  ngOnInit(): void {
    this.verifyEmail();
  }

  verifyEmail(): void {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      
      if (!token) {
        this.isVerified = false;
        this.errorMessage = 'Token de vérification manquant.';
        return;
      }

      this.signupService.verifyEmail(token).subscribe({
        next: () => {
          this.isVerified = true;
        },
        error: (error: { status: number; }) => {
          this.isVerified = true;
          if (error.status === 400) {
            this.errorMessage = 'Le lien de vérification est invalide ou a expiré.';
          } else {
            this.errorMessage = 'Une erreur est survenue lors de la vérification de votre email.';
          }
        }
      });
    });
  }
}
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss'],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms', style({ opacity: 1 }))
      ]),
      transition(':leave', [
        animate('300ms', style({ opacity: 0 }))
      ])
    ]),
    trigger('slideInOut', [
      transition(':enter', [
        style({ transform: 'translateY(20px)', opacity: 0 }),
        animate('400ms', style({ transform: 'translateY(0)', opacity: 1 }))
      ])
    ])
  ]
})
export class ResetPasswordComponent implements OnInit {
  resetPasswordForm: FormGroup;
  token: string = '';
  successMessage: string = '';
  errorMessage: string = '';
  loading = false;
  isSubmitted = false;
  formSubmitAttempted = false;
  resetSuccess = false;

  constructor(
    private fb: FormBuilder,
    private loginService: LoginService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.resetPasswordForm = this.fb.group({
      newPassword: ['', [
        Validators.required, 
        Validators.minLength(8)
      ]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
      if (!this.token) {
        this.router.navigate(['/login']);
      }
    });
  }

  get formControls() {
    return this.resetPasswordForm.controls;
  }

  passwordMatchValidator(form: FormGroup) {
    const newPassword = form.get('newPassword');
    const confirmPassword = form.get('confirmPassword');

    return newPassword && confirmPassword && newPassword.value === confirmPassword.value 
      ? null 
      : { passwordMismatch: true };
  }

  onSubmit() {
    this.isSubmitted = true;
    this.formSubmitAttempted = true;

    if (this.resetPasswordForm.invalid) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const newPassword = this.resetPasswordForm.get('newPassword')?.value;

    this.loginService.resetPassword(this.token, newPassword)
      .subscribe({
        next: (response: any) => {
          this.loading = false;
          this.successMessage = 'Password reset successfully';
          this.resetSuccess = true;
          
          // Navigate to login after a short delay
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        },
        error: (error: { message: string; }) => {
          this.loading = false;
          this.errorMessage = error.message || 'Password reset failed';
          this.resetSuccess = false;
        }
      });
  }

  clearSuccess() {
    this.successMessage = '';
  }

  clearError() {
    this.errorMessage = '';
  }
}
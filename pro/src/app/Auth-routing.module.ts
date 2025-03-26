import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { EmailVerificationComponent } from './email-verification/email-verification.component';
import { SignupComponent } from './signup/signup.component';
import { ForgetPasswordComponent } from './forget-password/forgot-password.component';

const authRoutes: Routes = [
  { path: 'login', component: LoginComponent },   
  { path: 'signup', component: SignupComponent },   
  { path: 'verify-email', component: EmailVerificationComponent },   
  { path: 'forgot-password', component: ForgetPasswordComponent }
];

@NgModule({
  imports: [RouterModule.forChild(authRoutes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {
  currentYear: number = new Date().getFullYear();
  newsletterEmail: string = '';

  constructor(private http: HttpClient) {}

  subscribeNewsletter() {
    if (!this.newsletterEmail) return;

    this.http.post('api/newsletter/subscribe', { email: this.newsletterEmail })
      .pipe(
        catchError(error => {
          console.error('Newsletter subscription failed', error);
          // You could add a toast or snackbar notification here
          return throwError(() => new Error('Subscription failed'));
        })
      )
      .subscribe({
        next: () => {
          alert('Thank you for subscribing!');
          this.newsletterEmail = '';
        }
      });
  }
}
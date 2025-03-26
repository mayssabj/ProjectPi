import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(public router: Router) {}

  // Method to check if header should be hidden
  shouldHideHeader(): boolean {
    const hiddenRoutes = ['/login', '/signup', '/forgot-password', '/verify-email'];
    return hiddenRoutes.includes(this.router.url);
  }
}

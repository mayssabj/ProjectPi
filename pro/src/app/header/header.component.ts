import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService,User } from '../login.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  userName: string = 'User';
  userProfileImage: string = 'assets/default-avatar.png';
  isProfileDropdownOpen: boolean = false;
  userRole: string | null = null;

  constructor(
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    const userId = this.loginService.getUserIdFromToken();
    
    if (userId) {
      this.loginService.getUserById(userId).subscribe({
        next: (user: User) => {
          this.userName = user.nom;
          this.userProfileImage = user.avatarUrl || this.userProfileImage;
          this.userRole = this.loginService.getRole();
        },
        error: (error) => {
          console.error('Error fetching user profile', error);
        }
      });
    }
  }

  toggleProfileDropdown(): void {
    this.isProfileDropdownOpen = !this.isProfileDropdownOpen;
  }

  navigateToProfile(): void {
    this.router.navigate(['/profile']);
    this.isProfileDropdownOpen = false;
  }

  navigateToAddRec(): void {
    this.router.navigate(['/add-reclamation']);
    this.isProfileDropdownOpen = false;
  }

  navigateToSettings(): void {
    this.router.navigate(['/user-reclamations']);
    this.isProfileDropdownOpen = false;
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: any): void {
    if (!event.target.closest('.user-profile')) {
      this.isProfileDropdownOpen = false;
    }
  }
}
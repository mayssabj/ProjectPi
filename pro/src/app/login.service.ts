import { Injectable } from '@angular/core';
import { catchError, Observable, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export interface User {
  id: number;
  nom: string;
  email: string;
  age: string;
  role: string;
  avatarUrl: string;  
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  constructor(private http: HttpClient) { }

  private baseUrl = 'http://localhost:8081/api/auth';

  login(user: { email: string; mdp: string }): Observable<any> {
    return this.http.post<{ token: string }>(`${this.baseUrl}/login`, user).pipe(
      tap(response => {
        if (response.token) {
          localStorage.setItem('token', response.token);
          console.log('Token', response.token);
          console.log('Role', this.getRole());
        }
      })
    );
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/user/${id}`);
  }

  getRole(): string | null {
    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
      const decodedToken = this.jwtDecode(token);
      return decodedToken?.role || null;
    } catch (error) {
      console.error('Error decoding token', error);
      return null;
    }
  }

  // Improved JWT decoding method
  private jwtDecode(token: string): any {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace('-', '+').replace('_', '/');
      return JSON.parse(window.atob(base64));
    } catch (error) {
      console.error('Error decoding token', error);
      return null;
    }
  }

  getUserIdFromToken(): number | null {
    const token = localStorage.getItem('token');
    if (!token) return null;
  
    try {
      const decodedToken = this.jwtDecode(token);
      // Directly parse the 'id' claim as a number
      return decodedToken?.id ? parseInt(decodedToken.id, 10) : null;
    } catch (error) {
      console.error('Error extracting user ID', error);
      return null;
    }
  }
  
  updateUserProfile(userId: string, profile: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/userUpdate/${userId}`, profile).pipe(
      catchError((error) => {
        console.error('Error updating user profile', error);
        throw error;
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  register(user: {
    nom: string;
    email: string;
    mdp: string;
    age: string;
    role: string;
  }): Observable<any> {
    console.log(user);
    return this.http.post(`${this.baseUrl}/signup`, user);
  }

  verifyEmail(token: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/verify-email?token=${token}`);
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/forgot-password`, { email });
  }
  
  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/reset-password`, { 
      token, 
      newPassword 
    });
  }
}
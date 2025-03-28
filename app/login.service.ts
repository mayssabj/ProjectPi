import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';


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

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/users`);
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
    const token = localStorage.getItem('token');
    
    if (!token) {
      console.error('No authentication token found');
      // Optionally throw an error or return an observable that errors out
      return throwError(() => new Error('No authentication token found'));
    }
  
    return this.http.put<User>(`${this.baseUrl}/userUpdate/${userId}`, profile, {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      })
    }).pipe(
      catchError(error => {
        console.error('Profile update error:', error);
        // Re-throw the error to be caught by the component
        return throwError(() => error);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  registerWithImage(formData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/signup`, formData, {
      headers: new HttpHeaders({
        'Accept': 'application/json'
      })
    });
  }

  register(user: {
    nom: string;
    email: string;
    mdp: string;
    age: string;
    role: string;
    link_image: string;
  }): Observable<any> {
    console.log(user);
    return this.http.post(`${this.baseUrl}/signup`, user);
  }

  verifyEmail(token: string): Observable<any> {
    const params = new HttpParams().set('token', token);
    return this.http.get(`${this.baseUrl}/verify-email`, { params });
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/forgot-password`, { email });
  }
  
  resetPassword(token: string, newPassword: string) {
    const params = new HttpParams()
      .set('token', token)
      .set('newPassword', newPassword);
  
    return this.http.post(
      `${this.baseUrl}/reset-password`,
      null,
      {
        params: params,
        headers: new HttpHeaders({
          'Content-Type': 'application/x-www-form-urlencoded'
        }),
        responseType: 'text' // Change responseType to 'text'
      }
    );
  }
}

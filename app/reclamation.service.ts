import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

// Define an interface for the Reclamation
export interface Reclamation {
  id?: number;
  subject: string;
  description: string;
  dateCreated?: string | Date;
  status: 'PENDING' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED';
  user: {
    id_user: number;
  };
}

@Injectable({
  providedIn: 'root'
})
export class ReclamationService {
  private apiUrl = 'http://localhost:8081/api/reclamations';

  constructor(private http: HttpClient) {}

  // Create headers with content type
  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json'
    });
  }

  // Error handling method
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    
    console.error(errorMessage);
    return throwError(errorMessage);
  }

  // Create a new reclamation
  createReclamation(reclamation: Reclamation): Observable<Reclamation> {
    console.log('Sending reclamation:', reclamation);

    return this.http.post<Reclamation>(this.apiUrl, reclamation, { 
      headers: this.getHeaders() 
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get all reclamations
  getAllReclamations(): Observable<Reclamation[]> {
    return this.http.get<Reclamation[]>(this.apiUrl).pipe(
      catchError(this.handleError)
    );
  }

  // Get reclamations by user ID
  getReclamationsByUserId(userId: number): Observable<Reclamation[]> {
    return this.http.get<Reclamation[]>(`${this.apiUrl}/user/${userId}`).pipe(
      catchError(this.handleError)
    );
  }

  // Get a single reclamation by ID
  getReclamationById(id: number): Observable<Reclamation> {
    return this.http.get<Reclamation>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Update a reclamation
  updateReclamation(id: number, reclamation: Reclamation): Observable<Reclamation> {
    return this.http.put<Reclamation>(`${this.apiUrl}/${id}`, reclamation, {
      headers: this.getHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Update reclamation status
  updateReclamationStatus(id: number, status: 'PENDING' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED'): Observable<Reclamation> {
    return this.http.patch<Reclamation>(`${this.apiUrl}/${id}/status`, { status }, {
      headers: this.getHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Delete a reclamation
  deleteReclamation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Optional: Method to filter reclamations
  filterReclamations(
    status?: 'PENDING' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED', 
    sortBy?: 'dateCreated' | 'status'
  ): Observable<Reclamation[]> {
    let url = this.apiUrl;
    
    // Add query parameters if needed
    const params: string[] = [];
    if (status) params.push(`status=${status}`);
    if (sortBy) params.push(`sortBy=${sortBy}`);

    if (params.length > 0) {
      url += `?${params.join('&')}`;
    }

    return this.http.get<Reclamation[]>(url).pipe(
      catchError(this.handleError)
    );
  }
}
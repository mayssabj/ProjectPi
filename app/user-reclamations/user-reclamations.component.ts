import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ReclamationService, Reclamation } from '../reclamation.service';
import { LoginService } from '../login.service';
import { Router } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';

@Component({
  selector: 'app-user-reclamations',
  templateUrl: './user-reclamations.component.html',
  styleUrls: ['./user-reclamations.component.scss'],
  animations: [
    trigger('reclamationAnimation', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(20px)' }),
        animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      transition(':leave', [
        animate('200ms ease-in', style({ opacity: 0, transform: 'scale(0.95)' }))
      ])
    ])
  ]
})
export class UserReclamationsComponent implements OnInit {
  @ViewChild('reclamationContainer') reclamationContainer!: ElementRef;

  reclamations: Reclamation[] = [];
  loading: boolean = true;
  filteredReclamations: Reclamation[] = [];
  statusFilter: string = 'ALL';

  // Status translation map
  private statusLabels: { [key: string]: string } = {
    'PENDING': 'En attente',
    'IN_PROGRESS': 'En cours',
    'RESOLVED': 'Résolue',
    'CLOSED': 'Fermée'
  };

  constructor(
    private reclamationService: ReclamationService,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const userId = this.loginService.getUserIdFromToken();
    
    if (!userId) {
      this.router.navigate(['/login']);
      return;
    }

    this.fetchReclamations(userId);
  }

  // Method to get translated status label
  getStatusLabel(status: string): string {
    return this.statusLabels[status] || status;
  }

  fetchReclamations(userId: any): void {
    this.loading = true;
    this.reclamationService.getReclamationsByUserId(userId).subscribe(
      (data) => {
        this.reclamations = data.sort((a, b) => 
          new Date(b.dateCreated ?? '').getTime() - new Date(a.dateCreated ?? '').getTime()
        );
        this.filteredReclamations = [...this.reclamations];
        this.loading = false;
      },
      (error) => {
        console.error('Error fetching reclamations', error);
        this.loading = false;
      }
    );
  }

  filterReclamations(status: string): void {
    this.statusFilter = status;
    this.filteredReclamations = status === 'ALL' 
      ? this.reclamations 
      : this.reclamations.filter(r => r.status === status);
  }

  goToAddReclamation(): void {
    this.router.navigate(['/add-reclamation']);
  }

  viewReclamationDetails(reclamation: Reclamation): void {
    this.router.navigate(['/reclamation-details', reclamation.id]);
  }

  deleteReclamation(id?: number): void {
    if (!id) return;

    const dialogConfirm = confirm('Êtes-vous sûr de vouloir supprimer cette réclamation ?');
    if (dialogConfirm) {
      this.reclamationService.deleteReclamation(id).subscribe(
        () => {
          this.reclamations = this.reclamations.filter(r => r.id !== id);
          this.filteredReclamations = this.filteredReclamations.filter(r => r.id !== id);
          this.showNotification('Réclamation supprimée avec succès');
        },
        (error) => {
          console.error('Erreur lors de la suppression', error);
          this.showNotification('Échec de la suppression', 'error');
        }
      );
    }
  }

  private showNotification(message: string, type: 'success' | 'error' = 'success'): void {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    document.body.appendChild(notification);
    
    setTimeout(() => {
      notification.classList.add('show');
      setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
          document.body.removeChild(notification);
        }, 300);
      }, 3000);
    }, 10);
  }
}
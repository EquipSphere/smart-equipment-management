import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { User, UserUpdate } from '../../models/user.model';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit {
  users: User[] = [];
  selectedRole: string = 'ALL';
  searchKeyword: string = '';
  loading: boolean = true;

  // Edit Modal State
  isModalOpen: boolean = false;
  currentUserId?: number;
  formModel: UserUpdate = {
    name: '',
    phone: '',
    department: '',
    role: 'USER'
  };

  alertMessage: string = '';
  alertType: 'success' | 'error' = 'success';

  constructor(private readonly userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.userService.getAll(this.selectedRole, this.searchKeyword.trim()).subscribe({
      next: (data) => {
        this.users = data;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        console.error('Error fetching users', err);
      }
    });
  }

  openEditModal(user: User): void {
    if (!user.id) return;
    this.currentUserId = user.id;
    this.formModel = {
      name: user.name,
      phone: user.phone || '',
      department: user.department || '',
      role: user.role
    };
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  saveUser(): void {
    if (!this.currentUserId) return;
    this.userService.update(this.currentUserId, this.formModel).subscribe({
      next: () => {
        this.showAlert('User profile updated successfully!', 'success');
        this.closeModal();
        this.loadUsers();
      },
      error: () => this.showAlert('Failed to update user.', 'error')
    });
  }

  deleteUser(user: User): void {
    if (!user.id) return;
    if (confirm(`Are you sure you want to delete user '${user.name}' (${user.email})?`)) {
      this.userService.delete(user.id).subscribe({
        next: () => {
          this.showAlert('User removed successfully!', 'success');
          this.loadUsers();
        },
        error: () => this.showAlert('Failed to delete user.', 'error')
      });
    }
  }

  showAlert(message: string, type: 'success' | 'error'): void {
    this.alertMessage = message;
    this.alertType = type;
    setTimeout(() => this.alertMessage = '', 4000);
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { User, UserCreate, UserUpdate } from '../../models/user.model';

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

  // Modal State
  isModalOpen: boolean = false;
  isEditMode: boolean = false;
  currentUserId?: number;

  formModel: {
    name: string;
    email: string;
    password?: string;
    phone: string;
    department: string;
    role: string;
  } = {
    name: '',
    email: '',
    password: '',
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

  openAddModal(): void {
    this.isEditMode = false;
    this.currentUserId = undefined;
    this.formModel = {
      name: '',
      email: '',
      password: 'password123',
      phone: '',
      department: '',
      role: 'USER'
    };
    this.isModalOpen = true;
  }

  openEditModal(user: User): void {
    if (!user.id) return;
    this.isEditMode = true;
    this.currentUserId = user.id;
    this.formModel = {
      name: user.name,
      email: user.email,
      password: '',
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
    if (!this.formModel.name || (!this.isEditMode && (!this.formModel.email || !this.formModel.password))) {
      this.showAlert('Please fill in all required fields.', 'error');
      return;
    }

    if (this.isEditMode && this.currentUserId) {
      const updateData: UserUpdate = {
        name: this.formModel.name,
        phone: this.formModel.phone,
        department: this.formModel.department,
        role: this.formModel.role
      };

      this.userService.update(this.currentUserId, updateData).subscribe({
        next: () => {
          this.showAlert('User updated successfully!', 'success');
          this.closeModal();
          this.loadUsers();
        },
        error: (err) => this.showAlert(err.error?.message || 'Failed to update user.', 'error')
      });
    } else {
      const createData: UserCreate = {
        name: this.formModel.name,
        email: this.formModel.email,
        password: this.formModel.password,
        phone: this.formModel.phone,
        department: this.formModel.department,
        role: this.formModel.role
      };

      this.userService.create(createData).subscribe({
        next: () => {
          this.showAlert('New user created successfully!', 'success');
          this.closeModal();
          this.loadUsers();
        },
        error: (err) => this.showAlert(err.error?.message || 'Failed to create user.', 'error')
      });
    }
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

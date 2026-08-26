import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EquipmentService } from '../../services/equipment.service';
import { Equipment, EquipmentRequest } from '../../models/equipment.model';

@Component({
  selector: 'app-equipment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './equipment.component.html',
  styleUrl: './equipment.component.css'
})
export class EquipmentComponent implements OnInit {
  equipmentList: Equipment[] = [];
  categories: string[] = [];
  
  selectedCategory: string = 'ALL';
  selectedStatus: string = 'ALL';
  searchKeyword: string = '';
  loading: boolean = true;
  viewMode: 'grid' | 'table' = 'grid';

  // Modal State
  isModalOpen: boolean = false;
  isEditMode: boolean = false;
  currentEquipmentId?: number;

  formModel: EquipmentRequest = {
    name: '',
    category: '',
    serialNumber: '',
    location: '',
    status: 'AVAILABLE',
    description: '',
    imageUrl: ''
  };

  alertMessage: string = '';
  alertType: 'success' | 'error' = 'success';

  constructor(private readonly equipmentService: EquipmentService) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadEquipment();
  }

  loadCategories(): void {
    this.equipmentService.getCategories().subscribe({
      next: (res) => this.categories = res,
      error: (err) => console.error('Error fetching categories', err)
    });
  }

  loadEquipment(): void {
    this.loading = true;
    if (this.searchKeyword.trim()) {
      this.equipmentService.search(this.searchKeyword.trim()).subscribe({
        next: (data) => {
          this.equipmentList = data;
          this.loading = false;
        },
        error: () => this.loading = false
      });
    } else {
      this.equipmentService.getAll(this.selectedCategory, this.selectedStatus).subscribe({
        next: (data) => {
          this.equipmentList = data;
          this.loading = false;
        },
        error: () => this.loading = false
      });
    }
  }

  onSearch(): void {
    this.loadEquipment();
  }

  onFilterChange(): void {
    this.searchKeyword = '';
    this.loadEquipment();
  }

  openAddModal(): void {
    this.isEditMode = false;
    this.currentEquipmentId = undefined;
    this.formModel = {
      name: '',
      category: this.categories[0] || 'Projector',
      serialNumber: '',
      location: '',
      status: 'AVAILABLE',
      description: '',
      imageUrl: ''
    };
    this.isModalOpen = true;
  }

  openEditModal(item: Equipment): void {
    this.isEditMode = true;
    this.currentEquipmentId = item.id;
    this.formModel = {
      name: item.name,
      category: item.category,
      serialNumber: item.serialNumber,
      location: item.location,
      status: item.status,
      description: item.description || '',
      imageUrl: item.imageUrl || ''
    };
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  saveEquipment(): void {
    if (!this.formModel.name || !this.formModel.serialNumber || !this.formModel.location) {
      this.showAlert('Please fill in all required fields.', 'error');
      return;
    }

    if (this.isEditMode && this.currentEquipmentId) {
      this.equipmentService.update(this.currentEquipmentId, this.formModel).subscribe({
        next: () => {
          this.showAlert('Equipment updated successfully!', 'success');
          this.closeModal();
          this.loadEquipment();
          this.loadCategories();
        },
        error: (err) => this.showAlert(err.error?.message || 'Error updating equipment.', 'error')
      });
    } else {
      this.equipmentService.create(this.formModel).subscribe({
        next: () => {
          this.showAlert('Equipment created successfully!', 'success');
          this.closeModal();
          this.loadEquipment();
          this.loadCategories();
        },
        error: (err) => this.showAlert(err.error?.message || 'Error adding equipment.', 'error')
      });
    }
  }

  toggleStatus(item: Equipment, newStatus: string): void {
    if (!item.id) return;
    this.equipmentService.updateStatus(item.id, newStatus).subscribe({
      next: () => {
        item.status = newStatus as any;
        this.showAlert(`Status updated to ${newStatus}`, 'success');
      },
      error: () => this.showAlert('Failed to update status', 'error')
    });
  }

  deleteEquipment(item: Equipment): void {
    if (!item.id) return;
    if (confirm(`Are you sure you want to delete '${item.name}'?`)) {
      this.equipmentService.delete(item.id).subscribe({
        next: () => {
          this.showAlert('Equipment deleted successfully!', 'success');
          this.loadEquipment();
        },
        error: () => this.showAlert('Failed to delete equipment.', 'error')
      });
    }
  }

  showAlert(message: string, type: 'success' | 'error'): void {
    this.alertMessage = message;
    this.alertType = type;
    setTimeout(() => this.alertMessage = '', 4000);
  }
}

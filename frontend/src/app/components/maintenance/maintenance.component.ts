import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MaintenanceService } from '../../services/maintenance.service';
import { EquipmentService } from '../../services/equipment.service';
import { AuthService } from '../../services/auth.service';
import { Maintenance, MaintenanceRequest, MaintenanceResolution } from '../../models/maintenance.model';
import { Equipment } from '../../models/equipment.model';

@Component({
  selector: 'app-maintenance',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './maintenance.component.html',
  styleUrl: './maintenance.component.css'
})
export class MaintenanceComponent implements OnInit {
  tickets: Maintenance[] = [];
  filteredTickets: Maintenance[] = [];
  availableEquipment: Equipment[] = [];
  loading: boolean = true;
  selectedStatus: string = 'ALL';
  searchQuery: string = '';

  // KPI Metrics
  totalActiveCount: number = 0;
  inRepairCount: number = 0;
  resolvedCount: number = 0;
  totalRepairCost: number = 0;

  // Report Modal
  isReportModalOpen: boolean = false;
  reportForm: {
    equipmentId?: number;
    description: string;
  } = {
    equipmentId: undefined,
    description: ''
  };

  // Resolve / Technician Modal
  isResolveModalOpen: boolean = false;
  selectedTicket?: Maintenance;
  resolveForm: {
    status: string;
    cost: number;
    technicianNotes: string;
  } = {
    status: 'REPAIRED',
    cost: 0,
    technicianNotes: ''
  };

  alertMessage: string = '';
  alertType: 'success' | 'error' = 'success';

  constructor(
    private readonly maintenanceService: MaintenanceService,
    private readonly equipmentService: EquipmentService,
    public readonly authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.maintenanceService.getAll().subscribe({
      next: (data) => {
        this.tickets = data;
        this.calculateMetrics();
        this.applyFilter();
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        console.error('Failed to load maintenance records', err);
      }
    });

    this.equipmentService.getAll().subscribe({
      next: (data) => {
        this.availableEquipment = data;
      },
      error: (err) => console.error('Failed to load equipment list', err)
    });
  }

  calculateMetrics(): void {
    this.totalActiveCount = this.tickets.filter(t => t.status === 'REPORTED').length;
    this.inRepairCount = this.tickets.filter(t => t.status === 'UNDER_MAINTENANCE').length;
    this.resolvedCount = this.tickets.filter(t => t.status === 'REPAIRED').length;
    this.totalRepairCost = this.tickets.reduce((acc, t) => acc + (t.cost || 0), 0);
  }

  applyFilter(): void {
    this.filteredTickets = this.tickets.filter(ticket => {
      const matchesStatus = this.selectedStatus === 'ALL' || ticket.status === this.selectedStatus;
      const query = this.searchQuery.toLowerCase().trim();
      const matchesSearch = !query ||
        (ticket.equipmentName && ticket.equipmentName.toLowerCase().includes(query)) ||
        (ticket.equipmentSerialNumber && ticket.equipmentSerialNumber.toLowerCase().includes(query)) ||
        (ticket.reportedByName && ticket.reportedByName.toLowerCase().includes(query)) ||
        (ticket.description && ticket.description.toLowerCase().includes(query));
      return matchesStatus && matchesSearch;
    });
  }

  onFilterChange(): void {
    this.applyFilter();
  }

  openReportModal(): void {
    this.reportForm = {
      equipmentId: this.availableEquipment[0]?.id,
      description: ''
    };
    this.isReportModalOpen = true;
  }

  closeReportModal(): void {
    this.isReportModalOpen = false;
  }

  submitReport(): void {
    if (!this.reportForm.equipmentId || !this.reportForm.description.trim()) {
      this.showAlert('Please select equipment and provide issue description.', 'error');
      return;
    }

    const payload: MaintenanceRequest = {
      equipmentId: this.reportForm.equipmentId,
      description: this.reportForm.description.trim()
    };

    this.maintenanceService.report(payload).subscribe({
      next: () => {
        this.showAlert('Fault reported successfully! Equipment marked as Under Maintenance.', 'success');
        this.closeReportModal();
        this.loadData();
      },
      error: (err) => this.showAlert(err.error?.message || 'Failed to submit fault report.', 'error')
    });
  }

  openResolveModal(ticket: Maintenance): void {
    this.selectedTicket = ticket;
    this.resolveForm = {
      status: ticket.status === 'REPORTED' ? 'UNDER_MAINTENANCE' : 'REPAIRED',
      cost: ticket.cost || 0,
      technicianNotes: ticket.technicianNotes || ''
    };
    this.isResolveModalOpen = true;
  }

  closeResolveModal(): void {
    this.isResolveModalOpen = false;
    this.selectedTicket = undefined;
  }

  submitResolution(): void {
    if (!this.selectedTicket?.id) return;

    const payload: MaintenanceResolution = {
      status: this.resolveForm.status,
      cost: this.resolveForm.cost || 0,
      technicianNotes: this.resolveForm.technicianNotes.trim()
    };

    this.maintenanceService.updateResolution(this.selectedTicket.id, payload).subscribe({
      next: () => {
        const msg = payload.status === 'REPAIRED'
          ? 'Ticket resolved! Equipment restored to AVAILABLE status.'
          : 'Ticket updated successfully!';
        this.showAlert(msg, 'success');
        this.closeResolveModal();
        this.loadData();
      },
      error: (err) => this.showAlert(err.error?.message || 'Failed to update ticket.', 'error')
    });
  }

  quickStatusChange(ticket: Maintenance, newStatus: string): void {
    if (!ticket.id) return;
    const payload: MaintenanceResolution = {
      status: newStatus,
      cost: ticket.cost || 0,
      technicianNotes: ticket.technicianNotes || ''
    };

    this.maintenanceService.updateResolution(ticket.id, payload).subscribe({
      next: () => {
        this.showAlert(`Status updated to ${newStatus}`, 'success');
        this.loadData();
      },
      error: (err) => this.showAlert(err.error?.message || 'Failed to update status', 'error')
    });
  }

  deleteTicket(ticket: Maintenance): void {
    if (!ticket.id) return;
    if (confirm(`Delete maintenance record for '${ticket.equipmentName}'?`)) {
      this.maintenanceService.delete(ticket.id).subscribe({
        next: () => {
          this.showAlert('Maintenance record deleted.', 'success');
          this.loadData();
        },
        error: () => this.showAlert('Failed to delete maintenance record.', 'error')
      });
    }
  }

  showAlert(message: string, type: 'success' | 'error'): void {
    this.alertMessage = message;
    this.alertType = type;
    setTimeout(() => this.alertMessage = '', 4500);
  }
}

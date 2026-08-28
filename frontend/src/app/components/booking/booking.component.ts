import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookingService } from '../../services/booking.service';
import { EquipmentService } from '../../services/equipment.service';
import { AuthService } from '../../services/auth.service';
import { Booking, BookingRequest, AvailabilityResponse } from '../../models/booking.model';
import { Equipment } from '../../models/equipment.model';

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './booking.component.html',
  styleUrl: './booking.component.css'
})
export class BookingComponent implements OnInit {
  bookings: Booking[] = [];
  equipmentList: Equipment[] = [];
  loading = false;
  activeFilter = 'ALL';
  currentTab: 'my' | 'all' = 'my';

  // Modal State
  showModal = false;
  submitting = false;
  checkingAvailability = false;
  availabilityResult: AvailabilityResponse | null = null;
  errorMessage = '';
  successMessage = '';

  // Reject Reason Modal
  showRejectModal = false;
  rejectBookingId: number | null = null;
  rejectReason = '';

  // New Booking Form
  newBooking: BookingRequest = {
    equipmentId: 0,
    startTime: '',
    endTime: '',
    purpose: ''
  };

  constructor(
    public authService: AuthService,
    private bookingService: BookingService,
    private equipmentService: EquipmentService
  ) {}

  ngOnInit(): void {
    if (this.authService.isAdmin()) {
      this.currentTab = 'all';
    }
    this.loadBookings();
    this.loadEquipment();
  }

  switchTab(tab: 'my' | 'all'): void {
    this.currentTab = tab;
    this.loadBookings();
  }

  loadBookings(): void {
    this.loading = true;
    this.errorMessage = '';

    const obs = this.currentTab === 'all' && this.authService.isAdmin()
      ? this.bookingService.getAllBookings()
      : this.bookingService.getMyBookings();

    obs.subscribe({
      next: (data: Booking[]) => {
        this.bookings = data;
        this.loading = false;
      },
      error: (err: any) => {
        this.errorMessage = 'Failed to load bookings. ' + (err.error?.message || '');
        this.loading = false;
      }
    });
  }

  loadEquipment(): void {
    this.equipmentService.getAll().subscribe({
      next: (data: Equipment[]) => {
        this.equipmentList = data.filter((e: Equipment) => e.status === 'AVAILABLE');
      },
      error: () => {}
    });
  }

  get filteredBookings(): Booking[] {
    if (this.activeFilter === 'ALL') return this.bookings;
    return this.bookings.filter(b => b.status === this.activeFilter);
  }

  openBookingModal(preselectedEquipmentId?: number): void {
    this.errorMessage = '';
    this.successMessage = '';
    this.availabilityResult = null;

    // Set default start time tomorrow 09:00 and end time tomorrow 12:00
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    const startIso = `${tomorrow.toISOString().split('T')[0]}T09:00`;
    const endIso = `${tomorrow.toISOString().split('T')[0]}T12:00`;

    this.newBooking = {
      equipmentId: preselectedEquipmentId || (this.equipmentList[0]?.id || 0),
      startTime: startIso,
      endTime: endIso,
      purpose: ''
    };

    this.showModal = true;
    this.checkConflict();
  }

  closeModal(): void {
    this.showModal = false;
    this.availabilityResult = null;
  }

  onTimeOrEquipmentChange(): void {
    this.checkConflict();
  }

  checkConflict(): void {
    if (!this.newBooking.equipmentId || !this.newBooking.startTime || !this.newBooking.endTime) {
      this.availabilityResult = null;
      return;
    }

    const start = this.formatToBackend(this.newBooking.startTime);
    const end = this.formatToBackend(this.newBooking.endTime);

    if (new Date(this.newBooking.endTime) <= new Date(this.newBooking.startTime)) {
      this.availabilityResult = {
        isAvailable: false,
        message: 'End time must be after start time.',
        equipmentId: this.newBooking.equipmentId,
        conflictingSlots: []
      };
      return;
    }

    this.checkingAvailability = true;
    this.bookingService.checkAvailability(this.newBooking.equipmentId, start, end).subscribe({
      next: (res: AvailabilityResponse) => {
        this.availabilityResult = res;
        this.checkingAvailability = false;
      },
      error: () => {
        this.checkingAvailability = false;
      }
    });
  }

  submitBooking(): void {
    if (!this.newBooking.equipmentId || !this.newBooking.startTime || !this.newBooking.endTime || !this.newBooking.purpose) {
      this.errorMessage = 'Please complete all required fields.';
      return;
    }

    if (this.availabilityResult && !this.availabilityResult.isAvailable) {
      this.errorMessage = 'Cannot submit: There is a time conflict with existing reservations!';
      return;
    }

    this.submitting = true;
    this.errorMessage = '';

    const payload: BookingRequest = {
      equipmentId: Number(this.newBooking.equipmentId),
      startTime: this.formatToBackend(this.newBooking.startTime),
      endTime: this.formatToBackend(this.newBooking.endTime),
      purpose: this.newBooking.purpose
    };

    this.bookingService.createBooking(payload).subscribe({
      next: () => {
        this.submitting = false;
        this.showModal = false;
        this.successMessage = 'Equipment reservation submitted successfully!';
        this.loadBookings();
        setTimeout(() => this.successMessage = '', 4000);
      },
      error: (err: any) => {
        this.submitting = false;
        this.errorMessage = err.error?.message || 'Failed to submit booking request.';
      }
    });
  }

  approveBooking(booking: Booking): void {
    if (!confirm(`Approve reservation for '${booking.equipment.name}' by ${booking.user.name}?`)) return;

    this.bookingService.updateBookingStatus(booking.id, { status: 'APPROVED' }).subscribe({
      next: () => {
        this.successMessage = `Booking #${booking.id} approved successfully!`;
        this.loadBookings();
        setTimeout(() => this.successMessage = '', 4000);
      },
      error: (err: any) => {
        this.errorMessage = err.error?.message || 'Failed to approve booking.';
      }
    });
  }

  openRejectDialog(booking: Booking): void {
    this.rejectBookingId = booking.id;
    this.rejectReason = '';
    this.showRejectModal = true;
  }

  confirmReject(): void {
    if (!this.rejectBookingId) return;

    this.bookingService.updateBookingStatus(this.rejectBookingId, {
      status: 'REJECTED',
      adminRemark: this.rejectReason || 'Declined by administrator'
    }).subscribe({
      next: () => {
        this.showRejectModal = false;
        this.rejectBookingId = null;
        this.successMessage = 'Booking rejected with remark.';
        this.loadBookings();
        setTimeout(() => this.successMessage = '', 4000);
      },
      error: (err: any) => {
        this.errorMessage = err.error?.message || 'Failed to reject booking.';
      }
    });
  }

  cancelBooking(booking: Booking): void {
    if (!confirm('Are you sure you want to cancel this reservation?')) return;

    this.bookingService.cancelBooking(booking.id).subscribe({
      next: () => {
        this.successMessage = 'Reservation cancelled successfully.';
        this.loadBookings();
        setTimeout(() => this.successMessage = '', 4000);
      },
      error: (err: any) => {
        this.errorMessage = err.error?.message || 'Failed to cancel reservation.';
      }
    });
  }

  getSelectedEquipment(): Equipment | undefined {
    return this.equipmentList.find(e => e.id == this.newBooking.equipmentId);
  }

  private formatToBackend(datetimeLocal: string): string {
    if (!datetimeLocal) return '';
    const formatted = datetimeLocal.replace('T', ' ');
    return formatted.length === 16 ? formatted + ':00' : formatted;
  }
}

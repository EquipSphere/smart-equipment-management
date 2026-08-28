export interface BookingUser {
  id: number;
  name: string;
  email: string;
  department: string;
  phone?: string;
  role: string;
}

export interface BookingEquipment {
  id: number;
  name: string;
  category: string;
  serialNumber: string;
  location: string;
  imageUrl?: string;
  status: string;
}

export interface Booking {
  id: number;
  user: BookingUser;
  equipment: BookingEquipment;
  startTime: string;
  endTime: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED' | 'COMPLETED';
  purpose: string;
  adminRemark?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface BookingRequest {
  equipmentId: number;
  startTime: string;
  endTime: string;
  purpose: string;
}

export interface BookingStatusUpdate {
  status: 'APPROVED' | 'REJECTED' | 'CANCELLED' | 'COMPLETED';
  adminRemark?: string;
}

export interface ConflictSlot {
  bookingId: number;
  startTime: string;
  endTime: string;
  status: string;
}

export interface AvailabilityResponse {
  isAvailable: boolean;
  message: string;
  equipmentId: number;
  conflictingSlots: ConflictSlot[];
}

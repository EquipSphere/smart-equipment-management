export interface Equipment {
  id?: number;
  name: string;
  category: string;
  serialNumber: string;
  location: string;
  status: 'AVAILABLE' | 'BOOKED' | 'MAINTENANCE';
  description?: string;
  imageUrl?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface EquipmentRequest {
  name: string;
  category: string;
  serialNumber: string;
  location: string;
  status?: string;
  description?: string;
  imageUrl?: string;
}

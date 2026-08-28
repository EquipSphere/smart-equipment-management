export interface Maintenance {
  id?: number;
  equipmentId: number;
  equipmentName?: string;
  equipmentCategory?: string;
  equipmentSerialNumber?: string;
  equipmentLocation?: string;
  equipmentImageUrl?: string;
  reportedById?: number;
  reportedByName?: string;
  reportedByEmail?: string;
  description: string;
  status: 'REPORTED' | 'UNDER_MAINTENANCE' | 'REPAIRED' | 'CANCELLED';
  cost?: number;
  technicianNotes?: string;
  reportedAt?: string;
  resolvedAt?: string;
}

export interface MaintenanceRequest {
  equipmentId: number;
  description: string;
}

export interface MaintenanceResolution {
  status: string;
  cost?: number;
  technicianNotes?: string;
}

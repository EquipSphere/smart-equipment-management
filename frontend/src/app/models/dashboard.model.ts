export interface MostBookedEquipment {
  equipmentName: string;
  bookingCount: number;
}

export interface DashboardStats {
  totalEquipment: number;
  availableEquipment: number;
  bookedEquipment: number;
  underMaintenance: number;
  pendingBookingsCount: number;
  approvedBookingsCount: number;
  totalUsersCount: number;
  activeMaintenanceCount: number;
  equipmentByCategory: { [key: string]: number };
  mostBookedEquipment: MostBookedEquipment[];
}

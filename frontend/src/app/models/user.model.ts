export interface User {
  id?: number;
  name: string;
  email: string;
  role: 'ADMIN' | 'USER';
  phone?: string;
  department?: string;
  createdAt?: string;
}

export interface UserUpdate {
  name: string;
  phone?: string;
  department?: string;
  role?: string;
}

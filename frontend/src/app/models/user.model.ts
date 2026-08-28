export interface User {
  id?: number;
  name: string;
  email: string;
  role: 'ADMIN' | 'USER';
  phone?: string;
  department?: string;
  createdAt?: string;
}

export interface UserCreate {
  name: string;
  email: string;
  password?: string;
  role?: string;
  phone?: string;
  department?: string;
}

export interface UserUpdate {
  name: string;
  phone?: string;
  department?: string;
  role?: string;
}

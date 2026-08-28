export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  phone?: string;
  department?: string;
  role?: 'ADMIN' | 'USER';
}

export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  name: string;
  email: string;
  role: 'ADMIN' | 'USER';
  phone?: string;
  department?: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

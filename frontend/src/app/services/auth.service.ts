import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { AuthResponse, LoginRequest, RegisterRequest, ChangePasswordRequest } from '../models/auth.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private tokenKey = 'equipsphere_token';
  private userKey = 'equipsphere_user';

  // Reactive State with Signals
  currentUser = signal<AuthResponse | null>(this.getUserFromStorage());
  isLoggedIn = computed(() => !!this.currentUser());
  isAdmin = computed(() => this.currentUser()?.role === 'ADMIN');
  userName = computed(() => this.currentUser()?.name || 'User');
  userRole = computed(() => this.currentUser()?.role || 'USER');

  constructor(private http: HttpClient, private router: Router) {}

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, userData).pipe(
      tap(response => this.handleAuthSuccess(response))
    );
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => this.handleAuthSuccess(response))
    );
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/me`);
  }

  changePassword(data: ChangePasswordRequest): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}/change-password`, data);
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private handleAuthSuccess(response: AuthResponse): void {
    localStorage.setItem(this.tokenKey, response.token);
    localStorage.setItem(this.userKey, JSON.stringify(response));
    this.currentUser.set(response);
  }

  private getUserFromStorage(): AuthResponse | null {
    const userStr = localStorage.getItem(this.userKey);
    if (!userStr) return null;
    try {
      return JSON.parse(userStr);
    } catch {
      return null;
    }
  }
}

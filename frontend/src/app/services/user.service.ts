import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserUpdate } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly baseUrl = 'http://localhost:8080/api/users';

  constructor(private readonly http: HttpClient) {}

  getAll(role?: string, keyword?: string): Observable<User[]> {
    let params = new HttpParams();
    if (role && role !== 'ALL') params = params.set('role', role);
    if (keyword) params = params.set('keyword', keyword);
    return this.http.get<User[]>(this.baseUrl, { params });
  }

  getById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }

  create(user: import('../models/user.model').UserCreate): Observable<User> {
    return this.http.post<User>(this.baseUrl, user);
  }

  update(id: number, user: UserUpdate): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/${id}`, user);
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
}

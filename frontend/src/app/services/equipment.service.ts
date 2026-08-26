import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Equipment, EquipmentRequest } from '../models/equipment.model';

@Injectable({
  providedIn: 'root'
})
export class EquipmentService {
  private readonly baseUrl = 'http://localhost:8080/api/equipment';

  constructor(private readonly http: HttpClient) {}

  getAll(category?: string, status?: string): Observable<Equipment[]> {
    let params = new HttpParams();
    if (category && category !== 'ALL') params = params.set('category', category);
    if (status && status !== 'ALL') params = params.set('status', status);
    return this.http.get<Equipment[]>(this.baseUrl, { params });
  }

  getById(id: number): Observable<Equipment> {
    return this.http.get<Equipment>(`${this.baseUrl}/${id}`);
  }

  create(equipment: EquipmentRequest): Observable<Equipment> {
    return this.http.post<Equipment>(this.baseUrl, equipment);
  }

  update(id: number, equipment: EquipmentRequest): Observable<Equipment> {
    return this.http.put<Equipment>(`${this.baseUrl}/${id}`, equipment);
  }

  updateStatus(id: number, status: string): Observable<Equipment> {
    return this.http.patch<Equipment>(`${this.baseUrl}/${id}/status`, null, {
      params: { status }
    });
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  search(keyword: string): Observable<Equipment[]> {
    return this.http.get<Equipment[]>(`${this.baseUrl}/search`, {
      params: { keyword }
    });
  }

  getCategories(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/categories`);
  }
}

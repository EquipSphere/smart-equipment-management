import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Maintenance, MaintenanceRequest, MaintenanceResolution } from '../models/maintenance.model';

@Injectable({
  providedIn: 'root'
})
export class MaintenanceService {
  private readonly baseUrl = 'http://localhost:8080/api/maintenance';

  constructor(private readonly http: HttpClient) {}

  getAll(status?: string): Observable<Maintenance[]> {
    let params = new HttpParams();
    if (status && status !== 'ALL') {
      params = params.set('status', status);
    }
    return this.http.get<Maintenance[]>(this.baseUrl, { params });
  }

  getByEquipment(equipmentId: number): Observable<Maintenance[]> {
    return this.http.get<Maintenance[]>(`${this.baseUrl}/equipment/${equipmentId}`);
  }

  getById(id: number): Observable<Maintenance> {
    return this.http.get<Maintenance>(`${this.baseUrl}/${id}`);
  }

  report(request: MaintenanceRequest): Observable<Maintenance> {
    return this.http.post<Maintenance>(this.baseUrl, request);
  }

  updateResolution(id: number, resolution: MaintenanceResolution): Observable<Maintenance> {
    return this.http.put<Maintenance>(`${this.baseUrl}/${id}`, resolution);
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
}

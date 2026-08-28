import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Booking, BookingRequest, BookingStatusUpdate, AvailabilityResponse } from '../models/booking.model';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private apiUrl = 'http://localhost:8080/api/bookings';

  constructor(private http: HttpClient) {}

  getMyBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}/my`);
  }

  getAllBookings(status?: string, equipmentId?: number): Observable<Booking[]> {
    let params = new HttpParams();
    if (status) params = params.set('status', status);
    if (equipmentId) params = params.set('equipmentId', equipmentId.toString());
    return this.http.get<Booking[]>(this.apiUrl, { params });
  }

  getBookingById(id: number): Observable<Booking> {
    return this.http.get<Booking>(`${this.apiUrl}/${id}`);
  }

  checkAvailability(equipmentId: number, startTime: string, endTime: string): Observable<AvailabilityResponse> {
    return this.http.post<AvailabilityResponse>(`${this.apiUrl}/check-availability`, {
      equipmentId,
      startTime,
      endTime
    });
  }

  createBooking(request: BookingRequest): Observable<Booking> {
    return this.http.post<Booking>(this.apiUrl, request);
  }

  updateBookingStatus(id: number, update: BookingStatusUpdate): Observable<Booking> {
    return this.http.patch<Booking>(`${this.apiUrl}/${id}/status`, update);
  }

  cancelBooking(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

import { Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { EquipmentComponent } from './components/equipment/equipment.component';
import { UsersComponent } from './components/users/users.component';
import { BookingComponent } from './components/booking/booking.component';
import { MaintenanceComponent } from './components/maintenance/maintenance.component';
import { authGuard, adminGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'equipment', component: EquipmentComponent, canActivate: [authGuard] },
  { path: 'bookings', component: BookingComponent, canActivate: [authGuard] },
  { path: 'maintenance', component: MaintenanceComponent, canActivate: [authGuard] },
  { path: 'users', component: UsersComponent, canActivate: [adminGuard] },
  { path: '**', redirectTo: 'dashboard' }
];

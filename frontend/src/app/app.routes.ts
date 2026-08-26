import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { EquipmentComponent } from './components/equipment/equipment.component';
import { UsersComponent } from './components/users/users.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'equipment', component: EquipmentComponent },
  { path: 'users', component: UsersComponent },
  { path: '**', redirectTo: 'dashboard' }
];

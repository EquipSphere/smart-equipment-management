import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DashboardService } from '../../services/dashboard.service';
import { DashboardStats } from '../../models/dashboard.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  loading: boolean = true;
  errorMessage: string = '';

  constructor(private readonly dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load dashboard statistics. Make sure backend is running on port 8080.';
        this.loading = false;
        console.error('Error fetching dashboard stats', err);
      }
    });
  }

  getCategoryEntries(): { category: string; count: number }[] {
    if (!this.stats?.equipmentByCategory) return [];
    return Object.entries(this.stats.equipmentByCategory).map(([category, count]) => ({
      category,
      count
    }));
  }
}

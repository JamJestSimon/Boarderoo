import { Component } from '@angular/core';
import { NavBarComponent } from '../nav-bar/nav-bar.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [NavBarComponent,CommonModule, FormsModule, HttpClientModule],
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.css'
})
export class AdminPanelComponent {
  isAdmin = true;

  constructor(private router: Router) {}

  ngOnInit(): void {
    const sessionToken = localStorage.getItem('session_token_admin');
    if (!sessionToken) {
      // Jeśli token jest pusty, przekierowujemy na stronę główną
      this.router.navigate(['/admin']);
  }
  
}}

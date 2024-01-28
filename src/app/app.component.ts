import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'atm_system';
  constructor(private router: Router) {}

  redirectToService() {
    if(localStorage===null){
      this.router.navigate(['/login']);
    }
    if(localStorage.getItem("account_number")===null){
      this.router.navigate(['/login']);
    }
    const accountNumber = localStorage.getItem('account_number');

    if (accountNumber) { 
      this.router.navigate(['/atmServices']);
    } else {
   
      this.router.navigate(['/login']);
    }
  }
  
}

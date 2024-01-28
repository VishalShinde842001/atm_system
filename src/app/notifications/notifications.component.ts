import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AtmService } from '../atm.service';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent implements OnInit {
  transactions:string[]=[];
  ngOnInit(): void {
    this.atmService.getAllTransactions().subscribe(
      (res)=>{
        this.transactions=res;
      }
    );
  }

  constructor(private atmService:AtmService){

  }
}


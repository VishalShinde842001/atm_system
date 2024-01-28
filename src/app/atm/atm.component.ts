import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AtmService, BalanceEnquiry, TransactionMessage } from '../atm.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-atm',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './atm.component.html',
  styleUrl: './atm.component.css'
})
export class AtmComponent implements OnInit {
  ngOnInit(): void {
   
    if (localStorage.getItem('account_number') === null) {
          this.router.navigate(['/login']);
        }
        this.account_number = localStorage.getItem('account_number') || '';
        this.account_holder=localStorage.getItem("user_name")||'';
        console.log(this.account_holder)
      }
      constructor(private router:Router,private atmService:AtmService){}
  account_number="";
  account_holder="";
  amount=0;
  errorMsg="";
  transactionDone=false;
  checkBal=false
  transaction:TransactionMessage=new TransactionMessage("","","",false,"");
 
  balance:BalanceEnquiry=new BalanceEnquiry(this.transaction,"","","",0);

  getAllNotification(){
    if (!localStorage) {
      this.errorMsg="Session is over login again to do it"
      setTimeout(()=>{
        this.errorMsg=""
      },5000);
      return ;
    }
    const accountNumber = localStorage.getItem("account_number");
  
    if (!accountNumber) {
      this.errorMsg="Session is over login again to do it"
      setTimeout(()=>{
        this.errorMsg=""
      },5000);
      return ;
    }
    this.router.navigate(['/notifications'])
    
  }
  
  deposit() {
    this.atmService.deposit(this.amount).subscribe(
      (res) => {
        this.transaction = res;
       
        if(res.transaction_status){
        this.transactionDone = true;
        setTimeout(() => {
          this.transactionDone = false;
          this.transaction = new TransactionMessage("", "", "", false, "");
          this.errorMsg = "";
        }, 3000);
      }
      else{
        this.errorMsg=res.trasaction_message;
        setTimeout(()=>{
          this.errorMsg=""
        },3000);
      }
        
      },
      (error) => {
        this.errorMsg = "Deposit Failed";
  
        // Set a timeout to clear the error message after 2500 milliseconds (2.5 seconds)
        setTimeout(() => {
          this.errorMsg = "";
        }, 3000);
      }
    );
  }
  changPin() {
    this.atmService.changePin(this.amount).subscribe(
      (res) => {
        this.transaction = res;
       
        if(res.transaction_status){
        this.transactionDone = true;
        setTimeout(() => {
          this.transactionDone = false;
          this.transaction = new TransactionMessage("", "", "", false, "");
          this.errorMsg = "";
        }, 3000);
      }
      else{
        this.errorMsg=res.trasaction_message;
        setTimeout(()=>{
          this.errorMsg=""
        },3000);
      }
        
      },
      (error) => {
        this.errorMsg = "Pin change Failed";
  
        // Set a timeout to clear the error message after 2500 milliseconds (2.5 seconds)
        setTimeout(() => {
          this.errorMsg = "";
        }, 3000);
      }
    );
  }
  withdraw() {
    this.atmService.withdraw(this.amount).subscribe(
      (res) => {
        this.transaction = res;
       
        if(res.transaction_status){
        this.transactionDone = true;
        setTimeout(() => {
          this.transactionDone = false;
          this.transaction = new TransactionMessage("", "", "", false, "");
          this.errorMsg = "";
        }, 3000);
      }
      else{
        this.errorMsg=res.trasaction_message;
        setTimeout(()=>{
          this.errorMsg=""
        },3000);
      }
        
      },
      (error) => {
        this.errorMsg = "Withdrawal Failed";
  
        // Set a timeout to clear the error message after 2500 milliseconds (2.5 seconds)
        setTimeout(() => {
          this.errorMsg = "";
        }, 3000);
      }
    );
  }
  checkBalance(){
    this.atmService.checkBalance().subscribe(
      (res) => {
        this.balance=res;
        this.transaction = res.transactionMessage;
       
        if(res.transactionMessage.transaction_status){
        this.transactionDone = true;
        this.checkBal=true;
        setTimeout(() => {
          this.transactionDone = false;
          this.checkBal=false;
          this.transaction = new TransactionMessage("", "", "", false, "");
          this.errorMsg = "";
        }, 5000);
      }
      else{
        this.errorMsg=res.transactionMessage.trasaction_message;
        setTimeout(()=>{
          this.errorMsg=""
        },5000);
      }
        
      },
      (error) => {
        this.errorMsg = "Balance check Failed";
  
        // Set a timeout to clear the error message after 2500 milliseconds (2.5 seconds)
        setTimeout(() => {
          this.errorMsg = "";
        }, 3000);
      }
    );

  }

  
  logout() {
    if (!localStorage) {
      this.router.navigate(['/login']);
    }
  
    const accountNumber = localStorage.getItem("account_number");
  
    if (!accountNumber) {
      // If account_number is not in sessionStorage, redirect to login
      this.router.navigate(['/login']);
    }
  
    const confirmLogout = window.confirm("Do you really want to logout");
  
    if (confirmLogout) {
      this.atmService.logout().subscribe(
        (res) => {
          if (res) {
            sessionStorage.clear();
            this.router.navigate(['/login']);
          }
        },
        (error) => {
          this.errorMsg = "Error: Something went wrong while logging out. Please attempt to log out again.";
          setTimeout(() => {
            this.errorMsg = "";
          }, 3000);
        }
      );
    }
  }
  
}

import { Component } from '@angular/core';
import { AccountData, LoginMessage, LoginService } from '../login.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginMsg="";
  
  constructor(private loginService:LoginService,private router:Router){}
  loginMessage:LoginMessage=new LoginMessage("",false,"");
  accountData:AccountData=new AccountData("",0);
  login(){
    console.log(this.accountData.account_password||isNaN(Number(this.accountData.account_password)));
    if(this.accountData.account_password===null){
      this.loginMsg="Please Enter 'Pin' in Account Password field(Numeric 4 Digits) "
      setTimeout(
        ()=>{
          this.loginMsg="";
        },2000
      );
      return;

    }
  
    if(this.accountData.account_number===""||this.accountData.account_password===0){
      this.loginMsg="Must fill Account Number and Password";
      setTimeout(
        ()=>{
          this.loginMsg="";
        },2000
      );
      return ;
    }
    this.loginService.login(this.accountData).subscribe(
      (res)=>{
        this.loginMessage=res;
        this.loginMsg=this.loginMessage.login_msg;

          setTimeout(()=>{
            this.loginMsg="";
            this.loginMessage=new LoginMessage("",false,"");
            if (res.login_status) {
              localStorage.setItem("account_number",this.accountData.account_number);
              localStorage.setItem("user_name",this.loginMessage.user_name);
              console.log("Account number stored in session is:"+sessionStorage.getItem("account_number"))
              this.router.navigate(['/atmServices']); // Navigate to the atmservices component
            }

          },2000);


      }
      ,(error)=>{
        this.loginMsg="Some Error Occured Try again";
        console.log(error);
        setTimeout(()=>{
          this.loginMsg="";

        },2000);
      }
    );

  }

}

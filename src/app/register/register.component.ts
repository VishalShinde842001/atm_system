import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AccountInfo, RegisterService, UserDetails } from '../register.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  constructor(private registerService:RegisterService){

  }
  errorMsg="";
  userDetails:UserDetails=new UserDetails("","","",0,"");
  accountInfo:AccountInfo=new AccountInfo("",0,"",false);
  showRegistrationForm = true;
  shoeAccountInfo=false;
  register(){
    if (this.userDetails.age <= 0) {
      this.errorMsg="Age Must Be greater than 0"
      setTimeout(()=>{
        this.errorMsg="";
      },1500);
      console.log("Age Must be greater than 0");
      return;
    }
    this.registerService.register(this.userDetails).subscribe(
      (res)=>{

        this.accountInfo=res;
        if(!res.account_creation_status){
          this.errorMsg=res.msg
          setTimeout(
            ()=>{
              this.errorMsg="";
            },2000
          );
        }
        else {
          this.shoeAccountInfo=true;
          this.showRegistrationForm = false;
          setTimeout(() => {
            this.userDetails = new UserDetails("", "", "", 0, "");
            this.shoeAccountInfo=false;
            this.showRegistrationForm = true;
          }, 5000);
        }
      }
      ,(error)=>{
        this.errorMsg="Something Error Occured Plaese do it again"
        setTimeout(
          ()=>{
            this.errorMsg="";
          },2000
        );
      }
    );
  }
 
}


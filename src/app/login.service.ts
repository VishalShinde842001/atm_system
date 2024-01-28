import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private httpClient:HttpClient) { }
  login(accountData:AccountData):Observable<LoginMessage>{
    return this.httpClient.post<LoginMessage>("http://localhost:8080/login",accountData);
  }


}
export class AccountData{
  account_number:string;
  account_password:number;
  constructor(account_number:string,account_password:number){
    this.account_number=account_number;
    this.account_password=account_password;
  }


}
export class LoginMessage{

  login_msg:string;
  login_status:boolean;
  user_name:string;
  constructor(login_msg:string,login_status:boolean,user_name:string){
    this.login_msg=login_msg;
    this.login_status=login_status;
    this.user_name=user_name;
}
}
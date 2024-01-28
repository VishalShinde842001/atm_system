import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private httpClient:HttpClient) { }
  register(userDetails:UserDetails):Observable<AccountInfo>{
    return this.httpClient.post<AccountInfo>("http://localhost:8080/register",userDetails);
  }
}
export class UserDetails{
  first_name:string;
  last_name:string;
  gender:string;
  age:number;
  email:string;
  constructor(first_name:string,last_name:string,gender:string,age:number,email:string){
    this.first_name=first_name;
    this.last_name=last_name;
    this.age=age;
    this.email=email;
    this.gender=gender;
  }
}
export class AccountInfo{
  account_number:string;
  account_password:number;
  msg:string;
  account_creation_status:boolean;
  constructor(account_number:string,account_password:number,msg:string,account_creation_status:boolean)
{
  this.account_creation_status=account_creation_status;
  this.account_number=account_number;
  this.account_password=account_password;
  this.msg=msg;
}}

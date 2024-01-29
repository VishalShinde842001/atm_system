import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AtmService {
  constructor(private httpClient: HttpClient) { }


  deposit(amount: number): Observable<TransactionMessage> {
    return this.httpClient.put<TransactionMessage>(`http://localhost:8080/deposit`, amount);
  }


  withdraw(amount: number): Observable<TransactionMessage> {
    return this.httpClient.put<TransactionMessage>(`http://localhost:8080/withdraw`, amount)
  }
  changePin(newPin: number): Observable<TransactionMessage> {
    return this.httpClient.put<TransactionMessage>("http://localhost:8080/changePin", newPin);
  }
  logout(): Observable<boolean> {
    return this.httpClient.get<boolean>("http://localhost:8080/logout");
  }
  checkBalance(): Observable<BalanceEnquiry> {
    return this.httpClient.get<BalanceEnquiry>("http://localhost:8080/checkBalance");
  }
  getAllTransactions(): Observable<string[]> {
    return this.httpClient.get<string[]>("http://localhost:8080/getAllTransactions");
  }
  moneyTransfer(moneyTransferDetails:MoneyTransferDetails): Observable<MoneyTransferResponse> {
    return this.httpClient.put<MoneyTransferResponse>("http://localhost:8080/moneyTransfer",moneyTransferDetails)

  }


}
export class MoneyTransferResponse {
  transaction: TransactionMessage;
  receiver: string;
  constructor(transaction: TransactionMessage,
    receiver: string) {
    this.transaction = transaction;
    this.receiver = receiver;
  }

}
export class MoneyTransferDetails {
  account_number: string;
  amount: number;
  constructor(account_number: string, amount: number) {
    this.account_number = account_number;
    this.amount = amount;
  }
}
export class BalanceEnquiry {
  transactionMessage: TransactionMessage;
  account_holder_first_name: string;
  account_holder_last_name: string;
  account_number: string;
  account_balance: number;
  constructor(transactionMessage: TransactionMessage, account_holder_first_name: string, account_holder_last_name: string, account_number: string, account_balance: number) {
    this.transactionMessage = transactionMessage;
    this.account_holder_first_name = account_holder_first_name;
    this.account_holder_last_name = account_holder_last_name;
    this.account_number = account_number;
    this.account_balance = account_balance;
  }

}

export class TransactionMessage {
  date: string;
  time: string;
  trasaction_message: string;
  transaction_status: boolean;
  trasaction_id: string;
  constructor(date: string, time: string, transaction_message: string, transaction_status: boolean, trasaction_id: string) {
    this.trasaction_message = transaction_message;
    this.transaction_status = transaction_status
    this.trasaction_id = trasaction_id;
    this.date = date;
    this.time = time;

  }
}
export class StatusColorService {
  getColor(status: boolean): string {
    switch (status) {
      case true: return 'green';
      case false: return 'red';
      default: return 'orange';
    }
  }
}
package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.classtoreturn.BalanceEnquiry;
import com.classtoreturn.LoginMessage;
import com.classtoreturn.TransactionMessage;
import com.entity.AccountData;
import com.entity.AccountInfo;
import com.entity.MiniStatement;
import com.entity.MoneyTransferDetails;
import com.entity.MoneyTransferResponse;
import com.entity.Transaction;
import com.entity.UserDetails;
import com.helper.DateAndTimeCreation;
import com.service.TransactionService;
import com.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

//I used single controller class to maintain all controller requests
@RestController
@CrossOrigin("http://localhost:4200")
public class MainController {
	// UserService is autowired to do operations with User table
	@Autowired
	private UserService userService;

	@Autowired
	private TransactionMessage trm;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private BalanceEnquiry be;
	@Autowired
	private MoneyTransferResponse mtr;

	@Autowired
	private MiniStatement miniStatement;
	@Autowired
	private DateAndTimeCreation dateAndTimeCreation;

	private HttpSession httpSession;

	@PostMapping("/register")
	public ResponseEntity<AccountInfo> register(@RequestBody UserDetails userDeatails) {
		AccountInfo aci;
		try {
			aci = this.userService.register(userDeatails);
			if (aci.isAccount_creation_status()) {
				System.out.println("Account Created Successfully:" + aci.getAccount_number());
				return ResponseEntity.ok(aci);
			}
			System.out.println("Account Not Created");
			return ResponseEntity.ok(aci);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error Occred While Creating Account");
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<LoginMessage> login(@RequestBody AccountData accountData, HttpServletRequest request) {
		try {
			LoginMessage loginMessage = this.userService.findByAccountNumber(accountData);
			if (loginMessage.isLogin_status()) {
				httpSession = request.getSession();
				httpSession.setAttribute("account_number", accountData.getAccount_number());
				System.out.println("Login Succesfully and value stored in session is:");
				System.out.println("Account Number:" + httpSession.getAttribute("account_number"));
				return ResponseEntity.ok(loginMessage);
			}

			return ResponseEntity.ok(loginMessage);
		} catch (Exception e) {
			System.out.println("Something went wrong");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("/logout")
	public ResponseEntity<Boolean> logout() {
		try {
			if (httpSession == null) {
				System.out.println("Session is null so logged out");
				return ResponseEntity.ok(true);
			}
			if (httpSession.getAttribute("account_number") == null) {
				System.out.println("Account number in session is null so logged out");
				return ResponseEntity.ok(true);
			}
			System.out.println("User Was Login I but now logged out");
			httpSession.invalidate();
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_IMPLEMENTED);
		}

	}

	@PutMapping("/deposit")
	public ResponseEntity<TransactionMessage> deposit(@RequestBody double amount) {
		try {
			if (httpSession == null) {
				trm.setTrasaction_message(
						"Transaction Error: The session is currently unavailable. Please log in again to initiate the transaction.");
				trm.setTransaction_status(false);
				return ResponseEntity.ok(trm);

			}
			if (httpSession.getAttribute("account_number") == null) {
				trm.setTrasaction_message(
						"Transaction Error: Your session has expired. Kindly log in again to proceed with the transaction.");
				trm.setTransaction_status(false);
				return ResponseEntity.ok(trm);

			}
			String account_number = (String) httpSession.getAttribute("account_number");
			trm = this.transactionService.deposit(amount, account_number);
			if (trm.isTransaction_status()) {
				String date = dateAndTimeCreation.dateAndTimeCreator("Date");
				String time = dateAndTimeCreation.dateAndTimeCreator("Time");
				trm.setDate(date);
				trm.setTime(time);
				return ResponseEntity.ok(trm);
			}
			return ResponseEntity.ok(trm);
		} catch (Exception e) {
			trm.setTrasaction_message(
					"Deposit Error: Your session has already been invalidated. Please log in again to initiate a deposit.");
			trm.setTransaction_status(false);
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(trm);

		}
	}

	@PutMapping("/withdraw")
	public ResponseEntity<TransactionMessage> withdraw(@RequestBody double amount) {
		try {
			if (httpSession == null) {
				trm.setTrasaction_message(
						"Transaction Error: The session is currently unavailable. Please log in again to initiate the transaction.");
				trm.setTransaction_status(false);
				return ResponseEntity.ok(trm);

			}
			if (httpSession.getAttribute("account_number") == null) {
				trm.setTrasaction_message(
						"Transaction Error: Your session has expired. Kindly log in again to proceed with the transaction.");
				trm.setTransaction_status(false);
				return ResponseEntity.ok(trm);

			}
			String account_number = (String) httpSession.getAttribute("account_number");
			trm = this.transactionService.withdraw(amount, account_number);
			if (trm.isTransaction_status()) {
				String date = dateAndTimeCreation.dateAndTimeCreator("Date");
				String time = dateAndTimeCreation.dateAndTimeCreator("Time");
				trm.setDate(date);
				trm.setTime(time);
				return ResponseEntity.ok(trm);
			}
			return ResponseEntity.ok(trm);
		} catch (Exception e) {
			trm.setTrasaction_message(
					"Withdrawal Error: Your session has already been invalidated. Please log in again to attempt a withdrawal");
			trm.setTransaction_status(false);
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(trm);

		}
	}

	@PutMapping("/changePin")
	public ResponseEntity<TransactionMessage> changePin(@RequestBody int pin) {
		try {
			if (httpSession == null) {
				trm.setTrasaction_message(
						"Transaction Error: The session is currently unavailable. Please log in again to initiate the transaction.");
				trm.setTransaction_status(false);
				return ResponseEntity.ok(trm);

			}
			if (httpSession.getAttribute("account_number") == null) {
				trm.setTrasaction_message(
						"Transaction Error: Your session has expired. Kindly log in again to proceed with the transaction.");
				trm.setTransaction_status(false);
				return ResponseEntity.ok(trm);

			}
			String account_number = (String) httpSession.getAttribute("account_number");
			trm = this.transactionService.changePin(pin, account_number);

			/*
			 * if (pcr.isPassword_change_status()) {
			 * 
			 * return ResponseEntity.ok(pcr); }
			 */
			return ResponseEntity.ok(trm);
		} catch (Exception e) {
			e.printStackTrace();
			trm.setTrasaction_message(
					"Pin Change Error: Your session has already been invalidated. Please log in again to perform the transaction.");
			trm.setTransaction_status(false);
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(trm);
		}

	}

	@GetMapping("/checkBalance")
	public ResponseEntity<BalanceEnquiry> checkBalance() {
		try {
			if (httpSession == null) {
				trm.setTrasaction_message(
						"Transaction Error: The session is currently unavailable. Please log in again to initiate the transaction.");
				trm.setTransaction_status(false);
				be.setTransactionMessage(trm);
				return ResponseEntity.ok(be);

			}
			if (httpSession.getAttribute("account_number") == null) {
				trm.setTrasaction_message(
						"Transaction Error: Your session has expired. Kindly log in again to proceed with the transaction.");
				trm.setTransaction_status(false);
				be.setTransactionMessage(trm);
				return ResponseEntity.ok(be);
			}

			String account_number = (String) httpSession.getAttribute("account_number");
			be = this.transactionService.checkBalance(account_number);

			return ResponseEntity.ok(be);

		} catch (Exception r) {
			r.printStackTrace();
			trm.setTrasaction_message("Somthing Error occured in Check Balance Login Agian and try");

			trm.setTransaction_status(false);
			be.setTransactionMessage(trm);
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(be);

		}

	}

	@PutMapping("/moneyTransfer")
	public ResponseEntity<MoneyTransferResponse> moneyTransfer(@RequestBody MoneyTransferDetails mtd) {
		if (httpSession == null) {
			trm.setTrasaction_message(
					"Transaction Error: The session is currently unavailable. Please log in again to initiate the transaction.");
			trm.setTransaction_status(false);
			mtr.setTransaction(trm);
			return ResponseEntity.ok(mtr);

		}
		if (httpSession.getAttribute("account_number") == null) {
			trm.setTrasaction_message(
					"Transaction Error: Your session has expired. Kindly log in again to proceed with the transaction.");
			trm.setTransaction_status(false);
			mtr.setTransaction(trm);
			return ResponseEntity.ok(mtr);

		}
		String account_number = (String) httpSession.getAttribute("account_number");
		mtr = this.transactionService.moneyTransfer(account_number, mtd.getAccount_number(), mtd.getAmount());
		if (mtr.getTransaction().isTransaction_status()) {
			return ResponseEntity.ok(mtr);
		}
		return ResponseEntity.ok(mtr);

	}

	@GetMapping("/miniStatement")
	public ResponseEntity<MiniStatement> miniStatement() {
		try {
			if (httpSession == null) {
				this.miniStatement.setMinistatment_status(false);
				this.miniStatement.setMinistatment_msg("Can't find session So Login Again");
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(miniStatement);
			}
			if (httpSession.getAttribute("account_number") == null) {
				this.miniStatement.setMinistatment_status(false);
				this.miniStatement.setMinistatment_msg("Can't find user So Login Again");
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(miniStatement);
			}
			String account_number = (String) httpSession.getAttribute("account_number");
			return ResponseEntity.ok(this.transactionService.miniStatement(account_number));

		} catch (Exception e) {
			e.printStackTrace();
			this.miniStatement.setMinistatment_msg("Error occured While getting Mini Statement");
			this.miniStatement.setMinistatment_status(false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this.miniStatement);

		}
	}
	@GetMapping("/getAllTransactions")
	public List<String> getAllTransaction(){
		try {
			if (httpSession == null) {
				this.miniStatement.setMinistatment_status(false);
				this.miniStatement.setMinistatment_msg("Can't find session So Login Again");
				return null;
			}
			if (httpSession.getAttribute("account_number") == null) {
				this.miniStatement.setMinistatment_status(false);
				this.miniStatement.setMinistatment_msg("Can't find user So Login Again");
				return null;
			}
			String account_number=(String)httpSession.getAttribute("account_number");
			return this.transactionService.getAllTransactionNotification(account_number);
			
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}

package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.classtoreturn.BalanceEnquiry;
import com.classtoreturn.PasswordChangeResponse;
import com.classtoreturn.TransactionMessage;
import com.entity.AccountData;
import com.entity.AccountInfo;
import com.entity.MiniStatement;
import com.entity.MoneyTransferDetails;
import com.entity.MoneyTransferResponse;

import com.entity.User;
import com.entity.UserDetails;
import com.service.TransactionService;
import com.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
//I used single controller class to maintain all controller requests
@RestController
@CrossOrigin("http://localhost:4200")
public class MainController {
	//UserService is autowired to do operations with User table
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionMessage trm;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private PasswordChangeResponse pcr;
	@Autowired
	private BalanceEnquiry be;
	@Autowired
	private MoneyTransferResponse mtr;

	@Autowired
	private MiniStatement miniStatement;
	
	private HttpSession httpSession;

	@PostMapping("/register")
	public ResponseEntity<AccountInfo> register(@RequestBody UserDetails userDeatails) {
		AccountInfo aci;
		try {
			aci=this.userService.register(userDeatails);
		if (aci.isAccount_creation_status()) {
			System.out.println("Account Created Successfully:"+aci.getAccount_number());
			return ResponseEntity.ok(aci);
		}
		System.out.println("Account Not Created");
		return ResponseEntity.ok(aci);}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error Occred While Creating Account");
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<Boolean> login(@RequestBody AccountData accountData, HttpServletRequest request) {
		try {
			User u = this.userService.findByAccountNumber(accountData);
			if (u == null) {
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
			httpSession = request.getSession();
			httpSession.setAttribute("account_number", u.getAccount_number());
			System.out.println("Login Succesfully and value stored in session is:");
			System.out.println("Account Number:" + httpSession.getAttribute("account_number"));
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			System.out.println("You send wrong request boy");
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
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

	@PutMapping("/deposit/{amount}")
	public ResponseEntity<TransactionMessage> deposit(@PathVariable double amount) {
		try {
			if (httpSession == null) {
				trm.setTrasaction_message("Actually Session is null!Try To Do With Login Again");
				trm.setTransaction_status(false);
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(trm);
			}
			if (httpSession.getAttribute("account_number") == null) {
				trm.setTrasaction_message("Actually Session is null!Try To Do With Login Again");
				trm.setTransaction_status(false);
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(trm);

			}
			String account_number = (String) httpSession.getAttribute("account_number");
			trm = this.transactionService.deposit(amount, account_number);
			if (trm.isTransaction_status()) {
				return ResponseEntity.ok(trm);
			}
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(trm);
		} catch (Exception e) {
			trm.setTrasaction_message("Session Already invalidated So login again for transaction");
			trm.setTransaction_status(false);
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(trm);
		}
	}

	@PutMapping("/withdraw/{amount}")
	public ResponseEntity<TransactionMessage> withdraw(@PathVariable double amount) {
		try {
			if (httpSession == null) {
				trm.setTrasaction_message("Actually Session is null!Try To Do With Login Again");
				trm.setTransaction_status(false);
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(trm);
			}
			if (httpSession.getAttribute("account_number") == null) {
				trm.setTrasaction_message("Actually Session is null!Try To Do With Login Again");
				trm.setTransaction_status(false);
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(trm);

			}
			String account_number = (String) httpSession.getAttribute("account_number");
			trm = this.transactionService.withdraw(amount, account_number);
			if (trm.isTransaction_status()) {
				return ResponseEntity.ok(trm);
			}
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(trm);
		} catch (Exception e) {
			trm.setTrasaction_message("Session Already invalidated So login again for transaction");
			trm.setTransaction_status(false);
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(trm);
		}
	}

	@PutMapping("/changepin/{pin}")
	public ResponseEntity<PasswordChangeResponse> changePin(@PathVariable int pin) {
		try {
			if (httpSession == null) {
				pcr.setPassword_change_message("Actually Session is null!Try To Do With Login Again");
				pcr.setPassword_change_status(false);
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(pcr);
			}
			if (httpSession.getAttribute("account_number") == null) {
				pcr.setPassword_change_message("Actually Session is null!Try To Do With Login Again");
				pcr.setPassword_change_status(false);
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(pcr);

			}
			String account_number = (String) httpSession.getAttribute("account_number");
			pcr = this.transactionService.changePin(pin, account_number);

			if (pcr.isPassword_change_status()) {

				return ResponseEntity.ok(pcr);
			}
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(pcr);
		} catch (Exception e) {
			e.printStackTrace();
			pcr.setPassword_change_message("Session Already invalidated So login again for transaction");
			pcr.setPassword_change_status(false);
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(pcr);
		}

	}

	@GetMapping("/checkBalance")
	public ResponseEntity<BalanceEnquiry> checkBalance() {
		try {
			if (httpSession == null) {
				be.setEnquiry_message("Actually Session is null!Try To Do With Login Again");
				be.setBalance_enquiry_status(false);
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(be);
			}
			if (httpSession.getAttribute("account_number") == null) {
				be.setEnquiry_message("Actually Session is null!Try To Do With Login Again");
				be.setBalance_enquiry_status(false);
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(be);
			}
			String account_number = (String) httpSession.getAttribute("account_number");
			be = this.transactionService.checkBalance(account_number);
			if (be.isBalance_enquiry_status()) {
				return ResponseEntity.ok(be);
			}

			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(be);

		} catch (Exception r) {
			r.printStackTrace();
			be.setEnquiry_message("Somthing Error occured in Check Balance Login Agian and try");
			be.setBalance_enquiry_status(false);
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(be);

		}

	}

	@PutMapping("/moneyTransfer")
	public ResponseEntity<MoneyTransferResponse> moneyTransfer(@RequestBody MoneyTransferDetails mtd) {
		if (httpSession == null) {
			mtr.setMoney_transfer_message("Session is Null So Login Again To Transfer Money");
			mtr.setMoney_transfer_status(false);
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(mtr);
		}
		if (httpSession.getAttribute("account_number") == null) {
			mtr.setMoney_transfer_message("Session is Null So Login Again To Transfer Money");
			mtr.setMoney_transfer_status(false);
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(mtr);
		}
		String account_number = (String) httpSession.getAttribute("account_number");
		mtr = this.transactionService.moneyTransfer(account_number, mtd.getAccount_number(), mtd.getAmount());
		if (mtr.isMoney_transfer_status()) {
			return ResponseEntity.ok(mtr);
		}
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(mtr);

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

}

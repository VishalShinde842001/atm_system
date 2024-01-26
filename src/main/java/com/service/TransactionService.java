package com.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classtoreturn.BalanceEnquiry;
import com.classtoreturn.PasswordChangeResponse;
import com.classtoreturn.TransactionMessage;
import com.dao.TransactionDao;
import com.dao.UserDao;
import com.entity.MoneyTransferResponse;
import com.entity.Transaction;
import com.entity.User;
import com.helper.DateAndTimeCreation;
import com.helper.TransactionIdCreation;

@Service
public class TransactionService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private TransactionMessage transactionMessage;
	@Autowired
	private PasswordChangeResponse pcr;
	@Autowired
	private BalanceEnquiry ble;
	@Autowired
	private MoneyTransferResponse mtr;
	@Autowired
	private TransactionDao trdao;
	@Autowired
	private TransactionIdCreation trasactionIdCreation;
	@Autowired
	private DateAndTimeCreation dateTimeCreation;

	private boolean isTransaactionCalled;
	private String transferedTo;
	private String transferdFrom;
	private String transactionIdForMoneyTransfer;

	public TransactionMessage deposit(double amount, String account_number) {
		try {
			if (amount <= 0) {
				transactionMessage.setTransaction_status(false);
				transactionMessage
						.setTrasaction_message("You Are Trying to deposit money less than 1rs so request rejected");
				return transactionMessage;
			}
			User u = this.findByAccountNumber(account_number);
			if (u == null) {
				transactionMessage.setTrasaction_message("Actually User Not Found!Try To Do With Login Again");
				transactionMessage.setTransaction_status(false);
				return transactionMessage;
			}
			double prevBalance = u.getAccount_balance();
			double newBalance = prevBalance + amount;
			u.setAccount_balance(newBalance);
			this.userDao.save(u);
			transactionMessage.setTrasaction_message(
					"Transaction Done Successfully!Amount Depositted:" + amount + " Current Balance:" + newBalance);
			transactionMessage.setTransaction_status(true);

			boolean b;
			if (isTransaactionCalled) {
				transactionMessage.setTrasaction_id(this.transactionIdForMoneyTransfer);
				b = this.saveTransaction("MR" + this.transactionIdForMoneyTransfer, this.transferedTo,
						this.transferdFrom, this.transferedTo, "Money Received", amount, prevBalance, newBalance);
			} else {
				String trasactionId = "DE" + this.trasactionIdCreation.createTransactionId();
				transactionMessage.setTrasaction_id(trasactionId);
				b = this.saveTransaction(trasactionId, account_number, null, null, "Deposit", amount, prevBalance,
						newBalance);
			}
			if (b) {
				System.out.println("Transaction Saved");
			}
			return transactionMessage;

		} catch (Exception e) {
			e.printStackTrace();
			transactionMessage.setTrasaction_message("Somthing Error Occured Inside Backend");
			transactionMessage.setTransaction_status(false);
			return transactionMessage;

		}
	}

	public boolean saveTransaction(String transactionId, String account_number, String sender, String receiver,
			String transaction_type, double amount, double prev_account_balance, double curr_account_balance) {
		try {
			if (transactionId == null || account_number == null) {
				return false;
			}
			LocalDateTime currentDateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String dateTime = currentDateTime.format(formatter);

			Transaction t = new Transaction(transactionId, account_number, sender, receiver, transaction_type, amount,
					prev_account_balance, curr_account_balance, dateTime);
			this.trdao.save(t);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error While saving Transaction");
			return false;
		}
	}

	public TransactionMessage withdraw(double amount, String account_number) {
		try {
			if (amount <= 0) {
				transactionMessage.setTransaction_status(false);
				transactionMessage
						.setTrasaction_message("You Are Trying to withdraw money less than 1rs so request rejected");
				return transactionMessage;
			}
			User u = this.findByAccountNumber(account_number);
			if (u == null) {
				transactionMessage.setTrasaction_message("Actually User Not Found!Try To Do With Login Again");
				transactionMessage.setTransaction_status(false);
				return transactionMessage;
			}
			if (u.getAccount_balance() < amount) {
				transactionMessage
						.setTrasaction_message("Insufficient Fund !,Your Account Balance is:" + u.getAccount_balance());
				transactionMessage.setTransaction_status(false);
				return transactionMessage;
			}
			double prevBalance = u.getAccount_balance();
			double newBalance = prevBalance - amount;
			u.setAccount_balance(newBalance);
			this.userDao.save(u);

			transactionMessage.setTrasaction_message(
					"Transaction Done Successfully! Amount Withdraw:" + amount + " Current Balance:" + newBalance);
			transactionMessage.setTransaction_status(true);

			boolean b;
			if (isTransaactionCalled) {
				System.out.println(isTransaactionCalled);
				transactionMessage.setTrasaction_id(this.transactionIdForMoneyTransfer);
				b = this.saveTransaction("MT" + this.transactionIdForMoneyTransfer, account_number, account_number,
						this.transferedTo, "Money Transfer", amount, prevBalance, newBalance);
			} else {
				String trasactionId = "WI" + this.trasactionIdCreation.createTransactionId();
				transactionMessage.setTrasaction_id(trasactionId);
				b = this.saveTransaction(trasactionId, account_number, null, null, "Withdraw", amount, prevBalance,
						newBalance);
			}
			if (b) {
				System.out.println("Transaction Saved");
			}
			return transactionMessage;

		} catch (Exception e) {
			e.printStackTrace();
			transactionMessage.setTrasaction_message("Somthing Error Occured Inside Backend");
			transactionMessage.setTransaction_status(false);
			return transactionMessage;

		}
	}

	public PasswordChangeResponse changePin(int newPin, String account_number) {
		try {
			int b = validatePasswordLengthAs4(newPin);
			if (b != 4) {
				pcr.setPassword_change_message("Pin Must be 4 Digit only" + " You Entered " + b + " Digits");
				pcr.setPassword_change_status(false);
			}
			User u = this.findByAccountNumber(account_number);
			if (u == null) {
				pcr.setPassword_change_message("Some Error Occured Try After login again");
				pcr.setPassword_change_status(false);
			}
			int oldPass = u.getAccount_password();
			u.setAccount_password(newPin);
			this.userDao.save(u);
			String trId = "PC" + trasactionIdCreation.createTransactionId();
			String date = dateTimeCreation.dateAndTimeCreator("Date");
			String time = dateTimeCreation.dateAndTimeCreator("Time");
			pcr.setDate(date);
			pcr.setTime(time);
			pcr.setTransaction_id(trId);
			pcr.setPassword_change_message(
					"Password Updated Successfully! Old Password:" + oldPass + " New Password: " + newPin);
			pcr.setPassword_change_status(true);
			this.saveTransaction(trId, account_number, null, null, "Pin Change", 0, u.getAccount_balance(),
					u.getAccount_balance());
			return pcr;

		} catch (Exception e) {
			e.printStackTrace();
			pcr.setPassword_change_message("Some Error Occured Try After login again");
			pcr.setPassword_change_status(false);
			return pcr;
		}

	}

	public int validatePasswordLengthAs4(int password) {

		String passwordString = String.valueOf(password);

		return passwordString.length();
	}

	public User findByAccountNumber(String account_number) {
		try {
			Optional<User> u = this.userDao.findById(account_number);
			if (u.isPresent()) {
				return u.get();

			}
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public BalanceEnquiry checkBalance(String account_number) {
		try {
			User u = this.findByAccountNumber(account_number);
			if (u == null) {
				ble.setEnquiry_message("Actuallly Login Again To Get Balance Info");
				ble.setBalance_enquiry_status(false);
				return ble;
			}
			ble.setEnquiry_message("Balance Enquiry");
			String trId = "BE" + trasactionIdCreation.createTransactionId();

			String date = dateTimeCreation.dateAndTimeCreator("Date");
			String time = dateTimeCreation.dateAndTimeCreator("Time");
			double bal = u.getAccount_balance();
			ble.setTransaction_id(trId);
			ble.setDate(date);
			ble.setTime(time);
			ble.setAccount_holder_first_name(u.getUserDetails().getFirst_name());
			ble.setAccount_holder_last_name(u.getUserDetails().getLast_name());
			ble.setAccount_number(u.getAccount_number());
			ble.setBalance_enquiry_status(true);
			ble.setAccount_balance(u.getAccount_balance());
			this.saveTransaction(trId, u.getAccount_number(), null, null, "Balance Enquiry", 0, bal, bal);
			return ble;

		} catch (Exception e) {
			e.printStackTrace();
			ble.setEnquiry_message("Actuallly Login Again To Get Balance Info");
			ble.setBalance_enquiry_status(false);
			return ble;

		}

	}

	public MoneyTransferResponse moneyTransfer(String fromAccountNumber, String toAccountNumber, double amount) {
		try {
			if (fromAccountNumber.equals(toAccountNumber)) {
				mtr.setMoney_transfer_status(false);
				mtr.setMoney_transfer_message("You are trying to Transfer Money on your account so request rejected");
				return mtr;
			}
			if (amount <= 0) {
				mtr.setMoney_transfer_status(false);
				mtr.setMoney_transfer_message("You are trying to Transfer 0rs or negative so request rejected");
				return mtr;
			}
			User u = this.findByAccountNumber(fromAccountNumber);
			if (u == null) {
				mtr.setMoney_transfer_status(false);
				mtr.setMoney_transfer_message("Money Sender Id Problem Login Again To Transfer  Money");
				return mtr;
			}
			double senderBalance = u.getAccount_balance();
			if (senderBalance < amount) {
				mtr.setMoney_transfer_status(false);
				mtr.setMoney_transfer_message("Money Not Sent Fund Insufficient : Your Balance: " + senderBalance
						+ " You are trying to Transfer : " + amount);
				return mtr;
			}
			User toAccount = this.findByAccountNumber(toAccountNumber);
			if (toAccount == null) {
				mtr.setMoney_transfer_status(false);
				mtr.setMoney_transfer_message(
						"You are Trying to Send Money But Receiver Account Number is wrong Check Again");
				return mtr;
			}
			double newBal = u.getAccount_balance() - amount;
			transactionIdForMoneyTransfer = this.trasactionIdCreation.createTransactionId();
			this.isTransaactionCalled = true;
			this.transferedTo = toAccountNumber;
			this.transferdFrom = fromAccountNumber;
			this.transactionMessage = this.withdraw(amount, fromAccountNumber);
			this.transactionMessage = this.deposit(amount, toAccountNumber);

			mtr.setMoney_transfer_status(true);
			mtr.setTransaction_id("S/R" + this.transactionIdForMoneyTransfer);
			String date = dateTimeCreation.dateAndTimeCreator("Date");
			String time = dateTimeCreation.dateAndTimeCreator("Time");
			mtr.setDate(date);
			mtr.setTime(time);
			mtr.setReceiver(toAccountNumber);
			mtr.setMoney_transfer_message("Money Transfer Successfully: " + amount + " Transfered To " + toAccountNumber
					+ " Your Current Balance is:" + newBal);
			return mtr;

		} catch (Exception e) {
			e.printStackTrace();
			mtr.setMoney_transfer_status(false);
			mtr.setMoney_transfer_message("Some Error Occured Try Again");
			return mtr;
		}

	}
}

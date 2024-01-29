package com.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classtoreturn.BalanceEnquiry;

import com.classtoreturn.TransactionMessage;
import com.dao.TransactionDao;
import com.dao.UserDao;
import com.entity.MiniStatement;
import com.entity.MoneyTransferResponse;
import com.entity.Transaction;
import com.entity.User;
import com.helper.DateAndTimeCreation;
import com.helper.TransactionIdCreation;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private TransactionMessage transactionMessage;
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
	@Autowired
	private MiniStatement miniStatement;

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

	public TransactionMessage changePin(int newPin, String account_number) {
		try {
			if (newPin < 0) {
				transactionMessage.setTrasaction_message("Password Can't be Negative");
				transactionMessage.setTransaction_status(false);
				return transactionMessage;
			}
			int b = validatePasswordLengthAs4(newPin);
			if (b != 4) {
				transactionMessage.setTrasaction_message("Pin Must be 4 Digit only" + " You Entered " + b + " Digits");
				transactionMessage.setTransaction_status(false);
				return transactionMessage;
			}
			User u = this.findByAccountNumber(account_number);
			if (u == null) {
				transactionMessage.setTrasaction_message("Some Error Occured Try After login again");
				transactionMessage.setTransaction_status(false);
				return transactionMessage;
			}
			int oldPass = u.getAccount_password();
			if (oldPass == newPin) {
				transactionMessage.setTrasaction_message(
						"Try Again: New password should be distinct from the existing password.");
				transactionMessage.setTransaction_status(false);
				return transactionMessage;
			}
			u.setAccount_password(newPin);
			this.userDao.save(u);
			String trId = "PC" + trasactionIdCreation.createTransactionId();
			String date = dateTimeCreation.dateAndTimeCreator("Date");
			String time = dateTimeCreation.dateAndTimeCreator("Time");
			transactionMessage.setDate(date);
			transactionMessage.setTime(time);
			transactionMessage.setTrasaction_id(trId);

			transactionMessage.setTrasaction_message(
					"Password Updated Successfully! Old Password:" + oldPass + " New Password: " + newPin);
			transactionMessage.setTransaction_status(true);
			this.saveTransaction(trId, account_number, null, null, "Pin Change", 0, u.getAccount_balance(),
					u.getAccount_balance());
			return transactionMessage;

		} catch (Exception e) {
			e.printStackTrace();
			transactionMessage.setTrasaction_message("Some Error Occured Try After login again");
			transactionMessage.setTransaction_status(false);
			return transactionMessage;
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
				transactionMessage.setTrasaction_message("Actuallly Login Again To Get Balance Info");
				transactionMessage.setTransaction_status(false);
				ble.setTransactionMessage(transactionMessage);
				return ble;
			}
			transactionMessage.setTrasaction_message("Balance Enquiry");
			String trId = "BE" + trasactionIdCreation.createTransactionId();

			String date = dateTimeCreation.dateAndTimeCreator("Date");
			String time = dateTimeCreation.dateAndTimeCreator("Time");
			double bal = u.getAccount_balance();
			transactionMessage.setDate(date);
			transactionMessage.setTime(time);

			ble.setAccount_holder_first_name(u.getUserDetails().getFirst_name());
			ble.setAccount_holder_last_name(u.getUserDetails().getLast_name());
			ble.setAccount_number(u.getAccount_number());
			transactionMessage.setTransaction_status(true);
			ble.setTransactionMessage(transactionMessage);
			ble.setAccount_balance(u.getAccount_balance());
			this.saveTransaction(trId, u.getAccount_number(), null, null, "Balance Enquiry", 0, bal, bal);
			return ble;

		} catch (Exception e) {
			e.printStackTrace();
			transactionMessage.setTrasaction_message("Actuallly Login Again To Get Balance Info");
			transactionMessage.setTransaction_status(false);
			ble.setTransactionMessage(transactionMessage);
			return ble;

		}

	}

	public MoneyTransferResponse moneyTransfer(String fromAccountNumber, String toAccountNumber, double amount) {
		try {
			if (fromAccountNumber.equals(toAccountNumber)) {
				transactionMessage.setTransaction_status(false);
				transactionMessage.setTrasaction_message(
						"Transaction Rejected: You cannot transfer money to your own account. Please provide a different recipient account for the transfer.");
				mtr.setTransaction(transactionMessage);
				return mtr;
			}
			if (amount <= 0) {
				transactionMessage.setTransaction_status(false);
				transactionMessage.setTrasaction_message(
						"Transaction Rejected: The transfer amount must be greater than zero. Please provide a valid and positive amount to proceed.");
				mtr.setTransaction(transactionMessage);
				return mtr;

			}
			User u = this.findByAccountNumber(fromAccountNumber);
			if (u == null) {
				transactionMessage.setTransaction_status(false);
				transactionMessage.setTrasaction_message(
						"Transaction Rejected: Unable to identify the sender. Please log in again to initiate the money transfer.");
				mtr.setTransaction(transactionMessage);
				return mtr;
			}
			double senderBalance = u.getAccount_balance();
			if (senderBalance < amount) {
				transactionMessage.setTransaction_status(false);
				transactionMessage
						.setTrasaction_message("Transaction Rejected: Insufficient Funds. Your current balance is  "
								+ senderBalance + ". The requested transfer amount is " + amount
								+ ". Please ensure sufficient funds are available and try again.");
				mtr.setTransaction(transactionMessage);
				return mtr;
			}
			User toAccount = this.findByAccountNumber(toAccountNumber);
			if (toAccount == null) {
				transactionMessage.setTransaction_status(false);
				transactionMessage.setTrasaction_message(
						"Money Transfer Failed: Unable to identify the recipient's account. Please double-check the account number and try again.");
				mtr.setTransaction(transactionMessage);
				return mtr;
			}
			double newBal = u.getAccount_balance() - amount;
			transactionIdForMoneyTransfer = this.trasactionIdCreation.createTransactionId();
			this.isTransaactionCalled = true;
			this.transferedTo = toAccountNumber;
			this.transferdFrom = fromAccountNumber;
			this.transactionMessage = this.withdraw(amount, fromAccountNumber);
			this.transactionMessage = this.deposit(amount, toAccountNumber);

			transactionMessage.setTransaction_status(true);
			transactionMessage.setTrasaction_id("S/R" + this.transactionIdForMoneyTransfer);

			String date = dateTimeCreation.dateAndTimeCreator("Date");
			String time = dateTimeCreation.dateAndTimeCreator("Time");
			transactionMessage.setDate(date);
			transactionMessage.setTime(time);
			mtr.setReceiver(toAccountNumber);
			transactionMessage.setTrasaction_message("Money Transfer Successful: ₹" + amount
					+ " transferred to account " + toAccountNumber + ". Your current balance is ₹" + newBal + ".");
			return mtr;

		} catch (Exception e) {
			e.printStackTrace();
			transactionMessage.setTransaction_status(false);
			transactionMessage.setTrasaction_message(
					"Transaction Error: An error occurred during the money transfer. Please try again later or contact support for assistance.");
			return mtr;
		}

	}

	public MiniStatement miniStatement(String account_number) {
		try {
			User u = this.findByAccountNumber(account_number);
			this.miniStatement.setMinistatment_msg("Can't find user Login Again");
			this.miniStatement.setMinistatment_status(false);
			if (u == null) {
				return miniStatement;
			}
			List<Transaction> t = this.trdao.miniStatment(account_number);
			if (!t.isEmpty()) {
				this.miniStatement.setTransaction(t);
				this.miniStatement.setMinistatment_status(true);
				this.miniStatement.setMinistatment_msg("Mini Statement Of Account Number:" + account_number);
				return miniStatement;

			}
			this.miniStatement.setTransaction(t);
			this.miniStatement.setMinistatment_status(true);
			this.miniStatement.setMinistatment_msg("Mini Statement of User Don't Have any record:" + account_number);
			return miniStatement;

		} catch (Exception e) {
			e.printStackTrace();
			this.miniStatement.setMinistatment_msg("Error occured While getting Mini Statement");
			this.miniStatement.setMinistatment_status(false);
			return miniStatement;
		}

	}

	public List<String> getAllTransactionNotification(String account_number) {

		try {
			List<Transaction> transactions = this.trdao.findTransactionsByAccountNumberSortedByTime(account_number);
			if (transactions.isEmpty()) {
				return null;
			}
			 
			User u=this.findByAccountNumber(account_number);
			String account_holder=u.getUserDetails().getFirst_name()+" "+u.getUserDetails().getLast_name();
			return this.transactionsToNotification(transactions,account_holder);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> transactionsToNotification(List<Transaction> transactions,String account_holder) {
		List<String> notifications = new ArrayList<String>();

		for (Transaction transaction : transactions) {
			String notification = generateNotification(transaction,account_holder);
			notifications.add(notification);
		}

		return notifications;
	}

	private String generateNotification(Transaction transaction,String account_holder) {
		String notification;

	
		switch (transaction.getTransaction_type()) {
		case "Deposit":
			notification = String.format(
					"A deposit of %.2f has been successfully credited to your account (%s) on %s. Your new balance is %.2f. Transaction ID: %s.",
					transaction.getAmount(), transaction.getAccount_number(), transaction.getTime(),
				 transaction.getCurr_account_balance(), transaction.getTransaction_id());
			break;
		case "Withdraw":
			notification = String.format(
					"A withdrawal of %.2f has been successfully processed from your account (%s) on %s . Your new balance is %.2f. Transaction ID: %s.",
					transaction.getAmount(), transaction.getAccount_number(), transaction.getTime(),
					 transaction.getCurr_account_balance(), transaction.getTransaction_id());
			break;
		case "Pin Change":
			notification = String.format(
					"Your ATM PIN has been successfully changed account number:(%s) on %s .If you did not make this change, please contact customer support immediately. Transaction ID: %s.",
					transaction.getAccount_number(), transaction.getTime(), transaction.getTransaction_id());
			break;
		case "Money Transfer":
			notification = String.format(
					"A sum of %.2f has been successfully transferred from your account (%s) to another account (%s) on %s . Your new balance is %.2f. Transaction ID: %s.",
					transaction.getAmount(), transaction.getAccount_number(), transaction.getReceiver(),
					transaction.getTime(), transaction.getCurr_account_balance(),
					transaction.getTransaction_id());
			break;
		case "Money Received":
			notification = String.format(
					"You have received a transfer of %.2f in your account (%s) from another account (%s) on %s . Your new balance is %.2f. Transaction ID: %s.",
					transaction.getAmount(), transaction.getReceiver(), transaction.getAccount_number(),
					transaction.getTime(), transaction.getTime(), transaction.getCurr_account_balance(),
					transaction.getTransaction_id());
			break;
		case "Balance Enquiry":
			notification = String.format(
					"Dear %s,This is to confirm that you have successfully checked the balance of your account (%s) on %s . Your current balance is %.2f.Thank you for using our services. If you have any further inquiries or require assistance, feel free to contact us.Best regards, Transaction ID: %s",
					account_holder, transaction.getAccount_number(), transaction.getTime(),
					 transaction.getCurr_account_balance(), "ATM Service Vishal",transaction.getTransaction_id());
			break;
		default:
			notification = "Unsupported transaction type: " + transaction.getTransaction_type();
			break;
		}

		return notification;
	}

}

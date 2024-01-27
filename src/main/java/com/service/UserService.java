package com.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classtoreturn.LoginMessage;
import com.dao.UserDao;
import com.entity.AccountData;
import com.entity.AccountInfo;
import com.entity.User;
import com.entity.UserDetails;
import com.helper.AccountCreationHelper;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private AccountInfo accountInfo;
	@Autowired
	private AccountCreationHelper accountCreationHelper;
	@Autowired
	private LoginMessage loginMessage;

	public AccountInfo register(UserDetails userDetails) {
		try {

			accountInfo.setMsg("User Email Already Registered Please Do with another Email");
			accountInfo.setAccount_number("0");
			accountInfo.setAccount_password(0);
			accountInfo.setAccount_creation_status(false);
			if (userDetails.getEmail() == null) {
				accountInfo.setMsg("Email must not be null");
				return accountInfo;
			}
			User u = this.findByEmail(userDetails.getEmail());

			if (userDetails.getAge() <= 0) {
				accountInfo.setMsg("Age Must be greater than '0'");
				return accountInfo;
			}
			if (u != null) {
				System.out.println("User Already  present So we can create Account!Change Email");
				return accountInfo;
			}

			AccountData acd = accountCreationHelper.createAccountData(userDetails.getGender());
			boolean b = this.save(userDetails, acd.getAccount_password(), acd.getAccount_number());
			System.out.println("Registered user Save is DB status:" + b);
			if (b) {
				accountInfo.setMsg("Registration Done Successfully");
				accountInfo.setAccount_number(acd.getAccount_number());
				accountInfo.setAccount_password(acd.getAccount_password());
				accountInfo.setAccount_creation_status(true);
			}

			return accountInfo;
		} catch (Exception e) {
			e.printStackTrace();
			accountInfo.setMsg("Some Error Occured During Account Creation");
			accountInfo.setAccount_number("0");
			accountInfo.setAccount_password(0);
			accountInfo.setAccount_creation_status(false);
			return accountInfo;

		}
	}

	public User findByEmail(String email) {
		try {
			User u = this.userDao.findByEmail(email);
			if (u == null) {
				return null;
			}
			return u;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public LoginMessage findByAccountNumber(AccountData accountData) {
		try {
			loginMessage.setLogin_msg("You Entered Wrong Account Number and Password");
			loginMessage.setLogin_status(false);
			if (!checkPassLength(accountData.getAccount_password())) {
				System.out.println("Password length is not 4 so it is false");
				loginMessage.setLogin_msg("Wrong Password");
				return loginMessage;
			}
			System.out.println("Account Number:"+accountData.getAccount_number());
			if (!checkAccountNumberLength(accountData.getAccount_number())) {

				System.out.println("Account Number length is less than 12 so it is false");

				return loginMessage;
			}
			System.out.println("Account Number from user:" + accountData.getAccount_number());
			System.out.println("Account Password from user:" + accountData.getAccount_password());
			if (accountData.getAccount_number() == null || accountData.getAccount_password() == 0) {
				loginMessage.setLogin_msg("Don't provide null values for Account Number or Password");
				return loginMessage;
			}
			Optional<User> u = this.userDao.findById(accountData.getAccount_number());
			if (u.isPresent()) {
				if (u.get().getAccount_password() == accountData.getAccount_password()) {
					loginMessage.setLogin_msg("Login Successfully");
					loginMessage.setLogin_status(true);
					return loginMessage;
				}
				loginMessage.setLogin_msg("Wrong Password");
				return loginMessage;

			}
			return loginMessage;

		} catch (Exception e) {
			e.printStackTrace();
			loginMessage.setLogin_msg("Somthing Error Ocured Plases try again");
			loginMessage.setLogin_status(false);
			return loginMessage;
		}
	}

	public boolean checkPassLength(int num) {
		String str = Integer.toString(num);
		if (str.length() != 4) {
			return false;
		}
		return true;
	}

	public boolean checkAccountNumberLength(String acNum) {
		if (acNum.length()!= 12) {
			return false;
		}
		return true;
	}

	public boolean save(UserDetails u, int pass, String acc) {
		try {
			User user = new User();
			user.setUserDetails(u);
			user.setAccount_number(acc);
			user.setAccount_password(pass);
			this.userDao.save(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

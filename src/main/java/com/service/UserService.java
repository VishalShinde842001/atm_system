package com.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public AccountInfo register(UserDetails userDetails) {
		try {
			User u = this.findByEmail(userDetails.getEmail());
			accountInfo.setMsg("User Email Already Registered Please Do with another Email");
			accountInfo.setAccount_number("0");
			accountInfo.setAccount_password(0);
			accountInfo.setAccount_creation_status(false);
			if (u != null) {
				return accountInfo;
			}
			AccountData acd = accountCreationHelper.createAccountData(userDetails.getAge(), userDetails.getGender());
			boolean b = this.save(userDetails, acd.getAccount_password(), acd.getAccount_number());

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

	public User findByAccountNumber(AccountData accountData) {
		try {
			Optional<User> u = this.userDao.findById(accountData.getAccount_number());
			if (u.isPresent()) {
				if (u.get().getAccount_password() == accountData.getAccount_password()) {
					return u.get();
				}

			}
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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

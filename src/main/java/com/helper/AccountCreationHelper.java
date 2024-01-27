package com.helper;

import org.springframework.stereotype.Component;

import com.entity.AccountData;

@Component
public class AccountCreationHelper {
	//3+4+4+1
	public AccountData createAccountData(String gender) {
		String accountNumberPrefix=generateAccountNumberPrefix();
		String accountNumber=accountNumberPrefix+generate4DigitsNumber()+generateRandomString()+gender.charAt(0);
		int accountPassword=generate4DigitsNumber();
		return new AccountData(accountNumber,accountPassword);
	}
	public String generateAccountNumberPrefix() {
	    double randomValue = Math.random();
	    int index;
	    if (randomValue < 0.20) {
	        index = 0; 
	    } else if (randomValue < 0.40) {
	        index = 1; 
	    } else if (randomValue < 0.60) {
	        index = 2; 
	    }else if (randomValue < 0.80) {
	        index = 3; 
	    }
	    else {
	        index = 4; 
	    }
	    return new String[]{"ATM", "BAN", "ACC", "NEW","NAC"}[index];
	}

	public int generate4DigitsNumber() {
		return (int)((Math.random()*9000)+1000);
	}
	private String generateRandomString() {
	    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    StringBuilder randomString = new StringBuilder();

	    for (int i = 0; i < 4; i++) {
	        int randomIndex = (int) (Math.random() * characters.length());
	        randomString.append(characters.charAt(randomIndex));
	    }

	    return randomString.toString();
	}
	

	
}

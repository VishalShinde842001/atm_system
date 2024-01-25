package com.helper;

import org.springframework.stereotype.Component;

import com.entity.AccountData;

@Component
public class AccountCreationHelper {
	
	public AccountData createAccountData(int age,String gender) {
		String accountNumberPrefix=generateAccountNumberPrefix();
		String accountNumber=accountNumberPrefix+generate4DigitsNumber()+generateRandomString()+age+gender.charAt(0);
		int accountPassword=generate4DigitsNumber();
		return new AccountData(accountNumber,accountPassword);
	}
	public String generateAccountNumberPrefix() {
	    double randomValue = Math.random();
	    int index;
	    if (randomValue < 0.25) {
	        index = 0; 
	    } else if (randomValue < 0.5) {
	        index = 1; 
	    } else if (randomValue < 0.75) {
	        index = 2; 
	    } else {
	        index = 3; 
	    }
	    return new String[]{"ATM", "BANK", "ACC", "NEW"}[index];
	}

	public int generate4DigitsNumber() {
		return (int)((Math.random()*9000)+1000);
	}
	private String generateRandomString() {
	    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    StringBuilder randomString = new StringBuilder();

	    for (int i = 0; i < 3; i++) {
	        int randomIndex = (int) (Math.random() * characters.length());
	        randomString.append(characters.charAt(randomIndex));
	    }

	    return randomString.toString();
	}
	

	
}

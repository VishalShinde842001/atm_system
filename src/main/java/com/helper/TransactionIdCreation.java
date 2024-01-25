package com.helper;

import org.springframework.stereotype.Component;


@Component
public class TransactionIdCreation {
	public String createTransactionId() {
		String accountNumberPrefix=generateAccountNumberPrefix();
		String transaction_id=accountNumberPrefix+generate4DigitsNumber()+generateRandomString();
		
		return transaction_id;
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
	    return new String[]{"TRAN", "TRDN", "DONE", "TROK"}[index];
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

package com.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {
	@Id
	private String transaction_id;
	private String account_number;
	private String sender;
	private String receiver;
	private String transaction_type;
	private double amount;
	private double prev_account_balance;
	private double curr_account_balance;
	private String time;

}

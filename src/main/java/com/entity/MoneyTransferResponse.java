package com.entity;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class MoneyTransferResponse {

	private String date;
	private String time;
	private boolean money_transfer_status;
	private String money_transfer_message;
	private String transaction_id;
	
	
}

package com.entity;

import org.springframework.stereotype.Component;

import com.classtoreturn.TransactionMessage;

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

	private TransactionMessage transaction;
	private String receiver;
	
	
}

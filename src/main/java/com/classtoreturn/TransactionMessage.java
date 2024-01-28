package com.classtoreturn;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//This entity is simply created to return status of transaction
//This is will used to return type for the methods like deposit and withdraw
public class TransactionMessage {
	private String date;
	private String time;
	private String trasaction_message;
	private boolean transaction_status;
	private String trasaction_id;

}

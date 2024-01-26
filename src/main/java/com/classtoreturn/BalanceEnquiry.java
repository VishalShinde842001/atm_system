package com.classtoreturn;

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
public class BalanceEnquiry {
	private String date;
	private String time;
	private String location;
	private String transaction_id;
	private String enquiry_message;
	private String account_holder_first_name;
	private String account_holder_last_name;
	private String account_number;
	private double account_balance;
	private boolean balance_enquiry_status;
	
  

}

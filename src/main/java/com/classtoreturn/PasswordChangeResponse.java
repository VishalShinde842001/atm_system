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
public class PasswordChangeResponse {
	private String date;
	private String time;
	private String transaction_id;
	private String password_change_message;
	private boolean password_change_status;

}

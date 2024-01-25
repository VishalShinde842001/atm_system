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
public class AccountInfo {

	private String account_number;
	private int account_password;
	private String msg;
	private boolean account_creation_status;
}

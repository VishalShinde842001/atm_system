package com.entity;

import java.util.List;

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
public class MiniStatement {

	private List<Transaction> transaction;
	private boolean ministatment_status;
	private String ministatment_msg;
}

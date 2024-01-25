package com.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
public class User {
	  @Embedded
	    @AttributeOverrides({
	        @AttributeOverride(name = "email", column = @Column(unique = true))
	    })
	    private UserDetails userDetails;
	
	@Id
	private String account_number;
	private int account_password;
	private double account_balance;
	

}

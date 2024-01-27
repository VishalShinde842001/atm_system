package com.entity;

import jakarta.persistence.Embeddable;
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
@Embeddable
public class UserDetails {
	private String first_name;
	private String last_name;
	private String gender;
	private int age;
	private String email;
	 public void setEmail(String email) {
	        if (email != null) {
	            this.email = email.toLowerCase();
	        } else {
	            this.email = null;
	        }
	    }

}

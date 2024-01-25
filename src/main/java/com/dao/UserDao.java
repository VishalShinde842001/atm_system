package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entity.User;

public interface UserDao extends JpaRepository<User, String>{
	
	@Query("SELECT u FROM User u WHERE u.userDetails.email = :email")
	User findByEmail(@Param("email") String email);

	
	// User findByUserDetailsEmail(String email);

}

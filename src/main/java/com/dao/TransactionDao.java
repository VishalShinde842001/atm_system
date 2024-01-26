package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entity.Transaction;

public interface TransactionDao extends JpaRepository<Transaction, String>{

	 @Query(value="SELECT t FROM Transaction t WHERE t.account_number = :accountNumber ORDER BY t.time DESC limit 10")
	    List<Transaction> miniStatment(@Param("accountNumber") String  accountNumber);
}

package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Transaction;

public interface TransactionDao extends JpaRepository<Transaction, String>{

}

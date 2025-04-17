package dev.abreu.bankapp.repository;

import org.springframework.data.repository.CrudRepository;

import dev.abreu.bankapp.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}

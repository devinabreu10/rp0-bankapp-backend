package dev.abreu.bankapp.repository;

import org.springframework.data.repository.CrudRepository;

import dev.abreu.bankapp.model.Credit;

public interface CreditRepository extends CrudRepository<Credit, Long> {

}

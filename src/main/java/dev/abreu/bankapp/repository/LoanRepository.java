package dev.abreu.bankapp.repository;

import org.springframework.data.repository.CrudRepository;

import dev.abreu.bankapp.model.Loan;

public interface LoanRepository extends CrudRepository<Loan, Long> {

}

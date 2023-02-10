package dev.abreu.bankapp.repository;

import org.springframework.data.repository.CrudRepository;

import dev.abreu.bankapp.model.Account;

public interface AccountRepository extends CrudRepository<Account, Long>{

}

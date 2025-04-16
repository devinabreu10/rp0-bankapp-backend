package dev.abreu.bankapp.repository;

import org.springframework.data.repository.CrudRepository;

import dev.abreu.bankapp.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {

}

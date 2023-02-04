package dev.abreu.bankapp.repository;

import org.springframework.data.repository.CrudRepository;

import dev.abreu.bankapp.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

}

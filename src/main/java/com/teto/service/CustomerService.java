package com.teto.service;

import com.teto.domain.customer.Customer;
import com.teto.repos.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer save(Customer c) {
        return customerRepository.save(c);
    }

    public Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    public boolean deleteById(Long id) {
        customerRepository.deleteById(id);
        return true;
    }
}

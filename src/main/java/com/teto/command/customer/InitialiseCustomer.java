package com.teto.command.customer;

import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.customer.Customer;

import java.util.Optional;

public class InitialiseCustomer extends AbstractCommand<Customer> {
    @Override
    public Optional<Customer> apply(Context ctx) {
        return Optional.empty();
    }
}

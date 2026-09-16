package com.teto.service;

import com.teto.repos.TargetRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class Database {
    @Autowired
    private CustomerService customers;
    @Autowired
    private KnownServicesService knownServices;
    @Autowired
    private TargetService targets;
}

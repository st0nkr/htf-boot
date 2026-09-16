package com.teto.service;

import com.teto.domain.customer.Customer;
import com.teto.domain.target.Target;
import com.teto.repos.CustomerRepository;
import com.teto.repos.TargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TargetService {
    @Autowired
    private TargetRepository repos;

    public Target save(Target c) {
        return repos.save(c);
    }

    public Iterable<Target> findAll() {
        return repos.findAll();
    }

    public boolean deleteById(Long id) {
        repos.deleteById(id);
        return true;
    }

    public List<Target> findAllByIpAddress(String ipAddress) {
        return repos.findAllByIpAddress(ipAddress);
    }

    public List<Target> findAllByUri(String uri) {
        return repos.findAllByUri(uri);
    }

    public Integer countByIpAddress(String ipAddress) {
        return repos.findAllByIpAddress(ipAddress).size();
    }
}

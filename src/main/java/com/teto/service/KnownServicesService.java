package com.teto.service;

import com.teto.domain.service.KnownService;
import com.teto.repos.KnownServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnownServicesService {
    @Autowired
    private KnownServicesRepository repos;

    public List<KnownService> findByPortNumberAndProtocol(Long portNumber, String protocol) {
        return repos.findAllByPortNumberAndProtocol(portNumber, protocol);
    }

    public KnownService save(KnownService c) {
        return repos.save(c);
    }

    public Iterable<KnownService> findAll() {
        return repos.findAll();
    }

    public Long countAll() {
        return repos.count();
    }

    public boolean deleteById(Long id) {
        repos.deleteById(id);
        return true;
    }
}

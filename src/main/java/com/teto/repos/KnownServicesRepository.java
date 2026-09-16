package com.teto.repos;

import com.teto.domain.service.KnownService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnownServicesRepository extends CrudRepository<KnownService, Long> {
    List<KnownService> findAllByPortNumberAndProtocol(Long portNumber, String protocol);
}

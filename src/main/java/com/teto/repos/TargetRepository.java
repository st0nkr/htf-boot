package com.teto.repos;

import com.teto.domain.target.Target;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetRepository extends CrudRepository<Target, Long> {

    List<Target> findAllByIpAddress(String ipAddress);
    List<Target> findAllByUri(String uri);
}

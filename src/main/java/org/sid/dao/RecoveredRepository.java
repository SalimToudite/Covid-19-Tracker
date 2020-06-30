package org.sid.dao;

import org.sid.model.Recovered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecoveredRepository extends MongoRepository<Recovered,String> {
     public Page<Recovered> findByCountryContains(String kw, Pageable pageable );
}

package org.sid.dao;

import org.sid.model.Infected;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InfectedRepository extends MongoRepository<Infected,String> {
   public Page<Infected> findByCountryContains(String kw, Pageable pageable);
}

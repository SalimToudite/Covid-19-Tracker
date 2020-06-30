package org.sid.dao;

import org.sid.model.Deaths;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeathsRepository extends MongoRepository<Deaths,String> {
   public List<Deaths> findByCountryContains(String kw);
}

package com.test.data.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.test.data.domain.Unit;

@Repository
public interface UnitRepository extends GraphRepository<Unit> {
	Unit findByName(String name);
}

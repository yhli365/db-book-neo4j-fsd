package com.test.data.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.test.data.domain.Winning;

@Repository
public interface WinningRepository extends GraphRepository<Winning> {
}

package com.test.data.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.test.data.domain.Show;

/**
 * 节目存储库接口设计.
 *
 */
@Repository
public interface ShowRepository extends GraphRepository<Show> {
}
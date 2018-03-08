package com.test.data.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.test.data.domain.Role;

@Repository
public interface RoleRepository extends GraphRepository<Role> {
	Role findByName(String name);
}

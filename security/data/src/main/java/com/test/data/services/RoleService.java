package com.test.data.services;

import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.test.data.domain.Role;
import com.test.data.model.RoleQo;
import com.test.data.repositories.RoleRepository;

@Service
@Transactional
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PagesService<Role> rolePagesService;

	public Role findById(Long id) {
		return roleRepository.findOne(id);
	}

	public Role create(Role role) {
		return roleRepository.save(role);
	}

	public Role update(Role role) {
		return roleRepository.save(role);
	}

	public void delete(Long id) {
		roleRepository.delete(id);
	}

	public Iterable<Role> findAll() {
		return roleRepository.findAll();
	}

	public Page<Role> findPage(RoleQo roleQo) {
		Pageable pageable = new PageRequest(roleQo.getPage(), roleQo.getSize(), new Sort(Sort.Direction.ASC, "id"));

		Filters filters = new Filters();
		if (!StringUtils.isEmpty(roleQo.getName())) {
			Filter filter = new Filter("name", roleQo.getName() + "*");
			filter.setComparisonOperator(ComparisonOperator.LIKE);
			filters.add(filter);
		}

		return rolePagesService.findAll(Role.class, pageable, filters);
	}
}

package com.test.data.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PagesService<T> {
	@Autowired
	private Session session;

	public Page<T> findAll(Class<T> clazz, Pageable pageable, Filters filters) {
		Collection<T> data = this.session.loadAll(clazz, filters, convert(pageable.getSort()),
				new Pagination(pageable.getPageNumber(), pageable.getPageSize()), 1);
		Collection<T> count = this.session.loadAll(clazz, filters, 1);
		return updatePage(pageable, new ArrayList<T>(data), count.size());
	}

	private Page<T> updatePage(Pageable pageable, List<T> results, int total) {
		return new PageImpl<T>(results, pageable, (long) total);
	}

	private SortOrder convert(Sort sort) {
		SortOrder sortOrder = new SortOrder();
		if (sort != null) {
			Iterator<Sort.Order> var3 = sort.iterator();

			while (var3.hasNext()) {
				Sort.Order order = var3.next();
				if (order.isAscending()) {
					sortOrder.add(new String[] { order.getProperty() });
				} else {
					sortOrder.add(SortOrder.Direction.DESC, new String[] { order.getProperty() });
				}
			}
		}
		return sortOrder;
	}
}

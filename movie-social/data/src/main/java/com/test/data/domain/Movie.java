package com.test.data.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * 电影节点建模.
 *
 */
@NodeEntity
public class Movie {
	@GraphId
	private Long id;// 节点标识
	private String name;// 名字
	@Relationship(type = "RATED", direction = Relationship.INCOMING)
	private Set<Rating> ratings = new HashSet<>();

	public Movie() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addRating(Rating rating) {
		ratings.add(rating);
	}

	public Set<Rating> getRatings() {
		return ratings;
	}
}

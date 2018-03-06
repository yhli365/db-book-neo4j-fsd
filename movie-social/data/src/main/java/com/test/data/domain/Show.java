package com.test.data.domain;

import java.util.Date;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateLong;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 节目节点建模.
 *
 */
@NodeEntity
public class Show {
	@GraphId
	private Long id;// 标识
	private String name;// 名字
	@DateLong
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date create;// 日期
	@Relationship(type = "SITE", direction = Relationship.OUTGOING)
	private Cinema cinema;
	@Relationship(type = "BLOCKBUSTER", direction = Relationship.OUTGOING)
	private Movie movie;

	public Show() {
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

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}
}

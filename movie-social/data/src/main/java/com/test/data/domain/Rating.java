package com.test.data.domain;

import java.util.Date;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * 评分关系建模.
 *
 */
@RelationshipEntity(type = "RATED")
public class Rating {
	@GraphId
	private Long id;// 标识
	private int stars;// 星级
	private String comment;// 评论
	@DateLong
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date create;// 日期

	@StartNode // 开始节点
	@JsonBackReference // 防止数据的递归调用
	private Person person;
	@EndNode // 结束节点
	@JsonBackReference
	private Movie movie;

	public Rating() {
	}

	public Rating(Person person, Movie movie, int stars, String comment) {
		this.person = person;
		this.movie = movie;
		this.stars = stars;
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Movie getMovie() {
		return movie;
	}

	public int getStars() {
		return stars;
	}

	public String getComment() {
		return comment;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}
}

package com.test.data.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateLong;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 观众节点建模.
 *
 */
@NodeEntity
public class Person {
	@GraphId
	private Long id;// 节点标识
	private String name;// 观众名字
	private int sex;// 性别
	@DateLong
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date create;// 创建时间

	@Relationship(type = "FRIEND_OF", direction = Relationship.OUTGOING)
	@JsonIgnore // 防止数据的递归调用
	private Set<Person> friends = new HashSet<>();

	@Relationship(type = "RATED") // 默认为发出方向OUTGOING
	private Set<Rating> ratings = new HashSet<>();

	@Relationship(type = "VISITED", direction = Relationship.OUTGOING)
	private Set<Show> visiters = new HashSet<>();

	public void beFriend(Person person) {
		friends.add(person);
	}

	public void addVistiter(Show show) {
		visiters.add(show);
	}

	public Rating rate(Movie movie, int stars, String comment) {
		Rating rating = new Rating(this, movie, stars, comment);
		ratings.add(rating);
		return rating;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public Set<Person> getFriends() {
		return friends;
	}

	public void setFriends(Set<Person> friends) {
		this.friends = friends;
	}

	public Set<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}

	public Set<Show> getVisiters() {
		return visiters;
	}

	public void setVisiters(Set<Show> visiters) {
		this.visiters = visiters;
	}

}

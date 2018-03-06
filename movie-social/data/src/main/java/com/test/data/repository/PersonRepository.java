package com.test.data.repository;

import java.util.Date;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.test.data.domain.Person;

/**
 * 观众存储库接口设计.
 * 
 * <p>
 * 电影推荐查询算法设计，按推荐的目标不同，可以分为两种，分别为推荐电影给观众的查询算法设计（如
 * findUsersByNotVisiterMovieNamePage）和推荐电影给朋友的查询算法设计（如
 * findFriendsNotVisiterMoviePage）。
 * </p>
 *
 */
@Repository
public interface PersonRepository extends GraphRepository<Person> {
	@Query("MATCH (n:Person) WHERE ID(n) <> {id} RETURN n;")
	Iterable<Person> findByIdNot(@Param("id") Long id);

	// 找出所有朋友包括路径
	@Query("MATCH shortestPath((n:Person)-[r:FRIEND_OF*]->(m:Person)) " + //
			"WHERE ID(n)={id} RETURN m, length(r) as path ORDER BY m.name") //
	Set<Person> findFriendsById(@Param("id") Long id);

	// 看过指定电影的所有朋友
	@Query("MATCH (n:Person)-[r:FRIEND_OF*]-(m:Person) " + //
			"WHERE id(n)={id} WITH  m MATCH (m)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) " + //
			"RETURN  distinct m ORDER BY m.name") //
	Set<Person> findFriendsVisiterMovie(@Param("id") Long id, @Param("name") String name);

	// 未看过指定电影的朋友
	@Query("MATCH (n:Person)-[:FRIEND_OF*1..3]->(m:Person) " + //
			"WHERE id(n)={id} AND NOT (m)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) " + //
			"AND id(m) <> {id} RETURN  m ORDER BY m.name") //
	Set<Person> findFriendsNotVisiterMovie(@Param("id") Long id, @Param("name") String name);

	/*
	 * 推荐电影给朋友的查询算法设计：在模式匹配中使用"[:FRIEND_OF*1..3]"限制了只找朋友关系中路径深度为1~3的朋友；
	 * 而在过滤条件中增加了一个条件"id(m) <> {id}"，即避免在观众的朋友中返回自己。
	 */
	@Query("MATCH (n:Person)-[:FRIEND_OF*1..3]->(m:Person) " + //
			"WHERE id(n)={id} AND NOT (m)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) " + //
			"AND id(m) <> {id} RETURN  m ORDER BY m.name skip {skip} limit {limit}") //
	Set<Person> findFriendsNotVisiterMoviePage(@Param("id") Long id, @Param("name") String name,
			@Param("skip") int skip, @Param("limit") int limit);

	// 看过指定电影的用户
	@Query("MATCH (o:Person) WHERE (o)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) RETURN o")
	Set<Person> findUsersByVisiterMovieName(@Param("name") String name);

	// 没有看过指定电影的用户
	@Query("MATCH (o:Person) WHERE NOT (o)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) RETURN o")
	Set<Person> findUsersByNotVisiterMovieName(@Param("name") String name);

	@Query("MATCH (o:Person) WHERE NOT (o)-[:VISITED]->()-[:BLOCKBUSTER]->({name:{name}}) RETURN o SKIP {skip} LIMIT {limit}")
	Set<Person> findUsersByNotVisiterMovieNamePage(@Param("name") String name, @Param("skip") int skip,
			@Param("limit") int limit);

	Person findByName(String name);

	Iterable<Person> findByNameLike(String name);

	Iterable<Person> findByCreateLessThan(Date create);

}
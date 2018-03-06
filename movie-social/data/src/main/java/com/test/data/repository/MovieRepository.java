package com.test.data.repository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.test.data.domain.Movie;

/**
 * 电影存储库接口设计.
 *
 */
@Repository
public interface MovieRepository extends GraphRepository<Movie> {
	// 电影评分排名
	/*
	 * 使用了两个链接关键字WITH实现了查询的管道功能。<br/>
	 * <p>#1-第一个管道主要实现了将评分关系中的属性stars转换为一个集合对象ratings。</p>
	 * <p>#2-第二个管道通过累计函数REDUCE()取出集合ratings中的每条数值进行累加，然后将累加的和除以集合ratings的总数，计算出一部电影在所有评分中的平均值。</p>
	 */
	@Query("MATCH (:Person)-[r:RATED]->(m:Movie) " + //
			"WITH m, COLLECT(r.stars) AS ratings " + // #1
			"WITH m, ratings, REDUCE(s = 0, i IN ratings | s + i)*1.0 / SIZE(ratings) AS stars " + // #2
			"RETURN ID(m) AS id, m.name AS name, stars, SIZE(ratings) AS num " + //
			"ORDER BY stars DESC, num DESC SKIP {skip} LIMIT {limit}") //
	Set<Map<String, Object>> findRatingMovie(@Param("skip") int skip, @Param("limit") int limit);

	// 电影评分总数
	@Query("MATCH (:Person)-[r:RATED]->(m:Movie) " + //
			"WITH m, COLLECT(r.stars) AS ratings " + //
			"RETURN COUNT(m) AS count ") //
	int findRatingMovieCount();
}

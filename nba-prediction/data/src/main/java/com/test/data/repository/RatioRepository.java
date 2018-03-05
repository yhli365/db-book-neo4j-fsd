package com.test.data.repository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.test.data.domain.Team;

/**
 * 预测算法存储库接口.
 *
 */
@Repository
public interface RatioRepository extends GraphRepository<Team> {
	Team findByName(String name);

	// 年度NBA季后赛
	@Query("MATCH (t:Team)-[r:WIN]->(p:Playoff) WHERE p.year = {year} " + //
			"RETURN t.name AS name, t.code AS code, p.year AS year, p.round AS round, r.win AS win") //
	Set<Map<String, Object>> findHistory(@Param("year") String year);

	// 一个球队的历史季后赛
	@Query("MATCH (t:Team {name: {name}})-[w:WIN]->(p:Playoff)<-[l:WIN]-() " + //
			"RETURN ID(t) AS id, ID(w) as wid, ID(l) as lid, t.name AS name, t.code AS code, w.win AS win, l.win AS loss,"
			+ //
			"p.year AS year, p.round AS round ORDER BY p.year SKIP {skip} LIMIT {limit}") //
	Set<Map<String, Object>> findHistoryByTeamName(@Param("name") String name, @Param("skip") Integer skip,
			@Param("limit") Integer limit);

	// 一个球队的历史季后赛总数
	@Query("MATCH (t:Team {name: {name}})-[w:WIN]->(p:Playoff)<-[l:WIN]-() " + //
			"RETURN COUNT(t) AS count") //
	Integer findHistoryByTeamNameCount(@Param("name") String nam);

	// ------------------------------------------胜负比率排名算法
	/**
	 * 胜负比率排名算法
	 * 
	 * <p>
	 * 胜负比率是指在比赛历史中，一支球队比赛获胜的次数除以获胜与失败之和的比率，这个比率越大，说明球队获胜的机会越多。所以，使用胜负比率进行排名，可以为胜负预测提供可靠的参考价值。
	 * </p>
	 * 
	 * @param skip
	 * @param limit
	 * @return
	 */
	@Query("MATCH (t:Team)-[w:WIN]->(:Playoff)<-[l:WIN]-() " + //
			"RETURN t.name AS team, SUM(w.win) AS wins, SUM(l.win) AS losses," + //
			"(SUM(w.win)*1.0 / (SUM(w.win)+ SUM(l.win))) AS percentage " + //
			"ORDER BY SUM(w.win) DESC SKIP {skip} LIMIT {limit}") //
	Set<Map<String, Object>> findPercentage(@Param("skip") Integer skip, @Param("limit") Integer limit);

	// 胜率总数
	@Query("MATCH (t:Team)-[w:WIN]->(:Playoff)<-[l:WIN]-() WITH DISTINCT t AS p " + //
			"RETURN  COUNT(p) AS count") //
	Integer findPercentageCount();

	// ------------------------------------------输赢预测算法
	// 两队以前见过
	/**
	 * 如果两支球队历史上有过交战，那么可以总结它们历次交战的场次及其输赢的情况，通过这些情况来推测哪支球队更有胜出的可能。
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	@Query("MATCH (t1:Team {name: {t1}})-[r1:WIN]->(p:Playoff)<-[r2:WIN]-(t2:Team {name:{t2}}) " + //
			"RETURN t1.name as t1, r1.win as r1, p.year as year, p.round as round, r2.win as r2, t2.name as t2") //
	Set<Map<String, Object>> findHaveMet(@Param("t1") String t1, @Param("t2") String t2);

	// 计算输赢
	/**
	 * 可以直接用来处理有交战记录的两支球队的输赢计算。
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	@Query("MATCH (t1:Team {name: {t1}})-[r1:WIN]->(p:Playoff)<-[r2:WIN]-(t2:Team {name:{t2}}) " + //
			"RETURN p.year AS year, r1.win AS win, r2.win AS loss " + //
			"ORDER BY p.year DESC") //
	Set<Map<String, Object>> findWinAndLoss(@Param("t1") String t1, @Param("t2") String t2);

	// 两队素昧平生
	/**
	 * 用来处理从未有交战记录的两支球队中关系最接近的比赛情况，并将这些数据列举出来。
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	@Query("MATCH (t1:Team {name:{t1}}),(t2:Team {name:{t2}}),\n" + //
			"p = AllshortestPaths((t1)-[*..14]-(t2))\n" + //
			"RETURN p") //
	Set<Map<String, Object>> findNeverMet(@Param("t1") String t1, @Param("t2") String t2);

	// 赢场比较
	@Query("MATCH (t1:Team {name:{t1}}),(t2:Team {name:{t2}}),\n" + //
			"p = AllshortestPaths((t1)-[r:WIN*..14]-(t2))\n" + //
			"WITH r,p,extract(r IN relationships(p)| r.win ) AS paths\n" + //
			"RETURN paths") //
	Set<Map<String, Object>> findNeverMetPaths(@Param("t1") String t1, @Param("t2") String t2);

	// 平均净赢
	/**
	 * 用来对正在进行比赛的两支球队，计算出其中一支球队的平均净赢比率。
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	@Query("MATCH p= AllShortestPaths((t1:Team {name: {t1}})-[:WIN*0..14]-(t2:Team {name:{t2}})) " + //
			"WITH extract(r IN relationships(p)| r.win) AS RArray, LENGTH(p)-1 AS s " + //
			"RETURN AVG(REDUCE(x = 0, a IN [i IN range(0,s) WHERE i % 2 = 0 | RArray[i] ] | x + a)) " + //
			"- AVG(REDUCE(x = 0, a IN [i IN range(0,s) WHERE i % 2 <> 0 | RArray[i] ] | x + a)) AS NET_WIN") //
	float findAvgNetWin(@Param("t1") String t1, @Param("t2") String t2);

}

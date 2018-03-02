package com.demo.client;

import java.util.Map;
import java.util.Set;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

/**
 * 使用驱动程序连接Neo4j服务器.
 *
 */
public class ConnectServer {
	public static void main(final String[] args) throws Exception {
		Driver driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "12345678"));
		Session session = driver.session();

		session.run("CREATE (u:User {name:'one', email:'one@com.cn'})");

		StatementResult result = session.run("MATCH (u) WHERE u.name = 'one' RETURN u");
		while (result.hasNext()) {
			Record record = result.next();
			Map<String, Object> map = record.get("u").asMap();
			Set<Map.Entry<String, Object>> set = map.entrySet();
			for (Map.Entry<String, Object> one : set) {
				System.out.print(one.getKey() + "=" + one.getValue() + ";");
			}
			System.out.println();
		}

		session.close();
		driver.close();
	}
}

package com.test.data.test;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.test.data.config.Neo4jConfig;
import com.test.data.domain.East;
import com.test.data.domain.Playoff;
import com.test.data.domain.West;
import com.test.data.repository.EastTeamRepository;
import com.test.data.repository.RatioRepository;
import com.test.data.repository.WestTeamRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Neo4jConfig.class })
public class DataTest {
	private static Logger log = LoggerFactory.getLogger(DataTest.class);

	@Autowired
	private WestTeamRepository westTeamRepository;
	@Autowired
	private EastTeamRepository eastTeamRepository;
	@Autowired
	private RatioRepository ratioRepository;

	@Before
	public void add() {
		Playoff playoff = new Playoff();
		playoff.setYear("2016");
		playoff.setRound("第一回合");

		East east = new East();
		east.setName("东部一队");
		east.setCode("东一");
		east.win(2, playoff);
		eastTeamRepository.save(east);
		Assert.notNull(east.getId(), "East team entity save failed");

		West west = new West();
		west.setName("西部一队");
		west.setCode("西一");
		west.win(4, playoff);
		westTeamRepository.save(west);
		Assert.notNull(west.getId(), "West team entity save failed");
	}

	@Test
	public void get() {
		Set<Map<String, Object>> maps = ratioRepository.findWinAndLoss("东部一队", "西部一队");
		Assert.notEmpty(maps, "Team playoff is not empty");
		log.info("\n==========Year:{} =============", maps.iterator().next().get("year"));
	}
}

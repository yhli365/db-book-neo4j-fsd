package com.test.data.test;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.test.data.config.Neo4jConfig;
import com.test.data.domain.Cinema;
import com.test.data.domain.Movie;
import com.test.data.domain.Person;
import com.test.data.domain.Show;
import com.test.data.model.PersonQo;
import com.test.data.repository.PersonRepository;
import com.test.data.service.PersonService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Neo4jConfig.class })
public class DataTest {
	private static Logger log = LoggerFactory.getLogger(DataTest.class);

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PersonService personService;

	/**
	 * 数据初始化.
	 */
	@Before
	public void addData() {
		Cinema cinema = new Cinema();
		cinema.setName("凤凰影院");
		cinema.setCity("深圳");

		Movie movie = new Movie();
		movie.setName("终结者");

		Show show = new Show();
		show.setName("终结者第一场");
		show.setMovie(movie);
		show.setCinema(cinema);
		show.setCreate(new Date());

		for (int i = 1; i <= 30; i++) {
			Person person = new Person();
			person.setName("观众" + i);
			person.setSex(1);
			person.setCreate(new Date());

			if (i > 1) {
				Person friend = personRepository.findByName("观众" + (i - 1));
				person.beFriend(friend);
			}

			person.addVistiter(show);
			person.rate(show.getMovie(), i % 10, "好看");

			personRepository.save(person);
		}
		Assert.notNull(show.getId(), "data init ok");
	}

	@Test
	public void testAll() {
		// 放在一起避免重复初始化数据.
		getPage();
		gets();
	}

	/**
	 * 观众分页查询.
	 */
	public void getPage() {
		PersonQo personQo = new PersonQo();
		Page<Person> persons = personService.findPage(personQo);
		Assert.notNull(persons, "person page query ok");
		for (Person person : persons.getContent()) {
			log.debug("\n==========Person name={}, create={}", person.getName(), person.getCreate());
		}
	}

	/**
	 * 观众模糊查询.
	 */
	public void gets() {
		Iterable<Person> persons = personRepository.findByNameLike("*众2*");
		Assert.notNull(persons, "person like query ok");
		for (Person person : persons) {
			log.debug("\n<<<gets>>>-----Person name={}, create={}, id={}", person.getName(), person.getCreate(),
					person.getId());
		}
	}
}

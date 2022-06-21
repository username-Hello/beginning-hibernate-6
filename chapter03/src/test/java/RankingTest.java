import chapter03.hibernate.Person;
import chapter03.hibernate.Ranking;
import chapter03.hibernate.Skill;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.IntSummaryStatistics;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class RankingTest {

	private SessionFactory sessionFactory;

	@BeforeClass
	public void setup() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure()
				.build();
		sessionFactory = new MetadataSources(registry)
				.buildMetadata()
				.buildSessionFactory();
	}

	@Test
	public void testRankings() {
		populateRankingData();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			IntSummaryStatistics stats = getStats("J. C. Smell", "Java");
			tx.commit();

			assertEquals(7, (int) stats.getAverage());
			assertEquals(8, stats.getMax());
			assertEquals(6, stats.getMin());
			assertEquals(3, stats.getCount());
		}
	}

	@Test
	public void testUpdating() {
		populateRankingData();
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			Query<Ranking> query = session.createQuery(
					"from Ranking r" +
							" where r.subject.name=:subject" +
							" and r.observer.name=:observer" +
							" and r.skill.name=:skill",
					Ranking.class);
			query.setParameter("subject", "J. C. Smell");
			query.setParameter("observer", "Gene Showrama");
			query.setParameter("skill", "Java");

			Ranking ranking = query.uniqueResult();
			assertNotNull(ranking);

			ranking.setRanking(9);
			tx.commit();

			IntSummaryStatistics stats = getStats("J. C. Smell", "Java");
			assertEquals(8, (int) stats.getAverage());
		}
	}

	@Test
	public void testRemove() {
		populateRankingData();
		try (Session session = sessionFactory.openSession()) {
			Ranking ranking = findRanking(session, "J. C. Smell", "Gene Showrama", "Java");
			Transaction tx = session.beginTransaction();
			assertNotNull(ranking);
			session.delete(ranking);
			tx.commit();
		}
		assertEquals(7.5, getStats("J. C. Smell", "Java").getAverage());
	}

	private Ranking findRanking(Session session, String subject, String observer, String skill) {
		Transaction tx = session.beginTransaction();
		Ranking ranking = session.createQuery(
						"from Ranking r" +
								" where r.subject.name=:subject" +
								" and r.observer.name=:observer" +
								" and r.skill.name=:skill",
						Ranking.class)
				.setParameter("subject", subject)
				.setParameter("observer", observer)
				.setParameter("skill", skill)
				.uniqueResult();
		tx.commit();
		return ranking;
	}

	public IntSummaryStatistics getStats(String subjectName, String skillName) {
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();

			Query<Ranking> query = session.createQuery(
					"from Ranking r " +
							"where r.subject.name=:subjectName " +
							"and r.skill.name=:skillName",
					Ranking.class);
			query.setParameter("subjectName", subjectName);
			query.setParameter("skillName", skillName);

			IntSummaryStatistics stats = query.list()
					.stream()
					.collect(Collectors.summarizingInt(Ranking::getRanking));
			tx.commit();
			return stats;
		}
	}

	private void populateRankingData() {
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			createData(session, "J. C. Smell", "Gene Showrama", "Java", 6);
			createData(session, "J. C. Smell", "Scottball Most", "Java", 7);
			createData(session, "J. C. Smell", "Drew Lombardo", "Java", 8);
		}
	}

	private Person savePerson(String name, Session session) {
		return findPerson(name, session)
				.orElseGet(() -> {
					Person person = new Person(); // person is transient at this point
					person.setName(name);
					session.save(person); //  person become persisted at this point
					return person;
				});
	}

	private Skill saveSkill(String name, Session session) {
		return findSkill(name, session)
				.orElseGet(() -> {
					Skill skill = new Skill();
					skill.setName(name);
					session.save(skill);
					return skill;
				});
	}

	private void createData(Session session,
							String subjectName,
							String observerName,
							String skillName,
							int rank) {
		Person observer = savePerson(observerName, session);
		Person subject = savePerson(subjectName, session);
		Skill skill = saveSkill(skillName, session);

		Ranking ranking = new Ranking();
		ranking.setObserver(observer);
		ranking.setSubject(subject);
		ranking.setSkill(skill);
		ranking.setRanking(rank);
		session.save(ranking);
	}

	private Optional<Person> findPerson(String name, Session session) {
		Query<Person> query = session.createQuery("from Person p where p.name=:name", Person.class);
		query.setParameter("name", name);
		return query.uniqueResultOptional();
	}

	private Optional<Skill> findSkill(String name, Session session) {
		Query<Skill> query = session.createQuery("from Skill s where s.name=:name", Skill.class);
		query.setParameter("name", name);
		return query.uniqueResultOptional();
	}

}

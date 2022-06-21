package chapter02.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class PersistenceTest {

	private SessionFactory factory;

	@BeforeClass
	public void setup() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure("my-hibernate-config.xml")
				.build();

		factory = new MetadataSources((registry))
				.buildMetadata()
				.buildSessionFactory();
	}

	public Message saveMessage(String text) {
		Message message = new Message(text);
		try (Session session = factory.openSession()) {
			Transaction tx = session.beginTransaction();
			session.persist(message);
			tx.commit();
		}
		return message;
	}

	@Test
	public void readMessage() {
		Message savedMessage = saveMessage("Hello world!");
		List<Message> list;
		try (Session session = factory.openSession()) {
			list = session
					.createQuery("from Message", Message.class)
					.list();
		}
		assertEquals(list.size(), 1);
		list.forEach(System.out::println);
		assertEquals(list.get(0), savedMessage);
	}

}

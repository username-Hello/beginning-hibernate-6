package com.bh6.hibernate.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class SessionUtil {

	private static final SessionUtil instance = new SessionUtil();
	private SessionFactory factory;

	public SessionUtil() {
		initialize();
	}

	private void initialize() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure()
				.build();
		factory = new MetadataSources(registry)
				.buildMetadata()
				.buildSessionFactory();

	}

	private static SessionUtil getInstance() {
		return instance;
	}

	public static Session getSession() {
		return getInstance().factory.openSession();
	}
}

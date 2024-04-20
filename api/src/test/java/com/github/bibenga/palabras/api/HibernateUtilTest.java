package com.github.bibenga.palabras.api;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class HibernateUtilTest {
    // private static SessionFactory sessionFactory;
    // private Session session;

    @BeforeAll
    public static void setup() {
        System.err.println("BeforeAll");
        // try {
        //     // Create the SessionFactory from hibernate.cfg.xml
        //     sessionFactory = new Configuration().configure("file:./hibernate-test.cfg.xml")
        //             .buildSessionFactory();
        // } catch (Throwable ex) {
        //     // Make sure you log the exception, as it might be swallowed
        //     System.err.println("Initial SessionFactory creation failed." + ex);
        //     throw new ExceptionInInitializerError(ex);
        // }
    }

    // @AfterAll
    // public static void tearDown() {
    //     if (sessionFactory != null)
    //         sessionFactory.close();
    // }

    // @BeforeEach
    // public void openSession() {
    //     session = sessionFactory.openSession();
    // }

    // @AfterEach
    // public void closeSession() {
    //     if (session != null)
    //         session.close();
    // }
}

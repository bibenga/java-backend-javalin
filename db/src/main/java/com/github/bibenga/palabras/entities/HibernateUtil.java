package com.github.bibenga.palabras.entities;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    // private static final SessionFactory sessionFactory = buildSessionFactory();

    public static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure("file:./hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // public static SessionFactory getSessionFactory() {
    //     return sessionFactory;
    // }

    // public static void runInTransaction(Runnable task) {
    //     runInTransaction(getSessionFactory(), task);
    // }

    public static void runInTransaction(SessionFactory sessionFactory, Runnable task) {
        var session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            task.run();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try {
                    tx.rollback();
                } catch (Exception e2) {
                    throw e2;
                }
                throw e;
            }
        } finally {
            session.close();
        }
    }
}

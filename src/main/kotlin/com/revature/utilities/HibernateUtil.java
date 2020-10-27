package com.revature.utilities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static Session session;
    private static SessionFactory sf;
    private static Configuration cfg;
    private static String currentSchema;

    static {
        cfg = new Configuration().configure("hibernate.cfg.xml");
        cfg.setProperty("hibernate.connection.url", System.getenv("db_url"));
        cfg.setProperty("hibernate.connection.password", System.getenv("postgres_pw"));
        cfg.setProperty("hibernate.connection.username", System.getenv("postgres_username"));
        currentSchema = System.getenv("p1_refactor_schema");
        cfg.setProperty("hibernate.default_schema", currentSchema);
        try {
            sf = cfg.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private HibernateUtil() {
        super();
    }

    public static Session getSession() {
        if (session == null || !session.isOpen()) {
            session = sf.openSession();
        }
        return session;
    }

    public static void closeSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    public static void reconfigureSchema(String schemaName) {
        cfg.setProperty("hibernate.default_schema", schemaName);
        currentSchema = schemaName;
        sf = cfg.buildSessionFactory();
    }

    public static String getCurrentSchema() {
        return currentSchema;
    }

}

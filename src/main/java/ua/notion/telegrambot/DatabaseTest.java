package ua.notion.telegrambot;

import org.hibernate.SessionFactory;
import ua.notion.telegrambot.util.HibernateUtil;

public class DatabaseTest {
  public static void main(String[] args) {
    try {
      SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
      System.out.println("Hibernate connect to Postgres.");

      HibernateUtil.shutdown();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}

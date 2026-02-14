package ua.notion.telegrambot.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.notion.telegrambot.model.Cabinet;
import ua.notion.telegrambot.util.HibernateUtil;

import java.util.List;

public class CabinetRepositoryImpl implements CabinetRepository {

  @Override
  public boolean save(Cabinet cabinet) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.persist(cabinet);
      transaction.commit();
      return true;
    } catch (Exception e) {
      if (transaction != null) transaction.rollback();
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public Cabinet findById(Integer id) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "FROM Cabinet c JOIN FETCH c.floor WHERE c.id = :id";
      Query<Cabinet> query = session.createQuery(hql, Cabinet.class);
      query.setParameter("id", id);
      query.setCacheable(true);

      return query.uniqueResult();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public List<Cabinet> findAllByFloorNumber(int floorNumber) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "FROM Cabinet c JOIN FETCH c.floor f WHERE f.number = :floorNum ORDER BY c.number ASC";
      Query<Cabinet> query = session.createQuery(hql, Cabinet.class);
      query.setParameter("floorNum", floorNumber);
      query.setCacheable(true);

      return query.list();
    } catch (Exception e) {
      e.printStackTrace();
      return List.of();
    }
  }

  @Override
  public Cabinet findByNumber(String number) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "FROM Cabinet c JOIN FETCH c.floor WHERE c.number = :number";
      Query<Cabinet> query = session.createQuery(hql, Cabinet.class);
      query.setParameter("number", number);
      query.setCacheable(true);

      return query.uniqueResult();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
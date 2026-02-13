package ua.notion.telegrambot.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ua.notion.telegrambot.model.Landmark;
import ua.notion.telegrambot.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class LandmarkRepositoryImpl implements LandmarkRepository {

  @Override
  public List<Landmark> findAllByFloorNumber(int floorNumber) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "FROM Landmark l " +
          "JOIN FETCH l.floor f " +
          "LEFT JOIN FETCH l.nearbyCabinets " +
          "WHERE f.number = :floorNum " +
          "ORDER BY l.id ASC";

      Query<Landmark> query = session.createQuery(hql, Landmark.class);
      query.setParameter("floorNum", floorNumber);

      return query.list();
    } catch (Exception e) {
      e.printStackTrace();
      return List.of();
    }
  }

  @Override
  public Optional<Landmark> findByCode(String code) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "FROM Landmark l LEFT JOIN FETCH l.nearbyCabinets WHERE l.code = :code";
      Query<Landmark> query = session.createQuery(hql, Landmark.class);
      query.setParameter("code", code);

      return query.uniqueResultOptional();
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public Optional<Landmark> findById(Integer id) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "FROM Landmark l LEFT JOIN FETCH l.nearbyCabinets WHERE l.id = :id";
      Query<Landmark> query = session.createQuery(hql, Landmark.class);
      query.setParameter("id", id);

      return query.uniqueResultOptional();
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
}
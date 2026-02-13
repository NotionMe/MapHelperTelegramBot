package ua.notion.telegrambot.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.notion.telegrambot.model.Route;
import ua.notion.telegrambot.util.HibernateUtil;
import java.util.List;
import java.util.Optional;

public class RouteRepositoryImpl implements RouteRepository {

  @Override
  public Optional<Route> findByLandmarkAndCabinet(Integer landmarkId, Integer cabinetId) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "FROM Route r " +
          "JOIN FETCH r.landmark l " +
          "JOIN FETCH r.cabinet c " +
          "WHERE l.id = :landmarkId AND c.id = :cabinetId";

      Query<Route> query = session.createQuery(hql, Route.class);
      query.setParameter("landmarkId", landmarkId);
      query.setParameter("cabinetId", cabinetId);

      return query.uniqueResultOptional();
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public List<Route> findAllByCabinetId(Integer cabinetId) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "FROM Route r JOIN FETCH r.landmark l JOIN FETCH r.cabinet c WHERE c.id = :cabinetId";

      Query<Route> query = session.createQuery(hql, Route.class);
      query.setParameter("cabinetId", cabinetId);

      return query.list();
    } catch (Exception e) {
      e.printStackTrace();
      return List.of();
    }
  }

  @Override
  public boolean save(Route route) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.persist(route);
      transaction.commit();
      return true;
    } catch (Exception e) {
      if (transaction != null)
        transaction.rollback();
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public String findGifUrlByFloor(Integer floor) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT r.gifUrl FROM Route r JOIN r.landmark l JOIN l.floor f WHERE f.number = :floorNumber";
      return session.createQuery(hql, String.class)
          .setParameter("floorNumber", floor)
          .uniqueResult();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}

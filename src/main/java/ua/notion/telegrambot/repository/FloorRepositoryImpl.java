package ua.notion.telegrambot.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ua.notion.telegrambot.model.Floor;
import ua.notion.telegrambot.util.HibernateUtil;
import java.util.Optional;

public class FloorRepositoryImpl implements FloorRepository {

  @Override
  public Optional<Floor> findByNumber(int number) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "FROM Floor f LEFT JOIN FETCH f.cabinets WHERE f.number = :number";

      Query<Floor> query = session.createQuery(hql, Floor.class);
      query.setParameter("number", number);

      return query.uniqueResultOptional();
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
}
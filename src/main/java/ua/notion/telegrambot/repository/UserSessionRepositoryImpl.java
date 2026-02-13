package ua.notion.telegrambot.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import ua.notion.telegrambot.model.UserSession;
import ua.notion.telegrambot.util.HibernateUtil;

import java.util.Optional;

public class UserSessionRepositoryImpl implements UserSessionRepository {

  @Override
  public Optional<UserSession> findByUserId(Long userId) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "FROM UserSession s WHERE s.userId = :userId";
      Query<UserSession> query = session.createQuery(hql, UserSession.class);
      query.setParameter("userId", userId);

      return query.uniqueResultOptional();
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public boolean saveOrUpdate(UserSession userSession) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.merge(userSession);
      transaction.commit();
      return true;
    } catch (Exception e) {
      if (transaction != null) transaction.rollback();
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean deleteByUserId(Long userId) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      String hql = "DELETE FROM UserSession s WHERE s.userId = :userId";

      MutationQuery query = session.createMutationQuery(hql);
      query.setParameter("userId", userId);

      int result = query.executeUpdate();
      transaction.commit();
      return result > 0;
    } catch (Exception e) {
      if (transaction != null) transaction.rollback();
      e.printStackTrace();
      return false;
    }
  }
}

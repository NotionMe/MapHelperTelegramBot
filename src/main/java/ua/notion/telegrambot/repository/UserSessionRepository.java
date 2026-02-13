package ua.notion.telegrambot.repository;

import ua.notion.telegrambot.model.UserSession;
import java.util.Optional;

public interface UserSessionRepository {

  Optional<UserSession> findByUserId(Long userId);

  boolean saveOrUpdate(UserSession session);

  boolean deleteByUserId(Long userId);
}

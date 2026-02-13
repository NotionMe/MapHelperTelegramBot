package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.UserSession;
import ua.notion.telegrambot.repository.UserSessionRepository;
import ua.notion.telegrambot.repository.UserSessionRepositoryImpl;
import java.util.Optional;

public class UserSessionServiceImpl implements UserSessionService {

  private final UserSessionRepository sessionRepository = new UserSessionRepositoryImpl();

  @Override
  public UserSession getOrCreateSession(Long userId) {
    if (userId == null) return null;

    Optional<UserSession> existingSession = sessionRepository.findByUserId(userId);

    if (existingSession.isPresent()) {
      return existingSession.get();
    } else {
      UserSession newSession = new UserSession();
      newSession.setUserId(userId);
      sessionRepository.saveOrUpdate(newSession);
      return newSession;
    }
  }

  @Override
  public void updateSession(UserSession session) {
    if (session != null && session.getUserId() != null) {
      sessionRepository.saveOrUpdate(session);
    }
  }

  @Override
  public void clearSession(Long userId) {
    if (userId != null) {
      sessionRepository.deleteByUserId(userId);
    }
  }
}

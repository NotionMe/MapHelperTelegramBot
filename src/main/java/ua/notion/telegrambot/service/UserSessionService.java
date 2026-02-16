package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.UserSession;

public interface UserSessionService {

  UserSession getOrCreateSession(Long userId);

  void updateSession(UserSession session);

  void clearSession(Long userId);
}

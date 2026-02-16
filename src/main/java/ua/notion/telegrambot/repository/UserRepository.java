package ua.notion.telegrambot.repository;

import ua.notion.telegrambot.model.TelegramUser;

public interface UserRepository {
    boolean save(TelegramUser user);
    TelegramUser findById(Long userId);
    boolean existsById(Long userId);
}
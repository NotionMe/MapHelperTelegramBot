package ua.notion.telegrambot.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ua.notion.telegrambot.model.TelegramUser;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, TelegramUser> users = new ConcurrentHashMap<>();

    @Override
    public boolean save(TelegramUser user) {
        if (user != null && user.getUserId() != null) {
            users.put(user.getUserId(), user);
            return true;
        }
        return false;
    }

    @Override
    public TelegramUser findById(Long userId) {
        return users.get(userId);
    }

    @Override
    public boolean existsById(Long userId) {
        return users.containsKey(userId);
    }
}
package ua.notion.telegrambot.repository;

import ua.notion.telegrambot.model.TelegramUser;

/**
 * Interface for user data access operations
 */
public interface UserRepository {
    /**
     * Save or update a Telegram user
     * @param user The user to save
     * @return True if successful, false otherwise
     */
    boolean save(TelegramUser user);

    /**
     * Find a user by their ID
     * @param userId The user ID to search for
     * @return The user if found, null otherwise
     */
    TelegramUser findById(Long userId);

    /**
     * Check if a user exists by their ID
     * @param userId The user ID to check
     * @return True if user exists, false otherwise
     */
    boolean existsById(Long userId);
}
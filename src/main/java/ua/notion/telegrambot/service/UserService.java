package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.MessageResponse;
import ua.notion.telegrambot.model.TelegramUser;

/**
 * Interface for business logic operations related to the Telegram bot
 */
public interface UserService {
    /**
     * Process a new user joining the bot
     * @param user The user who joined
     * @return A welcome message response
     */
    MessageResponse processNewUser(TelegramUser user);

    /**
     * Handle a command from a user
     * @param command The command received
     * @param userId The ID of the user who sent the command
     * @return A response message
     */
    MessageResponse handleCommand(String command, Long userId);

    /**
     * Handle a regular message from a user
     * @param message The message received
     * @param userId The ID of the user who sent the message
     * @return A response message
     */
    MessageResponse handleMessage(String message, Long userId);

    /**
     * Register a user in the system
     * @param user The user to register
     * @return True if registration was successful
     */
    boolean registerUser(TelegramUser user);
}
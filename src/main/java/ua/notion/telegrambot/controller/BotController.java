package ua.notion.telegrambot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.notion.telegrambot.model.MessageResponse;
import ua.notion.telegrambot.model.TelegramUser;
import ua.notion.telegrambot.service.UserService;

/**
 * Controller responsible for handling incoming Telegram updates
 * and delegating to the service layer
 */
public class BotController {
    private static final Logger logger = LoggerFactory.getLogger(BotController.class);

    private final UserService userService;

    public BotController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handle an incoming message from a Telegram user
     * 
     * @param userId       The ID of the user sending the message
     * @param firstName    The first name of the user
     * @param lastName     The last name of the user
     * @param username     The username of the user
     * @param languageCode The language code of the user
     * @param messageText  The text of the message
     * @return A response to send back to the user
     */
    public MessageResponse handleMessage(Long userId, String firstName, String lastName,
            String username, String languageCode, String messageText) {
        logger.info("Received message from user {}: {}", userId, messageText);

        // Create a TelegramUser object from the incoming data
        TelegramUser user = new TelegramUser(userId, firstName, lastName, username, languageCode);

        // Register or update the user in the system
        userService.registerUser(user);

        // Handle the message and get a response
        MessageResponse response = userService.handleMessage(messageText, userId);

        logger.info("Sending response to user {}: {}", userId, response.getText());
        return response;
    }

    /**
     * Handle a new user joining the bot
     * 
     * @param userId       The ID of the new user
     * @param firstName    The first name of the new user
     * @param lastName     The last name of the new user
     * @param username     The username of the new user
     * @param languageCode The language code of the new user
     * @return A welcome response for the new user
     */
    public MessageResponse handleNewUser(Long userId, String firstName, String lastName,
            String username, String languageCode) {
        logger.info("New user joined: {} (ID: {})", username, userId);

        // Create a TelegramUser object from the incoming data
        TelegramUser user = new TelegramUser(userId, firstName, lastName, username, languageCode);

        // Process the new user and get a welcome response
        MessageResponse response = userService.processNewUser(user);

        logger.info("Sending welcome message to new user {}: {}", userId, response.getText());
        return response;
    }
}
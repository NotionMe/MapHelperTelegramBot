package ua.notion.telegrambot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.notion.telegrambot.model.MessageResponse;
import ua.notion.telegrambot.model.TelegramUser;
import ua.notion.telegrambot.service.UserService;

public class BotController {
    private static final Logger logger = LoggerFactory.getLogger(BotController.class);

    private final UserService userService;

    public BotController(UserService userService) {
        this.userService = userService;
    }

    public MessageResponse handleMessage(Long userId, String firstName, String lastName,
            String username, String languageCode, String messageText) {
        logger.info("Received message from user {}: {}", userId, messageText);

        TelegramUser user = new TelegramUser(userId, firstName, lastName, username, languageCode);
        userService.registerUser(user);
        MessageResponse response = userService.handleMessage(messageText, userId);

        logger.info("Sending response to user {}: {}", userId, response.getText());
        return response;
    }

    public MessageResponse handleNewUser(Long userId, String firstName, String lastName,
            String username, String languageCode) {
        logger.info("New user joined: {} (ID: {})", username, userId);

        TelegramUser user = new TelegramUser(userId, firstName, lastName, username, languageCode);
        MessageResponse response = userService.processNewUser(user);

        logger.info("Sending welcome message to new user {}: {}", userId, response.getText());
        return response;
    }

    public MessageResponse getStartResponse(Long userId) {
        logger.info("Getting /start response for user: {}", userId);

        MessageResponse response = userService.getStartResponse(userId);

        logger.info("Sending /start response to user {}: {}", userId, response.getText());
        return response;
    }
}
package ua.notion.telegrambot.controller;

import ua.notion.telegrambot.model.MessageResponse;
import ua.notion.telegrambot.model.TelegramUser;
import ua.notion.telegrambot.service.UserService;

public class BotController {
    private final UserService userService;

    public BotController(UserService userService) {
        this.userService = userService;
    }

    public MessageResponse handleMessage(Long userId, String firstName, String lastName,
            String username, String languageCode, String messageText) {
        
        TelegramUser user = new TelegramUser(userId, firstName, lastName, username, languageCode);
        userService.registerUser(user);
        
        return userService.handleMessage(messageText, userId);
    }

    public MessageResponse getStartResponse(Long userId) {
        return userService.getStartResponse(userId);
    }
}
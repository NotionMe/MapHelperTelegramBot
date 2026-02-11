package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.MessageResponse;
import ua.notion.telegrambot.model.TelegramUser;

public interface UserService {
    MessageResponse processNewUser(TelegramUser user);
    MessageResponse getStartResponse(Long userId);
    MessageResponse handleMessage(String message, Long userId);
    boolean registerUser(TelegramUser user);
}
package com.example.telegrambot;

import org.junit.jupiter.api.Test;

import ua.notion.telegrambot.config.AppConfig;
import ua.notion.telegrambot.model.TelegramUser;
import ua.notion.telegrambot.repository.InMemoryUserRepository;
import ua.notion.telegrambot.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple test to verify the application structure
 */
public class TelegramBotApplicationTest {

    @Test
    public void testAppConfigInitialization() {
        // Test that the configuration initializes properly
        AppConfig config = new AppConfig();
        
        assertNotNull(config.getUserRepository(), "UserRepository should not be null");
        assertNotNull(config.getUserService(), "UserService should not be null");
        assertNotNull(config.getBotController(), "BotController should not be null");
    }

    @Test
    public void testUserRegistration() {
        // Test user registration functionality
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        
        TelegramUser user = new TelegramUser(123L, "John", "Doe", "johndoe", "en");
        
        boolean result = userService.registerUser(user);
        
        assertTrue(result, "User registration should be successful");
        assertTrue(userRepository.existsById(123L), "User should exist in repository after registration");
    }
}
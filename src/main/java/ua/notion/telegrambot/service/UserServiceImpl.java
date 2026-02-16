package ua.notion.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.notion.telegrambot.model.MessageResponse;
import ua.notion.telegrambot.model.TelegramUser;
import ua.notion.telegrambot.repository.UserRepository;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public MessageResponse processNewUser(TelegramUser user) {
        logger.info("Processing new user: {}", user.getUsername());
        boolean isNewUser = registerUser(user);

        String welcomeMessage = isNewUser
                ? String.format("Welcome to our bot, %s! We're glad to have you here.\n\nUse /help to see available commands.",
                        user.getFirstName() != null ? user.getFirstName() : "Friend")
                : "Welcome back! We're happy to see you again.";

        return new MessageResponse(user.getUserId(), welcomeMessage, true);
    }

    @Override
    public MessageResponse getStartResponse(Long userId) {
        return new MessageResponse(userId, 
                "Hello! Welcome to our Telegram bot. Use /help to see available commands.", true);
    }

    @Override
    public MessageResponse handleMessage(String message, Long userId) {
        logger.info("Handling message from user: {}", userId);
        
        String responseText = String.format("Thank you for your message: \"%s\". For help, use /help command.", message);
        return new MessageResponse(userId, responseText, true);
    }

    @Override
    public boolean registerUser(TelegramUser user) {
        if (user == null || user.getUserId() == null) {
            logger.warn("Cannot register user with null ID");
            return false;
        }

        if (userRepository.existsById(user.getUserId())) {
            logger.debug("User already exists: {}", user.getUserId());
            return false;
        }

        boolean saved = userRepository.save(user);
        if (saved) {
            logger.info("Successfully registered new user: {}", user.getUserId());
        } else {
            logger.error("Failed to register user: {}", user.getUserId());
        }

        return saved;
    }
}
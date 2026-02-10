package ua.notion.telegrambot.config;

import ua.notion.telegrambot.controller.BotController;
import ua.notion.telegrambot.repository.InMemoryUserRepository;
import ua.notion.telegrambot.repository.UserRepository;
import ua.notion.telegrambot.service.UserService;
import ua.notion.telegrambot.service.UserServiceImpl;

/**
 * Configuration class to manage dependencies and setup the application
 */
public class AppConfig {
    
    private final UserRepository userRepository;
    private final UserService userService;
    private final BotController botController;
    
    public AppConfig() {
        // Initialize the repository layer
        this.userRepository = new InMemoryUserRepository();
        
        // Initialize the service layer with repository dependency
        this.userService = new UserServiceImpl(userRepository);
        
        // Initialize the controller layer with service dependency
        this.botController = new BotController(userService);
    }
    
    public UserRepository getUserRepository() {
        return userRepository;
    }
    
    public UserService getUserService() {
        return userService;
    }
    
    public BotController getBotController() {
        return botController;
    }
}
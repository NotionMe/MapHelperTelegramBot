package ua.notion.telegrambot.config;

import ua.notion.telegrambot.controller.BotController;
import ua.notion.telegrambot.repository.InMemoryUserRepository;
import ua.notion.telegrambot.repository.UserRepository;
import ua.notion.telegrambot.service.UserService;
import ua.notion.telegrambot.service.UserServiceImpl;

public class AppConfig {

    private final UserRepository userRepository;
    private final UserService userService;
    private final BotController botController;

    public AppConfig() {
        this.userRepository = new InMemoryUserRepository();
        this.userService = new UserServiceImpl(userRepository);
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
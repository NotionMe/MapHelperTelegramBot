package ua.notion.telegrambot;

import io.github.natanimn.telebof.BotClient;
import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.MessageHandler;
import io.github.natanimn.telebof.types.updates.Message;
import ua.notion.telegrambot.config.AppConfig;
import ua.notion.telegrambot.controller.BotController;
import io.github.natanimn.telebof.enums.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class for the Telegram Bot
 * This class initializes the bot and handles incoming messages
 */
public class TelegramBotApplication {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotApplication.class);

    private final BotController botController;

    public TelegramBotApplication() {
        // Initialize the application configuration
        AppConfig config = new AppConfig();
        this.botController = config.getBotController();

        logger.info("Telegram Bot Application initialized successfully");
    }

    /**
     * Main method to start the Telegram bot
     * 
     * @param args Command-line arguments (bot token should be provided as
     *             environment variable)
     */
    public static void main(String[] args) {
        // Get bot token from environment variable
        String botToken = System.getenv("TELEGRAM_BOT_TOKEN");

        if (botToken == null || botToken.isEmpty()) {
            logger.error("TELEGRAM_BOT_TOKEN environment variable is not set!");
            System.err.println("Please set the TELEGRAM_BOT_TOKEN environment variable.");
            System.exit(1);
        }

        logger.info("Starting Telegram Bot...");

        try {
            TelegramBotApplication app = new TelegramBotApplication();

            // Create and configure the bot using Telebof
            BotClient bot = new BotClient(botToken);

            // Register the message handler
            bot.addHandler(app);

            // Start polling for updates
            bot.startPolling();

            logger.info("Telegram Bot is running and listening for messages...");

        } catch (Exception e) {
            logger.error("Error starting the Telegram Bot: ", e);
            System.err.println("Failed to start the bot: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Handle incoming messages from Telegram
     * 
     * @param context The bot context for sending responses
     * @param message The incoming message
     */
    @MessageHandler(type = MessageType.TEXT)
    public void handleMessage(BotContext context, Message message) {
        // Extract user information
        Long userId = message.getFrom().getId();
        String firstName = message.getFrom().getFirstName();
        String lastName = message.getFrom().getLastName();
        String username = message.getFrom().getUsername();
        String languageCode = message.getFrom().getLanguageCode();
        String messageText = message.getText();

        logger.info("Received message from user {}: {}", userId, messageText);

        // Handle the message using the controller
        var response = botController.handleMessage(
                userId,
                firstName,
                lastName,
                username,
                languageCode,
                messageText);

        // Send the response back to the user
        context.sendMessage(message.getChat().getId(), response.getText()).exec();

        logger.info("Response sent to user {}: {}", userId, response.getText());
    }

    /**
     * Handle /start command
     * 
     * @param context The bot context for sending responses
     * @param message The incoming message
     */
    @MessageHandler(commands = "start")
    public void handleStartCommand(BotContext context, Message message) {
        // Extract user information
        Long userId = message.getFrom().getId();
        String firstName = message.getFrom().getFirstName();
        String lastName = message.getFrom().getLastName();
        String username = message.getFrom().getUsername();
        String languageCode = message.getFrom().getLanguageCode();

        logger.info("Received /start command from user {}", userId);

        // Handle the new user using the controller
        var response = botController.handleNewUser(
                userId,
                firstName,
                lastName,
                username,
                languageCode);

        // Send the response back to the user
        context.sendMessage(message.getChat().getId(), response.getText()).exec();

        logger.info("Welcome message sent to user {}: {}", userId, response.getText());
    }
}
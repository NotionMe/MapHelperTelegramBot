package ua.notion.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.natanimn.telebof.BotClient;
import ua.notion.telegrambot.config.AppConfig;
import ua.notion.telegrambot.controller.BotController;
import ua.notion.telegrambot.handler.CommandHandler;
import ua.notion.telegrambot.handler.TelegramCallbackHandler;
import ua.notion.telegrambot.handler.TelegramMessageHandler;

public class TelegramBotApplication {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotApplication.class);

    private final BotController botController;

    private static Dotenv dotenv = null;

    public TelegramBotApplication() {
        AppConfig config = new AppConfig();
        this.botController = config.getBotController();

        logger.info("Telegram Bot Application initialized successfully");
    }

    public static void main(String[] args) {
        String botToken = System.getenv("TELEGRAM_BOT_TOKEN");
        dotenv = Dotenv.configure().load();

        if (botToken == null || botToken.isEmpty()) {
            logger.error("TELEGRAM_BOT_TOKEN environment variable is not set!");
            botToken = dotenv.get("TELEGRAM_BOT_TOKEN");
            if (botToken == null || botToken.isEmpty()) {
                System.err.println("Please set the TELEGRAM_BOT_TOKEN environment variable.");
                System.exit(1);
            }
        }

        logger.info("Starting Telegram Bot...");

        try {
            BotClient bot = new BotClient(botToken);

            bot.addHandler(new CommandHandler());
            bot.addHandler(new TelegramMessageHandler());
            bot.addHandler(new TelegramCallbackHandler());

            bot.startPolling();

            logger.info("Telegram Bot is running and listening for messages...");

        } catch (Exception e) {
            logger.error("Error starting the Telegram Bot: ", e);
            System.err.println("Failed to start the bot: " + e.getMessage());
            System.exit(1);
        }
    }
}
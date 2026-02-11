package ua.notion.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.natanimn.telebof.BotClient;
import ua.notion.telegrambot.config.AppConfig;
import ua.notion.telegrambot.handler.CommandHandler;
import ua.notion.telegrambot.handler.TelegramCallbackHandler;
import ua.notion.telegrambot.handler.TelegramMessageHandler;
import ua.notion.telegrambot.service.BotService;

public class TelegramBotApplication {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotApplication.class);

    public static void main(String[] args) {
        String botToken = getBotToken();
        logger.info("Starting Telegram Bot...");

        try {
            BotClient bot = new BotClient(botToken);
            AppConfig config = new AppConfig();
            BotService botService = new BotService();

            bot.addHandler(new CommandHandler(botService));
            bot.addHandler(new TelegramMessageHandler(config.getBotController()));
            bot.addHandler(new TelegramCallbackHandler(botService));

            bot.startPolling();
            logger.info("Telegram Bot is running and listening for messages...");

        } catch (Exception e) {
            logger.error("Error starting the Telegram Bot: ", e);
            System.exit(1);
        }
    }

    private static String getBotToken() {
        String botToken = System.getenv("TELEGRAM_BOT_TOKEN");

        if (botToken == null || botToken.isEmpty()) {
            Dotenv dotenv = Dotenv.configure().load();
            botToken = dotenv.get("TELEGRAM_BOT_TOKEN");
        }

        if (botToken == null || botToken.isEmpty()) {
            logger.error("TELEGRAM_BOT_TOKEN is not set!");
            System.exit(1);
        }

        return botToken;
    }
}

package ua.notion.telegrambot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.CallbackHandler;
import io.github.natanimn.telebof.types.updates.CallbackQuery;

public class TelegramCallbackHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelegramCallbackHandler.class);

    @CallbackHandler
    public void handleCallbackQuery(BotContext context, CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long userId = callbackQuery.getFrom().getId();

        logger.info("Received callback query with data: {} from user: {}", callbackData, userId);

        String responseText = "";
        switch (callbackData) {
            case "help_cmd":
                responseText = "Available commands:\n" +
                        "/start - Start the bot\n" +
                        "/help - Show this help message\n" +
                        "/info - Get information about the bot";
                break;
            case "info_cmd":
                responseText = "This is a sample Telegram bot built with Java, Maven, and Telebof library.";
                break;
            case "website_cmd":
                responseText = "Visit our website: https://example.com";
                break;
            default:
                responseText = "Unknown command. Use /help to see available commands.";
                break;
        }

        context.answerCallbackQuery(callbackQuery.getId())
                .text(responseText)
                .showAlert(false)
                .exec();

        logger.info("Handled callback query: {} for user {}: {}", callbackData, userId, responseText);
    }
}

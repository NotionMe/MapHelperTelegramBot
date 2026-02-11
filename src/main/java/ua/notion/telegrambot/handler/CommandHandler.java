package ua.notion.telegrambot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.MessageHandler;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardButton;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardMarkup;
import io.github.natanimn.telebof.types.updates.Message;
import ua.notion.telegrambot.config.AppConfig;
import ua.notion.telegrambot.controller.BotController;

public class CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);

    private BotController botController;

    public CommandHandler() {
        AppConfig config = new AppConfig();
        botController = config.getBotController();
    }

    @MessageHandler(commands = "start", priority = -1)
    public void handleStartCommand(BotContext context, Message message) {
        Long userId = message.getFrom().getId();

        logger.info("Received /start command from user {}", userId);

        var response = botController.getStartResponse(userId);
        var keyboard = new InlineKeyboardMarkup();
        keyboard.addKeyboard(new InlineKeyboardButton("Help", "help_cmd"));
        keyboard.addKeyboard(new InlineKeyboardButton("Info", "info_cmd"));
        keyboard.addKeyboard(new InlineKeyboardButton("Website", "website_cmd"));

        context.sendMessage(message.getChat().getId(), response.getText())
                .replyMarkup(keyboard)
                .exec();

        // var keyboard = new ReplyKeyboardMarkup().setResizeKeyboard(true);

        // // Add buttons to the keyboard (in a single row by default)
        // keyboard.add("ID", "Username", "Language");

        // // Send welcome message with the keyboard attached
        // context.sendMessage(message.getChat().getId(), "Welcome! Please choose an
        // option:")
        // .replyMarkup(keyboard) // Attach the keyboard to the message
        // .exec();

        logger.info("Welcome message with keyboard sent to user {}: {}", userId, response.getText());
    }

    @MessageHandler(commands = "help", priority = -1)
    public void handleHelpCommand(BotContext context, Message message) {
        Long userId = message.getFrom().getId();

        logger.info("Received /help command from user {}", userId);

        String helpText = "Available commands:\n" +
                "/start - Start the bot\n" +
                "/help - Show this help message\n" +
                "/info - Get information about the bot";

        context.sendMessage(message.getChat().getId(), helpText).exec();

        logger.info("Help message sent to user {}", userId);
    }

    @MessageHandler(commands = "info", priority = -1)
    public void handleInfoCommand(BotContext context, Message message) {
        Long userId = message.getFrom().getId();

        logger.info("Received /info command from user {}", userId);

        String infoText = "This is a sample Telegram bot built with Java, Maven, and Telebof library.";

        context.sendMessage(message.getChat().getId(), infoText).exec();

        logger.info("Info message sent to user {}", userId);
    }

}

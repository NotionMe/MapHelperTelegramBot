package ua.notion.telegrambot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.enums.MessageType;
import io.github.natanimn.telebof.types.updates.Message;
import ua.notion.telegrambot.config.AppConfig;
import ua.notion.telegrambot.controller.BotController;
import io.github.natanimn.telebof.annotations.MessageHandler;

public class TelegramMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(TelegramMessageHandler.class);
    private BotController botController;

    public TelegramMessageHandler() {
        AppConfig config = new AppConfig();
        botController = config.getBotController();
    }

    @MessageHandler(type = MessageType.TEXT)
    public void handleMessage(BotContext context, Message message) {
        Long userId = message.getFrom().getId();
        String firstName = message.getFrom().getFirstName();
        String lastName = message.getFrom().getLastName();
        String username = message.getFrom().getUsername();
        String languageCode = message.getFrom().getLanguageCode();
        String messageText = message.getText();

        logger.info("Received message from user {}: {}", userId, messageText);

        if (messageText != null && messageText.startsWith("/")) {
            return;
        }

        var response = botController.handleMessage(
                userId,
                firstName,
                lastName,
                username,
                languageCode,
                messageText);

        context.sendMessage(message.getChat().getId(), response.getText()).exec();

        logger.info("Response sent to user {}: {}", userId, response.getText());
    }
}

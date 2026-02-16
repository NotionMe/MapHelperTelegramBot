package ua.notion.telegrambot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.MessageHandler;
import io.github.natanimn.telebof.types.updates.Message;
import ua.notion.telegrambot.service.BotService;

public class CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private final BotService botService;

    public CommandHandler(BotService botService) {
        this.botService = botService;
    }

    @MessageHandler(commands = "start", priority = -1)
    public void handleStartCommand(BotContext context, Message message) {
        logger.info("Received /start command from user {}", message.getFrom().getId());
        botService.sendStartMessage(context, message.getChat().getId());
    }

    @MessageHandler(commands = "floor", priority = 1)
    public void handleMapCommand(BotContext context, Message message) {
        logger.info("Received /floor command from user {}", message.getFrom().getId());
        botService.sendMapMessage(context, message.getChat().getId(), "", "Test caption command"); // Потрібно надсилати карту поверху на
                                                                               // якій користувач останій раз робив
                                                                               // запрос
    }

    @MessageHandler(commands = "help", priority = -1)
    public void handleHelpCommand(BotContext context, Message message) {
        logger.info("Received /help command from user {}", message.getFrom().getId());
        botService.sendHelpMessage(context, message.getChat().getId());
    }

    @MessageHandler(commands = "info", priority = -1)
    public void handleInfoCommand(BotContext context, Message message) {
        logger.info("Received /info command from user {}", message.getFrom().getId());
        botService.sendInfoMessage(context, message.getChat().getId());
    }
}

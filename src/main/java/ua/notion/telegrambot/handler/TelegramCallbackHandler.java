package ua.notion.telegrambot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.CallbackHandler;
import io.github.natanimn.telebof.types.updates.CallbackQuery;
import ua.notion.telegrambot.service.BotService;

public class TelegramCallbackHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelegramCallbackHandler.class);
    private final BotService botService;

    public TelegramCallbackHandler(BotService botService) {
        this.botService = botService;
    }

    @CallbackHandler
    public void handleCallbackQuery(BotContext context, CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChat().getId();
        logger.info("Received callback query: {} from user: {}", callbackData, callbackQuery.getFrom().getId());

        // Получати із db url для карти поверху (потрібно із сесії брати поверх)
        String imageUrl = "https://picsum.photos/536/354";

        switch (callbackData) {
            case "floor_cmd" -> botService.sendMapMessage(context, chatId, imageUrl, "Test image");
            case "help_cmd" -> botService.sendHelpMessage(context, chatId);
            case "info_cmd" -> botService.sendInfoMessage(context, chatId);
            default -> botService.sendUnknownCommand(context, chatId);
        }

        context.answerCallbackQuery(callbackQuery.getId()).exec();
    }
}

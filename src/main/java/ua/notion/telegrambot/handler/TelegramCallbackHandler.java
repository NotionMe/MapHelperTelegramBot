package ua.notion.telegrambot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.CallbackHandler;
import io.github.natanimn.telebof.types.updates.CallbackQuery;
import ua.notion.telegrambot.model.UserSession;
import ua.notion.telegrambot.service.BotService;
import ua.notion.telegrambot.service.RouteService;
import ua.notion.telegrambot.service.UserSessionService;

public class TelegramCallbackHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelegramCallbackHandler.class);
    private final BotService botService;
    private final UserSessionService userSession;
    private final RouteService routeService;

    public TelegramCallbackHandler(BotService botService, UserSessionService userSession, RouteService routeService) {
        this.botService = botService;
        this.userSession = userSession;
        this.routeService = routeService;
    }

    @CallbackHandler
    public void handleCallbackQuery(BotContext context, CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChat().getId();
        logger.info("Received callback query: {} from user: {}", callbackData, callbackQuery.getFrom().getId());

        // Отримуємо URL для карти поверху з бази даних за floor
        UserSession session = userSession.getOrCreateSession(chatId);
        Integer floor = session.getCurrentFloor();
        logger.info("Session floor: {}", floor);

        String imageUrl = routeService.getGifUrlByFloor(floor);
        logger.info("GIF URL from DB: {}", imageUrl);
        if (imageUrl == null) {
            imageUrl = "https://picsum.photos/536/354";
            logger.warn("Using default image URL, floor '{}' not found in routes table or gif_url is null", floor);
        }

        switch (callbackData) {
            case "floor_cmd" -> botService.sendMapMessage(context, chatId, imageUrl, "Test image");
            case "help_cmd" -> botService.sendHelpMessage(context, chatId);
            case "info_cmd" -> botService.sendInfoMessage(context, chatId);
            default -> botService.sendUnknownCommand(context, chatId);
        }

        context.answerCallbackQuery(callbackQuery.getId()).exec();
    }
}

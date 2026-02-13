package ua.notion.telegrambot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.CallbackHandler;
import io.github.natanimn.telebof.enums.ParseMode;
import io.github.natanimn.telebof.types.updates.CallbackQuery;
import ua.notion.telegrambot.model.Cabinet;
import ua.notion.telegrambot.model.UserSession;
import ua.notion.telegrambot.service.BotService;
import ua.notion.telegrambot.service.CabinetService;
import ua.notion.telegrambot.service.CabinetServiceImpl;
import ua.notion.telegrambot.service.RouteService;
import ua.notion.telegrambot.service.UserSessionService;

public class TelegramCallbackHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelegramCallbackHandler.class);
    private final BotService botService;
    private final UserSessionService userSession;
    private final RouteService routeService;
    private final CabinetService cabinetService = new CabinetServiceImpl();

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
            default -> {
                if (callbackData.startsWith("cabinet_")) {
                    handleCabinetCallback(context, callbackQuery, callbackData, chatId);
                } else {
                    botService.sendUnknownCommand(context, chatId);
                }
            }
        }

        context.answerCallbackQuery(callbackQuery.getId()).exec();
    }

    // Відповідь на номер кабінету який користувач вибрав( не вписав )
    private void handleCabinetCallback(BotContext context, CallbackQuery callbackQuery, String callbackData,
            Long chatId) {
        try {
            String cabinetNumber = callbackData.replace("cabinet_", "");
            Cabinet cabinet = cabinetService.getByNumber(cabinetNumber);
            logger.info("cabinet id {}, cabinet {}", cabinetNumber, cabinet);

            if (cabinet == null) {
                context.sendMessage(chatId, "Cabinet not find.").exec();
                return;
            }

            // замість цьої хрені давати користувачу запитання де він знаходиться (або
            // лишити якщо нравиться на твою думку) але краще якщо зберігаєш то перероби
            // логічно
            StringBuilder response = new StringBuilder();
            response.append("🏢 <b>").append(cabinet.getName()).append("</b>\n");
            response.append("📍 Номер: ").append(cabinet.getNumber()).append("\n");
            response.append("📊 Поверх: ").append(cabinet.getFloor().getNumber()).append("\n\n");
            response.append("ℹ️ ").append(cabinet.getDescription()).append("\n\n");

            if (cabinet.getFeatures() != null && !cabinet.getFeatures().isEmpty()) {
                response.append("✨ Особливості:\n");
                for (String feature : cabinet.getFeatures()) {
                    response.append("  • ").append(feature).append("\n");
                }
            }

            context.sendMessage(chatId, response.toString())
                    .parseMode(ParseMode.HTML)
                    .exec();

            // TODO: Тут додати кнопки для побудови маршруту
            // Наприклад: "Побудувати маршрут", "Назад до списку"

            UserSession session = userSession.getOrCreateSession(chatId);
            session.setCurrentCabinet(cabinet.getNumber());
            session.setCurrentFloor(cabinet.getFloor().getNumber());
            userSession.updateSession(session);

        } catch (NumberFormatException e) {
            logger.error("Invalid cabinet ID in callback: {}", callbackData);
            context.sendMessage(chatId, "Error: Invalid cabinet ID.").exec();
        }
    }
}

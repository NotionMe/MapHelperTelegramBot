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
                } else if (callbackData.startsWith("start_floor_")) {
                    handleStartFloorCallback(context, callbackQuery, callbackData, chatId);
                } else if (callbackData.startsWith("landmark_")) {
                    handleLandmarkCallback(context, callbackQuery, callbackData, chatId);
                } else {
                    botService.sendUnknownCommand(context, chatId);
                }
            }
        }

        context.answerCallbackQuery(callbackQuery.getId()).exec();
    }

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

            StringBuilder response = new StringBuilder();
            response.append("🏢 <b>").append(cabinet.getName()).append("</b>\n");
            response.append("📍 Number: ").append(cabinet.getNumber()).append("\n");
            response.append("📊 Floor: ").append(cabinet.getFloor().getNumber()).append("\n\n");
            response.append("ℹ️ ").append(cabinet.getDescription()).append("\n\n");

            if (cabinet.getFeatures() != null && !cabinet.getFeatures().isEmpty()) {
                response.append("✨ Features:\n");
                for (String feature : cabinet.getFeatures()) {
                    response.append("  • ").append(feature).append("\n");
                }
            }

            context.sendMessage(chatId, response.toString())
                    .parseMode(ParseMode.HTML)
                    .exec();

            UserSession session = userSession.getOrCreateSession(chatId);
            session.setCurrentCabinet(cabinet.getNumber());
            userSession.updateSession(session);

            botService.sendStartFloorSelection(context, chatId);

        } catch (NumberFormatException e) {
            logger.error("Invalid cabinet ID in callback: {}", callbackData);
            context.sendMessage(chatId, "Error: Invalid cabinet ID.").exec();
        }
    }

    private void handleStartFloorCallback(BotContext context, CallbackQuery callbackQuery, String callbackData,
            Long chatId) {
        try {
            int floorId = Integer.parseInt(callbackData.replace("start_floor_", ""));

            UserSession session = userSession.getOrCreateSession(chatId);
            session.setCurrentFloor(floorId);
            userSession.updateSession(session);

            botService.sendLandmarkSelection(context, chatId, floorId);

        } catch (NumberFormatException e) {
            logger.error("Invalid floor ID in callback: {}", callbackData);
            context.sendMessage(chatId, "Error: Invalid floor ID.").exec();
        }
    }

    private void handleLandmarkCallback(BotContext context, CallbackQuery callbackQuery, String callbackData,
            Long chatId) {
        try {
            int landmarkId = Integer.parseInt(callbackData.replace("landmark_", ""));
            UserSession session = userSession.getOrCreateSession(chatId);

            String cabinetNumber = session.getCurrentCabinet();
            if (cabinetNumber == null) {
                context.sendMessage(chatId, "Please select a cabinet first.").exec();
                return;
            }

            Cabinet cabinet = cabinetService.getByNumber(cabinetNumber);
            if (cabinet == null) {
                context.sendMessage(chatId, "Cabinet not found.").exec();
                return;
            }

            botService.sendRoute(context, chatId, landmarkId, cabinet.getId());

        } catch (NumberFormatException e) {
            logger.error("Invalid landmark ID: {}", callbackData);
            context.sendMessage(chatId, "Error: Invalid landmark.").exec();
        }
    }
}

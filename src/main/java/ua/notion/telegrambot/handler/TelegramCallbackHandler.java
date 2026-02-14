package ua.notion.telegrambot.handler;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.CallbackHandler;
import io.github.natanimn.telebof.enums.ParseMode;
import io.github.natanimn.telebof.types.updates.CallbackQuery;
import ua.notion.telegrambot.model.Cabinet;
import ua.notion.telegrambot.model.Floor;
import ua.notion.telegrambot.model.UserSession;
import ua.notion.telegrambot.service.BotService;
import ua.notion.telegrambot.service.CabinetService;
import ua.notion.telegrambot.service.CabinetServiceImpl;
import ua.notion.telegrambot.service.FloorService;
import ua.notion.telegrambot.service.FloorServiceImpl;
import ua.notion.telegrambot.service.RouteService;
import ua.notion.telegrambot.service.UserSessionService;

public class TelegramCallbackHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelegramCallbackHandler.class);
    private final BotService botService;
    private final UserSessionService userSession;
    private final RouteService routeService;
    private final CabinetService cabinetService = new CabinetServiceImpl();
    private final FloorService floorService = new FloorServiceImpl();

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

        switch (callbackData) {
            case "floor_cmd" -> {
                CompletableFuture.supplyAsync(() -> {
                    UserSession session = userSession.getOrCreateSession(chatId);
                    Integer currentFloor = session.getCurrentFloor();
                    logger.info("Session floor: {}", currentFloor);
                    Floor floor = floorService.getFloorByNumber(currentFloor);
                    logger.info("Floor data from DB: {}", floor);
                    return floor;
                }).thenAccept(floor -> {
                    String imageUrl = floor != null ? floor.getMapImageUrl() : null;
                    String description = floor != null ? floor.getDescription() : "Floor map";
                    if (imageUrl == null || imageUrl.isEmpty()) {
                        imageUrl = "https://picsum.photos/536/354";
                        logger.warn("Using default image URL, floor data is null or map_image_url is empty");
                    }
                    botService.sendMapMessage(context, chatId, imageUrl, description);
                }).exceptionally(ex -> {
                    logger.error("Error loading floor data: ", ex);
                    botService.sendMapMessage(context, chatId, "https://picsum.photos/536/354", "Error loading map");
                    return null;
                });
            }
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
        String cabinetNumber = callbackData.replace("cabinet_", "");

        CompletableFuture.supplyAsync(() -> cabinetService.getByNumber(cabinetNumber))
                .thenAccept(cabinet -> {
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
                }).exceptionally(ex -> {
                    logger.error("Error loading cabinet: ", ex);
                    context.sendMessage(chatId, "Error loading cabinet information.").exec();
                    return null;
                });
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

            CompletableFuture.supplyAsync(() -> cabinetService.getByNumber(cabinetNumber))
                    .thenAccept(cabinet -> {
                        if (cabinet == null) {
                            context.sendMessage(chatId, "Cabinet not found.").exec();
                            return;
                        }
                        botService.sendRoute(context, chatId, landmarkId, cabinet.getId());
                    }).exceptionally(ex -> {
                        logger.error("Error loading cabinet for route: ", ex);
                        context.sendMessage(chatId, "Error loading route information.").exec();
                        return null;
                    });

        } catch (NumberFormatException e) {
            logger.error("Invalid landmark ID: {}", callbackData);
            context.sendMessage(chatId, "Error: Invalid landmark.").exec();
        }
    }
}

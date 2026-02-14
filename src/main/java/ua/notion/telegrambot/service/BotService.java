package ua.notion.telegrambot.service;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.enums.ParseMode;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardButton;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardMarkup;
import io.github.natanimn.telebof.types.keyboard.ReplyKeyboardMarkup;
import ua.notion.telegrambot.constants.BotMessages;
import ua.notion.telegrambot.repository.LandmarkRepository;
import ua.notion.telegrambot.repository.LandmarkRepositoryImpl;

public class BotService {
    private static final UserSessionService userSessionService = new UserSessionServiceImpl();
    private static final Logger logger = LoggerFactory.getLogger(BotService.class);

    public void sendStartMessage(BotContext context, Long chatId) {
        CompletableFuture.supplyAsync(() -> {
            return userSessionService.getOrCreateSession(chatId);
        }).thenAccept(userSession -> {
            logger.info("Session update {}", userSession);
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

        var keyboard = createMainInlineKeyboard();
        context.sendMessage(chatId, "Hello! Welcome to our Telegram bot. Use /help to see available commands.")
                .replyMarkup(keyboard)
                .exec();
    }

    public void sendMapMessage(BotContext context, Long chatId, String imageUrl, String caption) {
        context.sendPhoto(chatId, imageUrl)
                .caption(caption)
                .exec();

        var keyboard = createMapReplyKeyboard();
        context.sendMessage(chatId, "Welcome! Please choose an option:")
                .replyMarkup(keyboard)
                .exec();
    }

    public void sendHelpMessage(BotContext context, Long chatId) {
        context.sendMessage(chatId, BotMessages.HELP_TEXT).exec();
    }

    public void sendInfoMessage(BotContext context, Long chatId) {
        context.sendMessage(chatId, BotMessages.INFO_TEXT).exec();
    }

    public void sendUnknownCommand(BotContext context, Long chatId) {
        context.sendMessage(chatId, BotMessages.UNKNOWN_COMMAND).exec();
    }

    private InlineKeyboardMarkup createMainInlineKeyboard() {
        var keyboard = new InlineKeyboardMarkup();
        keyboard.addKeyboard(new InlineKeyboardButton("Floor", "floor_cmd"));
        keyboard.addKeyboard(new InlineKeyboardButton("Help", "help_cmd"));
        keyboard.addKeyboard(new InlineKeyboardButton("Info", "info_cmd"));
        return keyboard;
    }

    private ReplyKeyboardMarkup createMapReplyKeyboard() {
        var keyboard = new ReplyKeyboardMarkup().setResizeKeyboard(true);
        keyboard.add("1 floor");
        keyboard.add("2 floor");
        keyboard.add("3 floor");
        keyboard.add("4 floor");
        keyboard.add("5 floor");
        return keyboard;
    }

    public void sendStartFloorSelection(BotContext context, Long chatId) {
        var keyboard = new InlineKeyboardMarkup();
        keyboard.addKeyboard(new InlineKeyboardButton("1 floor", "start_floor_1"));
        keyboard.addKeyboard(new InlineKeyboardButton("2 floor", "start_floor_2"));
        keyboard.addKeyboard(new InlineKeyboardButton("3 floor", "start_floor_3"));
        keyboard.addKeyboard(new InlineKeyboardButton("4 floor", "start_floor_4"));
        keyboard.addKeyboard(new InlineKeyboardButton("5 floor", "start_floor_5"));

        context.sendMessage(chatId, "🏢 Select your current floor:")
                .replyMarkup(keyboard)
                .exec();
    }

    private final LandmarkRepository landmarkRepository = new LandmarkRepositoryImpl();
    private final RouteService routeService = new RouteServiceImpl();

    public void sendLandmarkSelection(BotContext context, Long chatId, Integer floorId) {
        CompletableFuture.supplyAsync(() -> landmarkRepository.findAllByFloorId(floorId))
                .thenAccept(landmarks -> {
                    if (landmarks.isEmpty()) {
                        context.sendMessage(chatId, "No landmarks found for this floor.").exec();
                        return;
                    }

                    var keyboard = new InlineKeyboardMarkup();
                    for (var landmark : landmarks) {
                        keyboard.addKeyboard(new InlineKeyboardButton(landmark.getName(), "landmark_" + landmark.getId()));
                    }

                    context.sendMessage(chatId, "📍 Where are you now? Choose the nearest landmark:")
                            .replyMarkup(keyboard)
                            .exec();
                }).exceptionally(ex -> {
                    logger.error("Error loading landmarks: ", ex);
                    context.sendMessage(chatId, "Error loading landmarks. Please try again.").exec();
                    return null;
                });
    }

    public void sendRoute(BotContext context, Long chatId, Integer landmarkId, Integer cabinetId) {
        CompletableFuture.supplyAsync(() -> routeService.getRoute(landmarkId, cabinetId))
                .thenAccept(routeOptional -> {
                    if (routeOptional.isEmpty()) {
                        context.sendMessage(chatId, "🚫 Route not found. Try another landmark.").exec();
                        return;
                    }

                    var route = routeOptional.get();
                    String caption = route.getDirection() != null
                            ? "🛣 <b>Route:</b> " + route.getDirection() + "\n📏 <b>Distance:</b> " + route.getDistance() + "m"
                            : "Here is your route!";

                    if (route.getGifTelegramId() != null) {
                        try {
                            context.sendPhoto(chatId, route.getGifTelegramId())
                                    .caption(caption)
                                    .parseMode(ParseMode.HTML)
                                    .exec();
                            return;
                        } catch (Exception e) {
                            logger.warn("Invalid file_id, falling back to URL: {}", e.getMessage());
                            route.setGifTelegramId(null);
                            routeService.saveRoute(route);
                        }
                    }

                    if (route.getGifUrl() != null) {
                        try {
                            var sentMessage = context.sendPhoto(chatId, route.getGifUrl())
                                    .caption(caption)
                                    .parseMode(ParseMode.HTML)
                                    .exec();

                            if (sentMessage.getPhoto() != null && !sentMessage.getPhoto().isEmpty()) {
                                String fileId = sentMessage.getPhoto().get(sentMessage.getPhoto().size() - 1).getFileId();
                                route.setGifTelegramId(fileId);
                                routeService.saveRoute(route);
                            }
                        } catch (Exception e) {
                            logger.error("Error sending route image: ", e);
                            context.sendMessage(chatId, "Error sending route image: " + e.getMessage()).exec();
                        }
                    } else {
                        context.sendMessage(chatId, caption).parseMode(ParseMode.HTML).exec();
                    }
                }).exceptionally(ex -> {
                    logger.error("Error loading route: ", ex);
                    context.sendMessage(chatId, "Error loading route. Please try again.").exec();
                    return null;
                });
    }
}

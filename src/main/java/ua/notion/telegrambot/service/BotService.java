package ua.notion.telegrambot.service;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.enums.ParseMode;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardButton;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardMarkup;
import io.github.natanimn.telebof.types.keyboard.ReplyKeyboardMarkup;
import ua.notion.telegrambot.constants.BotMessages;
import ua.notion.telegrambot.repository.LandmarkRepository;
import ua.notion.telegrambot.repository.LandmarkRepositoryImpl;
import ua.notion.telegrambot.repository.LandmarkRepository;
import ua.notion.telegrambot.repository.LandmarkRepositoryImpl;
import ua.notion.telegrambot.service.RouteService;
import ua.notion.telegrambot.service.RouteServiceImpl;
import ua.notion.telegrambot.service.UserSessionService;
import ua.notion.telegrambot.service.UserSessionServiceImpl;

public class BotService {
    private static final UserSessionService userSessionService = new UserSessionServiceImpl();

    public void sendStartMessage(BotContext context, Long chatId) {
        userSessionService.getOrCreateSession(chatId);
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
        var landmarks = landmarkRepository.findAllByFloorId(floorId);
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
    }

    public void sendRoute(BotContext context, Long chatId, Integer landmarkId, Integer cabinetId) {
        var routeOptional = routeService.getRoute(landmarkId, cabinetId);

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
                System.err.println("Invalid file_id, falling back to URL: " + e.getMessage());
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
                context.sendMessage(chatId, "Error sending route image: " + e.getMessage()).exec();
            }
        } else {
            context.sendMessage(chatId, caption).parseMode(ParseMode.HTML).exec();
        }
    }
}

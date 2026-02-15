package ua.notion.telegrambot.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.MessageHandler;
import io.github.natanimn.telebof.enums.MessageType;
import io.github.natanimn.telebof.enums.ParseMode;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardButton;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardMarkup;
import io.github.natanimn.telebof.types.updates.Message;
import ua.notion.telegrambot.controller.BotController;
import ua.notion.telegrambot.model.Cabinet;
import ua.notion.telegrambot.model.Floor;
import ua.notion.telegrambot.model.UserSession;
import ua.notion.telegrambot.service.BotService;
import ua.notion.telegrambot.service.CabinetService;
import ua.notion.telegrambot.service.CabinetServiceImpl;
import ua.notion.telegrambot.service.FloorService;
import ua.notion.telegrambot.service.FloorServiceImpl;
import ua.notion.telegrambot.service.UserSessionService;
import ua.notion.telegrambot.service.UserSessionServiceImpl;

public class TelegramMessageHandler {
  private static final Logger logger = LoggerFactory.getLogger(TelegramMessageHandler.class);
  private final BotController botController;
  private final BotService botService;
  private final CabinetService cabinetService = new CabinetServiceImpl();
  private final FloorService floorService = new FloorServiceImpl();
  private final UserSessionService userSessionService = new UserSessionServiceImpl();

  public TelegramMessageHandler(BotController botController, BotService botService) {
    this.botController = botController;
    this.botService = botService;
  }

  @MessageHandler(texts = { "1 floor", "2 floor", "3 floor", "4 floor", "5 floor" })
  void text(BotContext context, Message message) {
    String text = message.getText();
    Long userId = message.getFrom().getId();
    Long chatId = message.getChat().getId();

    int floorNumber = Character.getNumericValue(text.charAt(0));

    UserSession session = userSessionService.getOrCreateSession(userId);
    session.setCurrentFloor(floorNumber);
    userSessionService.updateSession(session);
    logger.info("Saved floor {} to user session for user {}", floorNumber, userId);

    Floor floor = floorService.getFloorByNumber(floorNumber);

    if (floor == null) {
      context.sendMessage(chatId,
          "Information about " + floorNumber + " The floor is missing from the database.")
          .exec();
      return;
    }

    List<Cabinet> cabinets = floor.getCabinets();

    if (cabinets == null || cabinets.isEmpty()) {
      context.sendMessage(chatId,
          "🏢 Floor: " + floor.getName() + "\n No offices have been added yet.")
          .exec();
      return;
    }

    String responseText = String.format("🏢 <b>%s</b>\n\nℹ️ <i>%s</i>\n\n Choose an office:",
        floor.getName(),
        floor.getDescription());

    var keyboard = createCabinetsKeyboard(cabinets);

    context.sendPhoto(chatId, floor.getMapImageUrl())
        .caption(responseText)
        .parseMode(ParseMode.HTML)
        .replyMarkup(keyboard)
        .exec();
  }

  private InlineKeyboardMarkup createCabinetsKeyboard(List<Cabinet> cabinets) {
    var keyboard = new InlineKeyboardMarkup();
    for (Cabinet cabinet : cabinets) {
      String callbackData = "cabinet_" + cabinet.getNumber();
      String buttonText = cabinet.getNumber() + " — " + cabinet.getName();
      keyboard.addKeyboard(new InlineKeyboardButton(buttonText, callbackData));
    }
    return keyboard;
  }

  @MessageHandler(type = MessageType.TEXT)
  void handleCabinetNumber(BotContext context, Message message) {
    String text = message.getText().trim();
    Long chatId = message.getChat().getId();
    Long userId = message.getFrom().getId();

    logger.info("Received cabinet number from user {}: {}", userId, text);

    Cabinet cabinet = cabinetService.getByNumber(text);

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

    UserSession session = userSessionService.getOrCreateSession(userId);
    session.setCurrentCabinet(cabinet.getNumber());
    userSessionService.updateSession(session);

    botService.sendStartFloorSelection(context, chatId);
  }
}
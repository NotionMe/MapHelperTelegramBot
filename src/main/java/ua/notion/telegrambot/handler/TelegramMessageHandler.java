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
import ua.notion.telegrambot.service.CabinetService;
import ua.notion.telegrambot.service.CabinetServiceImpl;
import ua.notion.telegrambot.service.FloorService;
import ua.notion.telegrambot.service.FloorServiceImpl;
import ua.notion.telegrambot.service.UserSessionService;
import ua.notion.telegrambot.service.UserSessionServiceImpl;

public class TelegramMessageHandler {
  private static final Logger logger = LoggerFactory.getLogger(TelegramMessageHandler.class);
  private final BotController botController;
  private final CabinetService cabinetService = new CabinetServiceImpl();
  private final FloorService floorService = new FloorServiceImpl();
  private final UserSessionService userSessionService = new UserSessionServiceImpl();

  public TelegramMessageHandler(BotController botController) {
    this.botController = botController;
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

    context.sendMessage(chatId, responseText)
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

  // Відповідь на номер кабінету який користувач всписав в чат ( не вибрав )
  @MessageHandler(type = MessageType.TEXT)
  void handleCabinetNumber(BotContext context, Message message) {
    String text = message.getText().trim();

    logger.info("Received cabinet number from user {}: {}", message.getFrom().getId(), text);

    // Шукаємо кабінет
    Cabinet cabinet = cabinetService.getByNumber(text);

    if (cabinet == null) {
      context.sendMessage(message.getChat().getId(),
          "❌ Кабінет " + text + " не знайдено в базі даних.\n\n" +
              "Перевірте номер або оберіть поверх з меню.")
          .exec();
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

    // TODO: Тут додати кнопки для побудови маршруту
    // Наприклад: "Побудувати маршрут", "Назад до списку"

    context.sendMessage(message.getChat().getId(), response.toString())
        .parseMode(ParseMode.HTML)
        .exec();
  }
}
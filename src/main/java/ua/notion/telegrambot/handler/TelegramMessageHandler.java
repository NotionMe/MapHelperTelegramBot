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
import ua.notion.telegrambot.service.CabinetService;
import ua.notion.telegrambot.service.CabinetServiceImpl;
import ua.notion.telegrambot.service.FloorService;
import ua.notion.telegrambot.service.FloorServiceImpl;

public class TelegramMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelegramMessageHandler.class);
    private final BotController botController;
    private final CabinetService cabinetService = new CabinetServiceImpl();
    private final FloorService floorService = new FloorServiceImpl();

    public TelegramMessageHandler(BotController botController) {
        this.botController = botController;
    }

    @MessageHandler(type = MessageType.TEXT)
    public void handleMessage(BotContext context, Message message) {
        if (isCommand(message.getText())) {
            return;
        }

        Long userId = message.getFrom().getId();
        logger.info("Received message from user {}: {}", userId, message.getText());

        var response = botController.handleMessage(
                userId,
                message.getFrom().getFirstName(),
                message.getFrom().getLastName(),
                message.getFrom().getUsername(),
                message.getFrom().getLanguageCode(),
                message.getText());

        context.sendMessage(message.getChat().getId(), response.getText()).exec();
    }

    private boolean isCommand(String text) {
        return text != null && text.startsWith("/");
    }

  @MessageHandler(texts = { "1 floor", "2 floor", "3 floor", "4 floor", "5 floor" })
  void text(BotContext context, Message message) {
    String text = message.getText();

    int floorNumber = Character.getNumericValue(text.charAt(0));

    Floor floor = floorService.getFloorByNumber(floorNumber);

    if (floor == null) {
      context.sendMessage(message.getChat().getId(),
              "😔 Інформація про " + floorNumber + " поверх відсутня в базі.")
          .exec();
      return;
    }

    List<Cabinet> cabinets = floor.getCabinets();

    if (cabinets == null || cabinets.isEmpty()) {
      context.sendMessage(message.getChat().getId(),
              "🏢 Поверх: " + floor.getName() + "\n😔 Кабінетів поки не додано.")
          .exec();
      return;
    }

    String responseText = String.format("🏢 <b>%s</b>\n\nℹ️ <i>%s</i>\n\n👇 Оберіть кабінет:",
        floor.getName(),
        floor.getDescription());

    var keyboard = createCabinetsKeyboard(cabinets);

    context.sendMessage(message.getChat().getId(), responseText)
        .parseMode(ParseMode.HTML)
        .replyMarkup(keyboard)
        .exec();
  }

  private InlineKeyboardMarkup createCabinetsKeyboard(List<Cabinet> cabinets) {
    var keyboard = new InlineKeyboardMarkup();
    for (Cabinet cabinet : cabinets) {
      String callbackData = "cabinet_" + cabinet.getId();
      String buttonText = cabinet.getNumber() + " — " + cabinet.getName();
      keyboard.addKeyboard(new InlineKeyboardButton(buttonText, callbackData));
    }
    return keyboard;
  }
}
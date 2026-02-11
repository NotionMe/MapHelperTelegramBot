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

public class TelegramMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(TelegramMessageHandler.class);
    private final BotController botController;

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
        var user = message.getFrom();
        String response = null;

        switch (message.getText()) {
            case "1 floor" -> {
                // Тут фейкові дані для перевірки, потрібно із бази даних все це брати
                var cabinets = fakeData();
                // ну короче цю хуйню удалити це все тест показ
                String cabinet = " ";
                for (Cabinet cabinete : cabinets) {
                    cabinet = cabinete.getFloor().getName();
                }

                var keyboard = createCabinetsKeyboard(cabinets);
                context.sendMessage(message.getChat().getId(),
                        "🏢 <b>" + cabinet + "</b>\nОберіть кабінет:")
                        .parseMode(ParseMode.HTML)
                        .replyMarkup(keyboard)
                        .exec();
                return;
            }

            case "2 floor" -> {
                if (user.getUsername() == null) {
                    response = "<i>You don't have a username set in your Telegram profile.</i>";
                } else {
                    response = String.format("<b>Your username is:</b> @%s", user.getUsername());
                }
                break;
            }

            case "3 floor" -> {
                String language = (user.getLanguageCode() != null) ? user.getLanguageCode()
                        : "not specified";
                response = String.format("<b>Your language code is:</b> %s", language);
                break;
            }
            default -> {
                response = "Unknown option selected.";
            }
        }

        context.sendMessage(message.getChat().getId(), response)
                .parseMode(ParseMode.HTML)
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

    private List<Cabinet> fakeData() {
        Floor floor1 = new Floor();
        floor1.setId(1);
        floor1.setNumber(1);
        floor1.setName("Перший поверх");
        floor1.setDescription("Головний вхід, деканат, бібліотека");

        Cabinet c1 = new Cabinet();
        c1.setId(1);
        c1.setNumber("101");
        c1.setName("Деканат");
        c1.setFloor(floor1);

        Cabinet c2 = new Cabinet();
        c2.setId(2);
        c2.setNumber("102");
        c2.setName("Бібліотека");
        c2.setFloor(floor1);

        Cabinet c3 = new Cabinet();
        c3.setId(3);
        c3.setNumber("103");
        c3.setName("Кафедра інформатики");
        c3.setFloor(floor1);

        Cabinet c4 = new Cabinet();
        c4.setId(4);
        c4.setNumber("104");
        c4.setName("Аудиторія 104");
        c4.setFloor(floor1);

        Cabinet c5 = new Cabinet();
        c5.setId(5);
        c5.setNumber("105");
        c5.setName("Методичний кабінет");
        c5.setFloor(floor1);

        List<Cabinet> cabinets = List.of(c1, c2, c3, c4, c5);
        return cabinets;
    }
}

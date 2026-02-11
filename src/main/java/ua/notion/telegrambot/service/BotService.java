package ua.notion.telegrambot.service;

import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardButton;
import io.github.natanimn.telebof.types.keyboard.InlineKeyboardMarkup;
import io.github.natanimn.telebof.types.keyboard.ReplyKeyboardMarkup;
import ua.notion.telegrambot.constants.BotMessages;

public class BotService {

    public void sendStartMessage(BotContext context, Long chatId) {
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
}

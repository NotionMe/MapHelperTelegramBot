package ua.notion.telegrambot.constants;

public class BotMessages {

        public static final String HELP_TEXT = """
                        Available commands:
                        /start - Start the bot
                        /help - Show this help message
                        /info - Get information about the bot""";

        public static final String INFO_TEXT = "This is a sample Telegram bot built with Java, Maven, and Telebof library.";

        public static final String UNKNOWN_COMMAND = "Unknown command. Use /help to see available commands.";

        private BotMessages() {
        }
}

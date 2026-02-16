# MapHelperTelegramBot 📍

This bot helps you navigate the building, find the rooms you need, and build routes from the nearest landmarks.

## 🛠 Available Commands

- **/start** - Start the bot and open the main menu.
- **/help** - Get help on how to use the bot.
- **/info** - View information about the bot.
- **/floor** - Quick access to floor and room selection.

## 🚀 How to Use

Building a route consists of three simple steps:

### 1. 🎯 Where do you want to go? (Choosing a room)
In the main menu, tap **"FLOOR"**. The bot will prompt you to select the desired floor from a list.
Next, after selecting the floor, the bot will ask you to choose the specific room from the list or enter its number.

### 2. 🏢 Where are you right now? (Choosing your current floor)
Since the route depends on your current location, the bot will ask for your floor.
- If you are on the same floor as your destination, select this floor.
- If you are on a different floor, select it so the bot can build a route starting from the stairs or the elevator.

### 3. 🚩 Starting point (Choosing a landmark)
After selecting your floor, the bot will show a list of known **landmarks** (e.g., *"Main Entrance"*, *"Stairs #1"*, *"Library"*).
Choose the one that is closest to you.

After that, the bot will send you a detailed route description along with an image!

## 🧑‍💻 Technologies

- **Java 21** - programming language
- **Telebof 1.3.0** - Telegram Bot API library
- **Hibernate 6.4.4.Final** - ORM for database operations
- **PostgreSQL** - database
- **Caffeine 3.1.8** - caching (Hibernate second-level cache)
- **Maven** - build system

## 📋 Requirements

- Java 21 or higher
- Maven 3.8+
- PostgreSQL database
- Telegram Bot Token

## ⚙️ Configuration

1. Create a `.env` file in the project root:
```
TELEGRAM_BOT_TOKEN=your_bot_token_here
```

2. Configure database connection in `src/main/resources/META-INF/hibernate.cfg.xml`

## 🚀 Running

### Build the project:
```bash
mvn clean package
```

### Run:
```bash
java -jar target/telegram-bot-1.0.0.jar
```

Or directly via Maven:
```bash
mvn clean compile exec:java -Dexec.mainClass="ua.notion.telegrambot.TelegramBotApplication"
```

## 📁 Project Structure

```
src/main/java/ua/notion/telegrambot/
├── config/         # Hibernate configuration
├── constants/      # Constants
├── controller/     # Controllers
├── handler/        # Message and callback handlers
├── model/          # Entity classes (Floor, Cabinet, Landmark, Route, etc.)
├── repository/     # DAO layer for database operations
├── service/        # Business logic
└── util/           # Utility classes
```
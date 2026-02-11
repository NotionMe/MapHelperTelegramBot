package ua.notion.telegrambot.model;

public class MessageResponse {
    private Long chatId;
    private String text;
    private boolean success;

    public MessageResponse() {}

    public MessageResponse(Long chatId, String text, boolean success) {
        this.chatId = chatId;
        this.text = text;
        this.success = success;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "chatId=" + chatId +
                ", text='" + text + '\'' +
                ", success=" + success +
                '}';
    }
}
package ua.notion.telegrambot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "telegram_users")
public class TelegramUser {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "language_code", length = 10)
    private String languageCode;

    public TelegramUser() {}

    public TelegramUser(Long userId, String firstName, String lastName, String username, String languageCode) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.languageCode = languageCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return "TelegramUser{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

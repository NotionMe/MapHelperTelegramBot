package ua.notion.telegrambot.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "current_floor")
    private Integer currentFloor;

    @Column(name = "current_landmark")
    private String currentLandmark;

    @Column(name = "current_cabinet")
    private String currentCabinet;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity = LocalDateTime.now();

    public UserSession() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(Integer currentFloor) {
        this.currentFloor = currentFloor;
    }

    public String getCurrentLandmark() {
        return currentLandmark;
    }

    public void setCurrentLandmark(String currentLandmark) {
        this.currentLandmark = currentLandmark;
    }

    public String getCurrentCabinet() {
        return currentCabinet;
    }

    public void setCurrentCabinet(String currentCabinet) {
        this.currentCabinet = currentCabinet;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", userId=" + userId +
                ", currentFloor=" + currentFloor +
                '}';
    }
}

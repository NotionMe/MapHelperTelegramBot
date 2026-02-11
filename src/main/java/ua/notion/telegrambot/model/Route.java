package ua.notion.telegrambot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "routes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cabinet_id", "landmark_id"})
})
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cabinet_id", nullable = false)
    private Cabinet cabinet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landmark_id", nullable = false)
    private Landmark landmark;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "direction", columnDefinition = "TEXT")
    private String direction;

    @Column(name = "gif_url", length = 500)
    private String gifUrl;

    @Column(name = "gif_telegram_file_id", length = 200)
    private String gifTelegramFileId;

    public Route() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cabinet getCabinet() {
        return cabinet;
    }

    public void setCabinet(Cabinet cabinet) {
        this.cabinet = cabinet;
    }

    public Landmark getLandmark() {
        return landmark;
    }

    public void setLandmark(Landmark landmark) {
        this.landmark = landmark;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getGifTelegramFileId() {
        return gifTelegramFileId;
    }

    public void setGifTelegramFileId(String gifTelegramFileId) {
        this.gifTelegramFileId = gifTelegramFileId;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", cabinet=" + (cabinet != null ? cabinet.getNumber() : null) +
                ", landmark=" + (landmark != null ? landmark.getCode() : null) +
                '}';
    }
}

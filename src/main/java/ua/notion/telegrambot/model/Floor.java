package ua.notion.telegrambot.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "floors")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number", nullable = false, unique = true)
    private Integer number;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cabinet_count")
    private Integer cabinetCount = 0;

    @Column(name = "map_image_url", length = 500)
    private String mapImageUrl;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cabinet> cabinets = new ArrayList<>();

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Landmark> landmarks = new ArrayList<>();

    public Floor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCabinetCount() {
        return cabinetCount;
    }

    public void setCabinetCount(Integer cabinetCount) {
        this.cabinetCount = cabinetCount;
    }

    public String getMapImageUrl() {
        return mapImageUrl;
    }

    public void setMapImageUrl(String mapImageUrl) {
        this.mapImageUrl = mapImageUrl;
    }

    public List<Cabinet> getCabinets() {
        return cabinets;
    }

    public void setCabinets(List<Cabinet> cabinets) {
        this.cabinets = cabinets;
    }

    public List<Landmark> getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(List<Landmark> landmarks) {
        this.landmarks = landmarks;
    }

    @Override
    public String toString() {
        return "Floor{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}

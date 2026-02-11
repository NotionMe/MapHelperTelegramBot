package ua.notion.telegrambot.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "landmarks")
public class Landmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "landmark_nearby_cabinets", joinColumns = @JoinColumn(name = "landmark_id"))
    @Column(name = "cabinet_id")
    private List<Integer> nearbyCabinets = new ArrayList<>();

    @OneToMany(mappedBy = "landmark", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Route> routes = new ArrayList<>();

    public Landmark() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public List<Integer> getNearbyCabinets() {
        return nearbyCabinets;
    }

    public void setNearbyCabinets(List<Integer> nearbyCabinets) {
        this.nearbyCabinets = nearbyCabinets;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "Landmark{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

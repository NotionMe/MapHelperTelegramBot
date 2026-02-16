package ua.notion.telegrambot.repository;

import ua.notion.telegrambot.model.Route;
import java.util.List;
import java.util.Optional;

public interface RouteRepository {

  Optional<Route> findByLandmarkAndCabinet(Integer landmarkId, Integer cabinetId);

  List<Route> findAllByCabinetId(Integer cabinetId);

  String findGifUrlByFloor(Integer floor);

  boolean save(Route route);
}

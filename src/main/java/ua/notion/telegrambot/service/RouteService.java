package ua.notion.telegrambot.service;

import java.util.Optional;
import ua.notion.telegrambot.model.Route;
import java.util.List;

public interface RouteService {

  Optional<Route> getRoute(Integer landmarkId, Integer cabinetId);

  List<Route> getRoutesToCabinet(Integer cabinetId);

  String getGifUrlByFloor(Integer floor);

  void saveRoute(Route route);
}

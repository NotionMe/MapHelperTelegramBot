package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.Route;
import java.util.List;

public interface RouteService {

  Route getRoute(Integer landmarkId, Integer cabinetId);
  List<Route> getRoutesToCabinet(Integer cabinetId);
}

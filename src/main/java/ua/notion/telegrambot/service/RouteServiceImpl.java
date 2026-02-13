package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.Route;
import ua.notion.telegrambot.repository.RouteRepository;
import ua.notion.telegrambot.repository.RouteRepositoryImpl;

import java.util.List;

public class RouteServiceImpl implements RouteService {

  private final RouteRepository routeRepository = new RouteRepositoryImpl();

  @Override
  public Route getRoute(Integer landmarkId, Integer cabinetId) {
    if (landmarkId == null || cabinetId == null) {
      return null;
    }
    return routeRepository.findByLandmarkAndCabinet(landmarkId, cabinetId).orElse(null);
  }

  @Override
  public List<Route> getRoutesToCabinet(Integer cabinetId) {
    if (cabinetId == null) {
      return List.of();
    }
    return routeRepository.findAllByCabinetId(cabinetId);
  }

  @Override
  public String getGifUrlByFloor(Integer floor) {
    if (floor == null || floor < 1) {
      return null;
    }
    return routeRepository.findGifUrlByFloor(floor);
  }
}
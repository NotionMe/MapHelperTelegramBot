package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.Route;
import ua.notion.telegrambot.repository.RouteRepository;
import ua.notion.telegrambot.repository.RouteRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class RouteServiceImpl implements RouteService {

  private final RouteRepository routeRepository = new RouteRepositoryImpl();

  @Override
  public Optional<Route> getRoute(Integer landmarkId, Integer cabinetId) {
    if (landmarkId == null || cabinetId == null) {
      return Optional.empty();
    }
    return routeRepository.findByLandmarkAndCabinet(landmarkId, cabinetId);
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

  @Override
  public void saveRoute(Route route) {
    if (route != null) {
      routeRepository.save(route);
    }
  }
}
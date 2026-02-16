package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.Floor;
import ua.notion.telegrambot.repository.FloorRepository;
import ua.notion.telegrambot.repository.FloorRepositoryImpl;

public class FloorServiceImpl implements FloorService {

  private final FloorRepository floorRepository = new FloorRepositoryImpl();

  @Override
  public Floor getFloorByNumber(int number) {
    return floorRepository.findByNumber(number).orElse(null);
  }

  @Override
  public String getMapImageUrlByFloorNumber(int number) {
    return floorRepository.findMapImageUrlByNumber(number).orElse(null);
  }
}
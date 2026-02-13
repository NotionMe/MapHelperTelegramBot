package ua.notion.telegrambot.service;

import java.util.List;
import ua.notion.telegrambot.model.Landmark;
import ua.notion.telegrambot.repository.LandmarkRepository;
import ua.notion.telegrambot.repository.LandmarkRepositoryImpl;

public class LandmarkServiceImpl implements LandmarkService {

  private final LandmarkRepository landmarkRepository = new LandmarkRepositoryImpl();

  @Override
  public List<Landmark> getLandmarksByFloor(int floorNumber) {
    if (floorNumber < 1 || floorNumber > 5) {
      return List.of();
    }
    return landmarkRepository.findAllByFloorNumber(floorNumber);
  }

  @Override
  public Landmark getLandmarkByCode(String code) {
    if (code == null || code.trim().isEmpty()) return null;
    return landmarkRepository.findByCode(code).orElse(null);
  }

  @Override
  public Landmark getLandmarkById(Integer id) {
    if (id == null) return null;
    return landmarkRepository.findById(id).orElse(null);
  }
}

package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.Cabinet;
import ua.notion.telegrambot.repository.CabinetRepository;
import ua.notion.telegrambot.repository.CabinetRepositoryImpl;

import java.util.List;

public class CabinetServiceImpl implements CabinetService {

  private final CabinetRepository cabinetRepository = new CabinetRepositoryImpl();

  @Override
  public List<Cabinet> getAllByFloor(int floorNumber) {
    if (floorNumber < 1 || floorNumber > 5) {
      return List.of();
    }

    return cabinetRepository.findAllByFloorNumber(floorNumber);
  }

  @Override
  public Cabinet getById(Integer id) {
    if (id == null) return null;
    return cabinetRepository.findById(id);
  }

  @Override
  public Cabinet getByNumber(String number) {
    if (number == null || number.trim().isEmpty()) return null;
    return cabinetRepository.findByNumber(number);
  }
}
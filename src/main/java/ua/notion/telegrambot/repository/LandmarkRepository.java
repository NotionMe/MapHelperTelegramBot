package ua.notion.telegrambot.repository;

import ua.notion.telegrambot.model.Landmark;
import java.util.List;
import java.util.Optional;

public interface LandmarkRepository {

  List<Landmark> findAllByFloorNumber(int floorNumber);

  List<Landmark> findAllByFloorId(Integer floorId);

  Optional<Landmark> findByCode(String code);

  Optional<Landmark> findById(Integer id);
}
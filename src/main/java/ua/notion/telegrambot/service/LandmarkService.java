package ua.notion.telegrambot.service;

import java.util.List;
import ua.notion.telegrambot.model.Landmark;

public interface LandmarkService {
  List<Landmark> getLandmarksByFloor(int floorNumber);
  Landmark getLandmarkByCode(String code);
  Landmark getLandmarkById(Integer id);
}

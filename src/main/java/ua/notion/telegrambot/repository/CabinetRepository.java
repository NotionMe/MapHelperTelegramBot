package ua.notion.telegrambot.repository;

import ua.notion.telegrambot.model.Cabinet;
import java.util.List;

public interface CabinetRepository {
  boolean save(Cabinet cabinet);

  Cabinet findById(Integer id);
  List<Cabinet> findAllByFloorNumber(int floorNumber);

  Cabinet findByNumber(String number);
}
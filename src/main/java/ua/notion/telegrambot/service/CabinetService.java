package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.Cabinet;
import java.util.List;

public interface CabinetService {

  List<Cabinet> getAllByFloor(int floorNumber);

  Cabinet getById(Integer id);

  Cabinet getByNumber(String number);
}
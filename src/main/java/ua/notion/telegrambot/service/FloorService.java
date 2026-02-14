package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.Floor;

public interface FloorService {
  Floor getFloorByNumber(int number);

  String getMapImageUrlByFloorNumber(int number);
}
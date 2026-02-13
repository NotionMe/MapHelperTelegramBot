package ua.notion.telegrambot.service;

import ua.notion.telegrambot.model.Floor;
import java.util.Optional;

public interface FloorService {
  Floor getFloorByNumber(int number);
}
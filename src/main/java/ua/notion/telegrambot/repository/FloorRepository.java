package ua.notion.telegrambot.repository;

import java.util.Optional;
import ua.notion.telegrambot.model.Floor;

public interface FloorRepository {
  Optional<Floor> findByNumber(int number);
}

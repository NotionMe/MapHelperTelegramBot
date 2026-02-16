package ua.notion.telegrambot;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ua.notion.telegrambot.model.Cabinet;
import ua.notion.telegrambot.model.Floor;
import ua.notion.telegrambot.repository.CabinetRepository;
import ua.notion.telegrambot.repository.CabinetRepositoryImpl;
import ua.notion.telegrambot.service.FloorService;
import ua.notion.telegrambot.service.FloorServiceImpl;
import ua.notion.telegrambot.util.HibernateUtil;

public class DatabaseTest {

  public static void main(String[] args) {
    // createFloor5AndCabinets();
    FloorService floorService = new FloorServiceImpl();
    CabinetRepository cabinetRepository = new CabinetRepositoryImpl();

    Floor floor = floorService.getFloorByNumber(1);

    Cabinet cabinet = new Cabinet();
    cabinet.setNumber("Toilet");
    cabinet.setName("Toilet");
    cabinet.setDescription("Bathroom");
    cabinet.setFeatures(List.of("Bathroom", "Toilet"));
    cabinet.setFloor(floor);
    cabinetRepository.save(cabinet);
  }

  public static void createFloor3AndCabinets() {
    FloorService floorService = new FloorServiceImpl();
    CabinetRepository cabinetRepository = new CabinetRepositoryImpl();

    // Створюємо 3-й поверх якщо не існує
    Floor floor3 = floorService.getFloorByNumber(3);
    if (floor3 == null) {
      System.out.println("Floor 3 not found! Creating floor 3...");
      try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        Transaction transaction = session.beginTransaction();
        floor3 = new Floor();
        floor3.setNumber(3);
        floor3.setName("Third floor");
        floor3.setDescription("Third floor with science and creative classrooms");
        session.persist(floor3);
        transaction.commit();
        System.out.println("Floor 3 created successfully!");
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }
      // Отримуємо знову після створення
      floor3 = floorService.getFloorByNumber(3);
    }

    if (floor3 == null) {
      System.out.println("Failed to create or find floor 3!");
      return;
    }

    List<String[]> cabinetsData = Arrays.asList(
        new String[] { "301", "Physics laboratory", "Laboratory for physics experiments and practical work" },
        new String[] { "302", "Chemistry lab", "Chemical research and student experiments" },
        new String[] { "303", "Biology classroom", "Study of biological specimens and microscopy" },
        new String[] { "304", "Mathematics department", "Advanced mathematics and statistics courses" },
        new String[] { "305", "Language lab", "Foreign language learning with audio equipment" },
        new String[] { "306", "Art studio", "Drawing, painting and creative workshops" },
        new String[] { "307", "Music room", "Music practice and instrument training" });

    List<List<String>> featuresList = Arrays.asList(
        Arrays.asList("Physics", "Experiments", "Equipment"),
        Arrays.asList("Chemistry", "Safety", "Reagents"),
        Arrays.asList("Biology", "Microscopes", "Specimens"),
        Arrays.asList("Math", "Calculators", "Board"),
        Arrays.asList("Languages", "Audio", "Headphones"),
        Arrays.asList("Art", "Easels", "Materials"),
        Arrays.asList("Music", "Piano", "Instruments"));

    int createdCount = 0;

    for (int i = 0; i < cabinetsData.size(); i++) {
      String[] data = cabinetsData.get(i);
      String number = data[0];

      Cabinet existing = cabinetRepository.findByNumber(number);
      if (existing != null) {
        System.out.println("Cabinet " + number + " already exists");
        continue;
      }

      Cabinet cabinet = new Cabinet();
      cabinet.setNumber(number);
      cabinet.setName(data[1]);
      cabinet.setDescription(data[2]);
      cabinet.setFeatures(featuresList.get(i));
      cabinet.setFloor(floor3);

      if (cabinetRepository.save(cabinet)) {
        System.out.println("Created cabinet: " + number);
        createdCount++;
      } else {
        System.out.println("Failed to create cabinet: " + number);
      }
    }

    System.out.println("Cabinets 301-307 created successfully! Created: " + createdCount);
  }

  // Залишаємо старий метод для сумісності
  public static void createCabinetsForFloor3() {
    createFloor3AndCabinets();
  }

  public static void createFloor4AndCabinets() {
    FloorService floorService = new FloorServiceImpl();
    CabinetRepository cabinetRepository = new CabinetRepositoryImpl();

    // Створюємо 4-й поверх якщо не існує
    Floor floor4 = floorService.getFloorByNumber(4);
    if (floor4 == null) {
      System.out.println("Floor 4 not found! Creating floor 4...");
      try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        Transaction transaction = session.beginTransaction();
        floor4 = new Floor();
        floor4.setNumber(4);
        floor4.setName("Fourth floor");
        floor4.setDescription("Fourth floor with sports and recreation facilities");
        session.persist(floor4);
        transaction.commit();
        System.out.println("Floor 4 created successfully!");
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }
      // Отримуємо знову після створення
      floor4 = floorService.getFloorByNumber(4);
    }

    if (floor4 == null) {
      System.out.println("Failed to create or find floor 4!");
      return;
    }

    List<String[]> cabinetsData = Arrays.asList(
        new String[] { "401", "Gymnasium", "Indoor sports and physical education" },
        new String[] { "402", "Fitness center", "Modern fitness equipment and training" },
        new String[] { "403", "Yoga studio", "Meditation and yoga practice space" },
        new String[] { "404", "Table tennis room", "Ping-pong tables and tournaments" },
        new String[] { "405", "Dance hall", "Choreography and dance classes" },
        new String[] { "406", "Martial arts dojo", "Karate, judo and self-defense training" },
        new String[] { "407", "Swimming pool office", "Pool management and lifeguard station" });

    List<List<String>> featuresList = Arrays.asList(
        Arrays.asList("Sports", "Basketball", "Volleyball"),
        Arrays.asList("Fitness", "Equipment", "Training"),
        Arrays.asList("Yoga", "Meditation", "Relaxation"),
        Arrays.asList("Ping-pong", "Tables", "Tournaments"),
        Arrays.asList("Dance", "Choreography", "Mirror"),
        Arrays.asList("Martial arts", "Karate", "Judo"),
        Arrays.asList("Swimming", "Pool", "Lifeguard"));

    int createdCount = 0;

    for (int i = 0; i < cabinetsData.size(); i++) {
      String[] data = cabinetsData.get(i);
      String number = data[0];

      Cabinet existing = cabinetRepository.findByNumber(number);
      if (existing != null) {
        System.out.println("Cabinet " + number + " already exists");
        continue;
      }

      Cabinet cabinet = new Cabinet();
      cabinet.setNumber(number);
      cabinet.setName(data[1]);
      cabinet.setDescription(data[2]);
      cabinet.setFeatures(featuresList.get(i));
      cabinet.setFloor(floor4);

      if (cabinetRepository.save(cabinet)) {
        System.out.println("Created cabinet: " + number);
        createdCount++;
      } else {
        System.out.println("Failed to create cabinet: " + number);
      }
    }

    System.out.println("Cabinets 401-407 created successfully! Created: " + createdCount);
  }

  public static void createFloor5AndCabinets() {
    FloorService floorService = new FloorServiceImpl();
    CabinetRepository cabinetRepository = new CabinetRepositoryImpl();

    // Створюємо 5-й поверх (гуртожиток) якщо не існує
    Floor floor5 = floorService.getFloorByNumber(5);
    if (floor5 == null) {
      System.out.println("Floor 5 not found! Creating floor 5 (Dormitory)...");
      try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        Transaction transaction = session.beginTransaction();
        floor5 = new Floor();
        floor5.setNumber(5);
        floor5.setName("Dormitory floor");
        floor5.setDescription("Student dormitory with living rooms and common areas");
        session.persist(floor5);
        transaction.commit();
        System.out.println("Floor 5 created successfully!");
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }
      // Отримуємо знову після створення
      floor5 = floorService.getFloorByNumber(5);
    }

    if (floor5 == null) {
      System.out.println("Failed to create or find floor 5!");
      return;
    }

    List<String[]> cabinetsData = Arrays.asList(
        new String[] { "501", "Room 501", "Double student room with basic amenities" },
        new String[] { "502", "Room 502", "Double student room with basic amenities" },
        new String[] { "503", "Room 503", "Triple student room for three students" },
        new String[] { "504", "Common kitchen", "Shared kitchen with cooking facilities" },
        new String[] { "505", "Laundry room", "Washing machines and dryers for residents" },
        new String[] { "506", "Study room", "Quiet space for studying and homework" },
        new String[] { "507", "Dormitory office", "Administration and security office" });

    List<List<String>> featuresList = Arrays.asList(
        Arrays.asList("Dormitory", "Bed", "Desk"),
        Arrays.asList("Dormitory", "Bed", "Wardrobe"),
        Arrays.asList("Dormitory", "Beds", "Shared"),
        Arrays.asList("Kitchen", "Stove", "Fridge"),
        Arrays.asList("Laundry", "Washer", "Dryer"),
        Arrays.asList("Study", "Quiet", "WiFi"),
        Arrays.asList("Admin", "Security", "Reception"));

    int createdCount = 0;

    for (int i = 0; i < cabinetsData.size(); i++) {
      String[] data = cabinetsData.get(i);
      String number = data[0];

      Cabinet existing = cabinetRepository.findByNumber(number);
      if (existing != null) {
        System.out.println("Cabinet " + number + " already exists");
        continue;
      }

      Cabinet cabinet = new Cabinet();
      cabinet.setNumber(number);
      cabinet.setName(data[1]);
      cabinet.setDescription(data[2]);
      cabinet.setFeatures(featuresList.get(i));
      cabinet.setFloor(floor5);

      if (cabinetRepository.save(cabinet)) {
        System.out.println("Created cabinet: " + number);
        createdCount++;
      } else {
        System.out.println("Failed to create cabinet: " + number);
      }
    }

    System.out.println("Cabinets 501-507 created successfully! Created: " + createdCount);
  }
}

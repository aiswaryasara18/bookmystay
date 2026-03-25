import java.util.*;

// Main Class
public class HotelBookingSystem {

  // ---------------- ROOM DOMAIN MODEL ----------------
  static class Room {
    private String type;
    private int price;
    private String amenities;

    public Room(String type, int price, String amenities) {
      this.type = type;
      this.price = price;
      this.amenities = amenities;
    }

    public String getType() {
      return type;
    }

    public int getPrice() {
      return price;
    }

    public String getAmenities() {
      return amenities;
    }
  }

  // ---------------- INVENTORY (STATE HOLDER) ----------------
  static class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
      inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
      inventory.put(roomType, count);
    }

    public int getAvailability(String roomType) {
      return inventory.getOrDefault(roomType, 0);
    }

    // Write operation (NOT used in search)
    public boolean updateAvailability(String roomType, int change) {
      int current = inventory.getOrDefault(roomType, 0);

      if (current + change < 0) {
        System.out.println("Not enough rooms available for: " + roomType);
        return false;
      }

      inventory.put(roomType, current + change);
      return true;
    }

    // Read-only access (used in search)
    public Map<String, Integer> getAllAvailability() {
      return Collections.unmodifiableMap(inventory);
    }
  }

  // ---------------- SEARCH SERVICE ----------------
  static class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, Map<String, Room> roomData) {

      System.out.println("\n--- Available Rooms ---");

      // Read-only access
      Map<String, Integer> availability = inventory.getAllAvailability();

      boolean found = false;

      for (String type : availability.keySet()) {

        int count = availability.get(type);

        // Validation: show only available rooms
        if (count > 0 && roomData.containsKey(type)) {
          Room room = roomData.get(type);

          System.out.println("Room Type : " + room.getType());
          System.out.println("Price     : ₹" + room.getPrice());
          System.out.println("Amenities : " + room.getAmenities());
          System.out.println("Available : " + count);
          System.out.println("-------------------------");

          found = true;
        }
      }

      if (!found) {
        System.out.println("No rooms available.");
      }
    }
  }

  // ---------------- MAIN METHOD ----------------
  public static void main(String[] args) {

    // Initialize Inventory
    RoomInventory inventory = new RoomInventory();

    inventory.addRoomType("Single", 10);
    inventory.addRoomType("Double", 0);   // Not available
    inventory.addRoomType("Suite", 2);

    // Room Domain Data
    Map<String, Room> roomData = new HashMap<>();

    roomData.put("Single", new Room("Single", 2000, "AC, WiFi"));
    roomData.put("Double", new Room("Double", 3500, "AC, WiFi, TV"));
    roomData.put("Suite", new Room("Suite", 6000, "AC, WiFi, TV, Mini Bar"));

    // Create Search Service
    SearchService searchService = new SearchService();

    // Guest searches rooms (READ-ONLY OPERATION)
    searchService.searchAvailableRooms(inventory, roomData);

    // Verify inventory unchanged
    System.out.println("\nInventory after search (unchanged):");
    System.out.println(inventory.getAllAvailability());
  }
}
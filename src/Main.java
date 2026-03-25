import java.util.HashMap;
import java.util.Map;

public class HotelBookingSystem {

  // Inner class: RoomInventory
  static class RoomInventory {

    // Centralized storage using HashMap
    private Map<String, Integer> inventory;

    // Constructor
    public RoomInventory() {
      inventory = new HashMap<>();
    }

    // Initialize room types
    public void addRoomType(String roomType, int count) {
      inventory.put(roomType, count);
    }

    // Get availability
    public int getAvailability(String roomType) {
      return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (booking = negative, cancellation = positive)
    public boolean updateAvailability(String roomType, int change) {
      int current = inventory.getOrDefault(roomType, 0);

      if (current + change < 0) {
        System.out.println("Not enough rooms available for: " + roomType);
        return false;
      }

      inventory.put(roomType, current + change);
      return true;
    }

    // Display all inventory
    public void displayInventory() {
      System.out.println("\n--- Current Room Inventory ---");
      for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
        System.out.println(entry.getKey() + " : " + entry.getValue());
      }
    }
  }

  // Main method
  public static void main(String[] args) {

    // Step 1: Initialize inventory
    RoomInventory inventory = new RoomInventory();

    // Step 2: Register room types
    inventory.addRoomType("Single", 10);
    inventory.addRoomType("Double", 5);
    inventory.addRoomType("Suite", 2);

    // Step 3: Display initial inventory
    inventory.displayInventory();

    // Step 4: Booking rooms
    System.out.println("\nBooking 2 Double rooms...");
    inventory.updateAvailability("Double", -2);

    // Step 5: Cancelling rooms
    System.out.println("\nCancelling 1 Single room...");
    inventory.updateAvailability("Single", 1);

    // Step 6: Check availability
    System.out.println("\nAvailable Suites: " +
            inventory.getAvailability("Suite"));

    // Step 7: Final inventory
    inventory.displayInventory();
  }
}
import java.util.*;

// Main Class
public class HotelBookingSystem {

  // ---------------- ROOM DOMAIN MODEL ----------------
  static class Room {
    private String type;
    private int price;

    public Room(String type, int price) {
      this.type = type;
      this.price = price;
    }

    public String getType() {
      return type;
    }

    public int getPrice() {
      return price;
    }
  }

  // ---------------- INVENTORY ----------------
  static class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
      inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
      inventory.put(type, count);
    }

    public int getAvailability(String type) {
      return inventory.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) {
      inventory.put(type, inventory.get(type) - 1);
    }

    public Map<String, Integer> getAllAvailability() {
      return Collections.unmodifiableMap(inventory);
    }
  }

  // ---------------- RESERVATION ----------------
  static class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
      this.guestName = guestName;
      this.roomType = roomType;
    }

    public String getGuestName() {
      return guestName;
    }

    public String getRoomType() {
      return roomType;
    }
  }

  // ---------------- BOOKING QUEUE (FIFO) ----------------
  static class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
      queue.offer(r);
    }

    public Reservation getNextRequest() {
      return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
      return queue.isEmpty();
    }
  }

  // ---------------- BOOKING SERVICE (ALLOCATION) ----------------
  static class BookingService {

    // Track all allocated room IDs (GLOBAL uniqueness)
    private Set<String> allAllocatedRooms = new HashSet<>();

    // Map room type → allocated room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    // Room ID generator counter
    private int idCounter = 1;

    public void processBookings(BookingQueue queue, RoomInventory inventory) {

      while (!queue.isEmpty()) {

        Reservation req = queue.getNextRequest();

        String type = req.getRoomType();
        String guest = req.getGuestName();

        System.out.println("\nProcessing request for " + guest + " (" + type + ")");

        // Check availability
        if (inventory.getAvailability(type) <= 0) {
          System.out.println("Booking failed: No rooms available for " + type);
          continue;
        }

        // Generate unique room ID
        String roomId;
        do {
          roomId = type.substring(0, 1).toUpperCase() + idCounter++;
        } while (allAllocatedRooms.contains(roomId));

        // Add to global set
        allAllocatedRooms.add(roomId);

        // Add to type-specific allocation
        roomAllocations.putIfAbsent(type, new HashSet<>());
        roomAllocations.get(type).add(roomId);

        // Update inventory immediately (atomic step)
        inventory.reduceRoom(type);

        // Confirm booking
        System.out.println("Booking CONFIRMED for " + guest);
        System.out.println("Assigned Room ID: " + roomId);
      }
    }

    public void displayAllocations() {
      System.out.println("\n--- Room Allocations ---");
      for (String type : roomAllocations.keySet()) {
        System.out.println(type + " → " + roomAllocations.get(type));
      }
    }
  }

  // ---------------- MAIN METHOD ----------------
  public static void main(String[] args) {

    // Step 1: Inventory setup
    RoomInventory inventory = new RoomInventory();
    inventory.addRoomType("Single", 2);
    inventory.addRoomType("Double", 1);

    // Step 2: Booking queue
    BookingQueue queue = new BookingQueue();
    queue.addRequest(new Reservation("Aiswarya", "Single"));
    queue.addRequest(new Reservation("Rahul", "Single"));
    queue.addRequest(new Reservation("Priya", "Single")); // exceeds availability
    queue.addRequest(new Reservation("Kiran", "Double"));

    // Step 3: Booking service
    BookingService service = new BookingService();

    // Step 4: Process bookings (FIFO)
    service.processBookings(queue, inventory);

    // Step 5: Show allocations
    service.displayAllocations();

    // Step 6: Final inventory
    System.out.println("\nFinal Inventory:");
    System.out.println(inventory.getAllAvailability());
  }
}
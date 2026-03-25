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

  // ---------------- INVENTORY ----------------
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

    // Read-only access
    public Map<String, Integer> getAllAvailability() {
      return Collections.unmodifiableMap(inventory);
    }
  }

  // ---------------- RESERVATION (BOOKING REQUEST) ----------------
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

    @Override
    public String toString() {
      return "Guest: " + guestName + ", Room Type: " + roomType;
    }
  }

  // ---------------- BOOKING REQUEST QUEUE ----------------
  static class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
      queue = new LinkedList<>();
    }

    // Add booking request (FIFO)
    public void addRequest(Reservation reservation) {
      queue.offer(reservation);
      System.out.println("Request added: " + reservation);
    }

    // View all requests (without removing)
    public void displayQueue() {
      System.out.println("\n--- Booking Request Queue (FIFO Order) ---");

      if (queue.isEmpty()) {
        System.out.println("No booking requests.");
        return;
      }

      for (Reservation r : queue) {
        System.out.println(r);
      }
    }

    // Peek next request (no removal)
    public Reservation peekNext() {
      return queue.peek();
    }
  }

  // ---------------- MAIN METHOD ----------------
  public static void main(String[] args) {

    // Initialize inventory (unchanged in this use case)
    RoomInventory inventory = new RoomInventory();
    inventory.addRoomType("Single", 10);
    inventory.addRoomType("Double", 5);
    inventory.addRoomType("Suite", 2);

    // Create booking queue
    BookingQueue bookingQueue = new BookingQueue();

    // Guests submit booking requests
    bookingQueue.addRequest(new Reservation("Aiswarya", "Single"));
    bookingQueue.addRequest(new Reservation("Rahul", "Double"));
    bookingQueue.addRequest(new Reservation("Priya", "Suite"));
    bookingQueue.addRequest(new Reservation("Kiran", "Single"));

    // Display queue (FIFO order preserved)
    bookingQueue.displayQueue();

    // Show next request to be processed
    System.out.println("\nNext request to process:");
    System.out.println(bookingQueue.peekNext());

    // Verify inventory is NOT modified
    System.out.println("\nInventory remains unchanged:");
    System.out.println(inventory.getAllAvailability());
  }
}
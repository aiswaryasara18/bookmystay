import java.util.*;

// Main Class
public class HotelBookingSystem {

  // ---------------- INVENTORY ----------------
  static class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

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
    private String reservationId;

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

    public void setReservationId(String id) {
      this.reservationId = id;
    }

    public String getReservationId() {
      return reservationId;
    }
  }

  // ---------------- QUEUE ----------------
  static class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
      queue.offer(r);
    }

    public Reservation getNext() {
      return queue.poll();
    }

    public boolean isEmpty() {
      return queue.isEmpty();
    }
  }

  // ---------------- BOOKING SERVICE ----------------
  static class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomAllocations = new HashMap<>();
    private int idCounter = 1;

    public void processBookings(BookingQueue queue, RoomInventory inventory,
                                List<Reservation> confirmedReservations) {

      while (!queue.isEmpty()) {

        Reservation r = queue.getNext();
        String type = r.getRoomType();

        if (inventory.getAvailability(type) <= 0) {
          System.out.println("Booking failed for " + r.getGuestName());
          continue;
        }

        // Generate unique room ID
        String roomId;
        do {
          roomId = type.charAt(0) + "" + idCounter++;
        } while (allocatedRoomIds.contains(roomId));

        allocatedRoomIds.add(roomId);

        roomAllocations.putIfAbsent(type, new HashSet<>());
        roomAllocations.get(type).add(roomId);

        // Assign reservation ID
        r.setReservationId("RES" + roomId);

        // Update inventory
        inventory.reduceRoom(type);

        confirmedReservations.add(r);

        System.out.println("Booking Confirmed → " + r.getGuestName() +
                " | Room ID: " + roomId +
                " | Reservation ID: " + r.getReservationId());
      }
    }
  }

  // ---------------- ADD-ON SERVICE ----------------
  static class Service {
    private String name;
    private int cost;

    public Service(String name, int cost) {
      this.name = name;
      this.cost = cost;
    }

    public String getName() {
      return name;
    }

    public int getCost() {
      return cost;
    }
  }

  // ---------------- ADD-ON SERVICE MANAGER ----------------
  static class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<Service>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, Service service) {
      serviceMap.putIfAbsent(reservationId, new ArrayList<>());
      serviceMap.get(reservationId).add(service);

      System.out.println("Added service '" + service.getName() +
              "' to " + reservationId);
    }

    // Calculate total cost of services
    public int calculateServiceCost(String reservationId) {
      int total = 0;

      List<Service> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());

      for (Service s : services) {
        total += s.getCost();
      }

      return total;
    }

    // Display services
    public void displayServices(String reservationId) {
      List<Service> services = serviceMap.get(reservationId);

      if (services == null || services.isEmpty()) {
        System.out.println("No add-on services for " + reservationId);
        return;
      }

      System.out.println("Services for " + reservationId + ":");
      for (Service s : services) {
        System.out.println("- " + s.getName() + " (₹" + s.getCost() + ")");
      }
    }
  }

  // ---------------- MAIN ----------------
  public static void main(String[] args) {

    // Step 1: Inventory
    RoomInventory inventory = new RoomInventory();
    inventory.addRoomType("Single", 2);

    // Step 2: Queue
    BookingQueue queue = new BookingQueue();
    queue.addRequest(new Reservation("Aiswarya", "Single"));
    queue.addRequest(new Reservation("Rahul", "Single"));

    // Step 3: Booking
    BookingService bookingService = new BookingService();
    List<Reservation> confirmed = new ArrayList<>();

    bookingService.processBookings(queue, inventory, confirmed);

    // Step 4: Add-on services
    AddOnServiceManager serviceManager = new AddOnServiceManager();

    // Define services
    Service wifi = new Service("WiFi", 200);
    Service breakfast = new Service("Breakfast", 300);
    Service spa = new Service("Spa", 1000);

    // Attach services to reservations
    for (Reservation r : confirmed) {
      serviceManager.addService(r.getReservationId(), wifi);
      serviceManager.addService(r.getReservationId(), breakfast);
    }

    // Add extra service to first reservation
    if (!confirmed.isEmpty()) {
      serviceManager.addService(confirmed.get(0).getReservationId(), spa);
    }

    // Step 5: Display services and cost
    for (Reservation r : confirmed) {
      System.out.println("\nReservation: " + r.getReservationId());
      serviceManager.displayServices(r.getReservationId());

      int cost = serviceManager.calculateServiceCost(r.getReservationId());
      System.out.println("Total Add-On Cost: ₹" + cost);
    }

    // Step 6: Verify inventory unchanged
    System.out.println("\nFinal Inventory:");
    System.out.println(inventory.getAllAvailability());
  }
}
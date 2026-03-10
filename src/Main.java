

// Abstract class representing a generic Room
abstract class Room {

  protected String type;
  protected int beds;
  protected double price;

  public Room(String type, int beds, double price) {
    this.type = type;
    this.beds = beds;
    this.price = price;
  }

  // Method to display room details
  public void displayDetails() {
    System.out.println("Room Type : " + type);
    System.out.println("Beds      : " + beds);
    System.out.println("Price     : ₹" + price);
  }
}

// Single Room class
class SingleRoom extends Room {

  public SingleRoom() {
    super("Single Room", 1, 2000);
  }
}

// Double Room class
class DoubleRoom extends Room {

  public DoubleRoom() {
    super("Double Room", 2, 3500);
  }
}

// Suite Room class
class SuiteRoom extends Room {

  public SuiteRoom() {
    super("Suite Room", 3, 6000);
  }
}

// Main Application
public class HotelRoomApp {

  public static void main(String[] args) {

    // Static availability variables
    int singleRoomAvailable = 5;
    int doubleRoomAvailable = 3;
    int suiteRoomAvailable = 2;

    // Create room objects (Polymorphism)
    Room single = new SingleRoom();
    Room doubleRoom = new DoubleRoom();
    Room suite = new SuiteRoom();

    System.out.println("===== Hotel Room Availability =====");

    single.displayDetails();
    System.out.println("Available : " + singleRoomAvailable);
    System.out.println();

    doubleRoom.displayDetails();
    System.out.println("Available : " + doubleRoomAvailable);
    System.out.println();

    suite.displayDetails();
    System.out.println("Available : " + suiteRoomAvailable);
    System.out.println();
  }
}

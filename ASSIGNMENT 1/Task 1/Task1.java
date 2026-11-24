
import java.util.Scanner;

// Represents a vehicle
class Vehicle {
    String regNo;
    String vehicleType;

    Vehicle(String regNo, String vehicleType) {
        this.regNo = regNo;
        this.vehicleType = vehicleType;
    }
}

// Represents a parking user
class ParkingUser {
    String userType;

    ParkingUser(String userType) {
        this.userType = userType;
    }
}

// Represents a parking slot
class ParkingSlot {
    int floorNo;
    int slotNo;
    boolean occupied;

    ParkingSlot(int floorNo, int slotNo) {
        this.floorNo = floorNo;
        this.slotNo = slotNo;
        this.occupied = false;
    }
}

// Represents a parking ticket
class ParkingTicket {
    static int ticketCounter = 1000;

    int ticketNo;
    Vehicle vehicle;
    String entryTime;

    ParkingTicket(Vehicle vehicle, String entryTime) {
        ticketNo = ++ticketCounter;
        this.vehicle = vehicle;
        this.entryTime = entryTime;
    }

    void displayTicket() {
        System.out.println("\n----- PARKING TICKET -----");
        System.out.println("Ticket Number : " + ticketNo);
        System.out.println("Vehicle Number: " + vehicle.regNo);
        System.out.println("Vehicle Type  : " + vehicle.vehicleType);
        System.out.println("Entry Time    : " + entryTime);
    }
}

// Represents the parking facility
class ParkingFacility {

    void parkVehicle(Vehicle v, ParkingUser user,
                     ParkingSlot slot, String entryTime) {

        if (slot.occupied) {
            System.out.println("Parking slot is already occupied!");
            return;
        }

        slot.occupied = true;

        System.out.println("\nUser Type     : " + user.userType);
        System.out.println("Floor Number  : " + slot.floorNo);
        System.out.println("Slot Number   : " + slot.slotNo);

        ParkingTicket ticket = new ParkingTicket(v, entryTime);
        ticket.displayTicket();

        System.out.println("Parking Status: Successful");
    }
}

// Main class
public class Task01 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("CAMPUS SMART PARKING SYSTEM");

        System.out.print("Enter Vehicle Registration Number: ");
        String regNo = sc.nextLine();

        System.out.print("Enter Vehicle Type (Bike/Car/EV): ");
        String type = sc.nextLine();

        System.out.print("Enter User Type (Student/Staff/Visitor): ");
        String userType = sc.nextLine();

        System.out.print("Enter Floor Number: ");
        int floor = sc.nextInt();

        System.out.print("Enter Slot Number: ");
        int slotNo = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Entry Time: ");
        String entryTime = sc.nextLine();

        Vehicle vehicle = new Vehicle(regNo, type);
        ParkingUser user = new ParkingUser(userType);
        ParkingSlot slot = new ParkingSlot(floor, slotNo);

        ParkingFacility facility = new ParkingFacility();

        facility.parkVehicle(vehicle, user, slot, entryTime);

        sc.close();
    }
}

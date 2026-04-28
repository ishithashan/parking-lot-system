import java.util.*;

public class ParkingLotSystem {
    static HashMap<Integer, Vehicle> vehicles = new HashMap<>();
    static HashMap<String, Long> rateCard = new HashMap<>();
    static HashMap<Integer, ParkingSlot> slots = new HashMap<>();
    static HashMap<Integer, Ticket> tickets = new HashMap<>();
    static HashMap<Integer, Billing> bills = new HashMap<>();
    static HashMap<Integer, ParkingSlot> allSlots = new HashMap<>();
    static HashMap<Integer, ParkingSlot> vehicleSlotMap = new HashMap<>();
    
    static Scanner sc = new Scanner(System.in);
    static String currentVehicleNo = null;
    static int nextId = 1;
    static int ticketCounter = 1;
    
    public static void main(String[] args) {
        
        rateCard.put("Car", 50L);
        rateCard.put("Bike", 20L);
        rateCard.put("Truck", 100L);
        rateCard.put("Others", 30L);
        
        for (int i = 1; i <= 20; i++) {
            int floor = (i <= 10) ? 1 : 2;
            int row = (i % 5) + 1;
            String type = (i <= 10) ? "Car" : "Bike";
            allSlots.put(i, new ParkingSlot(i, floor, row, type));
        }
        
        while (true) {
            System.out.println("\n===== PARKING LOT MANAGEMENT =====");
            System.out.println("1. Driver Panel");
            System.out.println("2. Admin Panel");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine();
            
            if (choice == 1) driverPanel();
            else if (choice == 2) adminPanel();
            else if (choice == 3) {
                System.out.println("Thank you!");
                break;
            }
            else System.out.println("Invalid choice!");
        }
    }
    
    static void driverPanel() {
        if (currentVehicleNo == null) {
            System.out.println("\nPlease register/login first from Admin Panel!");
            return;
        }
        

        int vehicleId = -1;
        for (Map.Entry<Integer, Vehicle> entry : vehicles.entrySet()) {
            if (entry.getValue().vehicleNo.equals(currentVehicleNo)) {
                vehicleId = entry.getKey();
                break;
            }
        }
        
        while (true) {
            System.out.println("\n--- DRIVER PANEL ---");
            System.out.println("Welcome: " + currentVehicleNo);
            System.out.println("1. View My Parking Slot");
            System.out.println("2. View My Ticket");
            System.out.println("3. View My Bill");
            System.out.println("4. Exit Vehicle (Generate Bill)");
            System.out.println("5. Logout");
            System.out.print("Choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine();
            
            if (choice == 1) {
                viewMySlot(vehicleId);
            } else if (choice == 2) {
                viewMyTicket(vehicleId);
            } else if (choice == 3) {
                viewMyBill(vehicleId);
            } else if (choice == 4) {
                exitVehicle(vehicleId);
            } else if (choice == 5) {
                currentVehicleNo = null;
                System.out.println("Logged out!");
                break;
            }
        }
    }
    
    static void viewMySlot(int vehicleId) {
        if (vehicleSlotMap.containsKey(vehicleId)) {
            ParkingSlot ps = vehicleSlotMap.get(vehicleId);
            System.out.println("\n--- YOUR PARKING SLOT ---");
            System.out.println(ps.getLocation());
            System.out.println("Status: " + (ps.isOccupied ? "OCCUPIED" : "FREE"));
        } else {
            System.out.println("No slot assigned yet! Please contact admin.");
        }
    }
    
    static void viewMyTicket(int vehicleId) {
        if (tickets.containsKey(vehicleId)) {
            Ticket t = tickets.get(vehicleId);
            t.generateTicket();
        } else {
            System.out.println("No ticket generated yet!");
        }
    }
    
    static void viewMyBill(int vehicleId) {
        if (bills.containsKey(vehicleId)) {
            Billing b = bills.get(vehicleId);
            System.out.println("\n--- YOUR BILL ---");
            System.out.println("Total Charge: Rs." + b.getCharge());
        } else {
            System.out.println("No bill generated yet!");
        }
    }
    
    static void exitVehicle(int vehicleId) {
        if (!tickets.containsKey(vehicleId)) {
            System.out.println("No ticket found!");
            return;
        }
        
        Ticket t = tickets.get(vehicleId);
        t.close();
        
        long duration = t.getDuration();
        Vehicle v = vehicles.get(vehicleId);
        
        long hours = (duration / 60) + 1;
        long rate = rateCard.getOrDefault(v.type, 30L);
        long charge = hours * rate;
        if (charge < 10) charge = 10;
        
        Billing bill = new Billing(charge);
        bills.put(vehicleId, bill);
        
        if (vehicleSlotMap.containsKey(vehicleId)) {
            vehicleSlotMap.get(vehicleId).vacate();
        }
        
        System.out.println("\nVEHICLE EXITED");
        System.out.println("Vehicle: " + v.vehicleNo);
        System.out.println("Duration: " + duration + " minutes (" + (duration/60) + " hours)");
        System.out.println("Total Charge: Rs." + charge);
        System.out.println("Exit Time: " + t.exitTime);
        
        tickets.remove(vehicleId);
        vehicleSlotMap.remove(vehicleId);
    }

    static void adminPanel() {
        while (true) {
            System.out.println("\n--- ADMIN PANEL ---");
            System.out.println("1. Register Vehicle");
            System.out.println("2. Assign Parking Slot");
            System.out.println("3. Generate Ticket");
            System.out.println("4. View All Slots");
            System.out.println("5. Check Slot Availability");
            System.out.println("6. View All Vehicles");
            System.out.println("7. Logout");
            System.out.print("Choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine();
            
            if (choice == 1) registerVehicle();
            else if (choice == 2) assignParkingSlot();
            else if (choice == 3) generateTicket();
            else if (choice == 4) viewAllSlots();
            else if (choice == 5) checkAvailability();
            else if (choice == 6) viewAllVehicles();
            else if (choice == 7) break;
            else System.out.println("Invalid choice!");
        }
    }
    
    static void registerVehicle() {
        System.out.println("\n--- REGISTER VEHICLE ---");
        
        System.out.print("Enter Vehicle Number: ");
        String vehicleNo = sc.nextLine();
        
        // Check if already exists
        for (Vehicle v : vehicles.values()) {
            if (v.vehicleNo.equals(vehicleNo)) {
                System.out.println("Vehicle already registered!");
                return;
            }
        }
        
        System.out.println("Choose Vehicle Type:");
        System.out.println("1. Car");
        System.out.println("2. Bike");
        System.out.println("3. Truck");
        System.out.println("4. Others");
        System.out.print("Choice: ");
        int typeno = sc.nextInt();
        sc.nextLine();
        
        String type;
        switch (typeno) {
            case 1: type = "Car"; break;
            case 2: type = "Bike"; break;
            case 3: type = "Truck"; break;
            default: type = "Others";
        }
        
        System.out.print("Enter Owner Name: ");
        String ownerName = sc.nextLine();
        
        Vehicle newVehicle = new Vehicle(vehicleNo, type, ownerName);
        vehicles.put(nextId, newVehicle);
        
        System.out.println("Registration successful!");
        System.out.println("Your Vehicle ID: " + nextId);
        nextId++;
    }
    
    static void assignParkingSlot() {
        System.out.println("\n--- ASSIGN PARKING SLOT ---");
        
        viewAllVehicles();
        
        System.out.print("Enter Vehicle ID to assign slot: ");
        int vehicleId = sc.nextInt();
        sc.nextLine();
        
        
        if (vehicleId == -1) {
            System.out.println("Vehicle not found!");
            return;
        }
        
        Vehicle v = vehicles.get(vehicleId);
    
        ParkingSlot availableSlot = null;
        for (ParkingSlot ps : allSlots.values()) {
            if (!ps.isOccupied && ps.type.equals(v.type)) {
                availableSlot = ps;
                break;
            }
        }
        
        if (availableSlot == null) {
            for (ParkingSlot ps : allSlots.values()) {
                if (!ps.isOccupied) {
                    availableSlot = ps;
                    break;
                }
            }
        }
        
        if (availableSlot == null) {
            System.out.println("No slots available!");
            return;
        }
        
        availableSlot.occupy();
        
        vehicleSlotMap.put(vehicleId, availableSlot);
        
        System.out.println("Slot assigned successfully!");
        System.out.println("Vehicle: " + v.vehicleNo);
        System.out.println("Slot: " + availableSlot.getLocation());
    }
    
    static void generateTicket() {
        System.out.println("\n--- GENERATE TICKET ---");
        
        viewAllVehicles();
        
        System.out.print("Enter Vehicle ID to generate ticket: ");
        int vehicleId = sc.nextInt();
        sc.nextLine();
        
        if (vehicleId == -1) {
            System.out.println("Vehicle not found!");
            return;
        }
        
        if (!vehicleSlotMap.containsKey(vehicleId)) {
            System.out.println("Please assign a parking slot first!");
            return;
        }
        
        if (tickets.containsKey(vehicleId)) {
            System.out.println("Ticket already exists for this vehicle!");
            return;
        }
        
        Vehicle v = vehicles.get(vehicleId);
        ParkingSlot ps = vehicleSlotMap.get(vehicleId);

        Ticket t = new Ticket(ticketCounter++, v.vehicleNo, ps.slotNo, new Date());
        tickets.put(vehicleId, t);
        
        System.out.println("Ticket generated!");
        t.generateTicket();
        
        currentVehicleNo = v.vehicleNo;
    }
    
    static void viewAllSlots() {
        System.out.println("\n=== ALL PARKING SLOTS ===");
        System.out.println("Slot ID | Floor | Row | Type | Status");
        System.out.println("----------------------------------------");
        
        for (ParkingSlot ps : allSlots.values()) {
            System.out.println(ps);
        }
    }
    
    static void checkAvailability() {
        System.out.println("\n--- CHECK AVAILABILITY ---");
        
        System.out.print("Enter Floor Number: ");
        int floor = sc.nextInt();
        System.out.print("Enter Row Number: ");
        int row = sc.nextInt();
        sc.nextLine();
        
        boolean found = false;
        for (ParkingSlot ps : allSlots.values()) {
            if (ps.floorNo == floor && ps.rowNo == row) {
                System.out.println("Slot " + ps.slotNo + " (" + ps.type + "): " + 
                                  (ps.isOccupied ? "OCCUPIED" : "AVAILABLE"));
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No slots found at Floor " + floor + ", Row " + row);
        }
    }
    
    static void viewAllVehicles() {
        System.out.println("\n=== ALL VEHICLES ===");
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles registered!");
            return;
        }
        
        System.out.println("ID | Vehicle No | Type | Owner");
        System.out.println("--------------------------------");
        for (Map.Entry<Integer, Vehicle> entry : vehicles.entrySet()) {
            Vehicle v = entry.getValue();
            System.out.println(entry.getKey() + " | " + v.vehicleNo + " | " + v.type + " | " + v.ownerName);
        }
    }
}

//===============Classes==============
class Vehicle {
    String vehicleNo;
    String type;
    String ownerName;
    
    public Vehicle(String no, String typ, String nm) {
        this.vehicleNo = no;
        this.type = typ;
        this.ownerName = nm;
    }
    
    public String getType() {
        return type;
    }
}

class ParkingSlot {
    int slotNo;
    int floorNo;
    int rowNo;
    String type;
    boolean isOccupied;
    
    public ParkingSlot(int s, int f, int r, String t) {
        this.slotNo = s;
        this.floorNo = f;
        this.rowNo = r;
        this.type = t;
        this.isOccupied = false;
    }
    
    public boolean occupy() {
        if (!isOccupied) {
            this.isOccupied = true;
            return true;
        }
        return false;
    }
    
    public void vacate() {
        this.isOccupied = false;
    }
    
    public String getLocation() {
        return "Floor: " + floorNo + ", Row: " + rowNo + ", Slot: " + slotNo;
    }
    
    public String toString() {
        return "  " + slotNo + "     |   " + floorNo + "   |  " + rowNo + "   | " + type + " | " + (isOccupied ? "OCCUPIED" : "AVAILABLE");
    }
}

class Ticket {
    int ticketId;
    String vehicleNo;
    int slotId;
    Date entryTime;
    Date exitTime;
    
    public Ticket(int id, String vNo, int sId, Date eTime) {
        this.ticketId = id;
        this.vehicleNo = vNo;
        this.slotId = sId;
        this.entryTime = eTime;
        this.exitTime = null;
    }
    
    public void generateTicket() {
        System.out.println("\n--- TICKET DETAILS ---");
        System.out.println("Ticket ID: " + ticketId);
        System.out.println("Vehicle No: " + vehicleNo);
        System.out.println("Slot ID: " + slotId);
        System.out.println("Entry Time: " + entryTime);
        System.out.println("Status: ACTIVE");
    }
    
    public void close() {
        this.exitTime = new Date();
    }
    
    public long getDuration() {
        if (exitTime == null) {
            return 0;
        }
        // Duration in minutes
        return (exitTime.getTime() - entryTime.getTime()) / (1000 * 60);
    }
}

class Billing {
    long charge;
    
    public Billing(long charge) {
        this.charge = charge;
    }
    
    public long getCharge() {
        return charge;
    }
    
    public void computeCharge(long duration, String type, HashMap<String, Long> rateCard) {
        charge = duration * rateCard.get(type);
        if (charge < 10) charge = 10;
    }
}
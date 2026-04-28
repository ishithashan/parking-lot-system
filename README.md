# Parking Lot Management System

## Problem Statement

Design a system to manage vehicles entering and exiting a multi-floor parking lot.
The system should handle:

* Vehicle registration
* Slot allocation based on vehicle type
* Ticket generation at entry
* Billing calculation at exit
* Slot occupancy tracking

This project implements **UC-007: Parking Lot Management**.

---

## Objectives

* Efficiently allocate parking slots
* Track vehicle entry and exit
* Calculate parking charges based on duration
* Maintain real-time slot occupancy

---

## Approach / Logic Used

The system is implemented using **Object-Oriented Programming (OOP)** principles in Java.

### Core Classes

* **Vehicle** → Stores vehicle details
* **ParkingSlot** → Represents slot (floor, row, type, occupancy)
* **Ticket** → Stores entry/exit timestamps
* **Billing** → Handles charge calculation
* **ParkingLotSystem (Main)** → Controls application flow

---

### Data Structures Used

* `HashMap<Integer, Vehicle>` → Stores vehicles
* `HashMap<Integer, ParkingSlot>` → Stores all slots
* `HashMap<Integer, ParkingSlot>` → Maps vehicle → slot
* `HashMap<Integer, Ticket>` → Tracks active tickets
* `HashMap<Integer, Billing>` → Stores billing records

---

### System Workflow

1. **Vehicle Entry**

   * Admin registers vehicle
   * System assigns nearest available slot based on type

2. **Slot Allocation**

   * Matches vehicle type (Car/Bike/Truck)
   * If unavailable → assigns alternate slot

3. **Ticket Generation**

   * Entry timestamp recorded
   * Unique ticket ID generated

4. **Vehicle Exit**

   * Exit time captured
   * Duration calculated

5. **Billing**

   * Charges computed per hour
   * Minimum charge applied
   * Slot marked as available again

---

## Steps to Execute the Code

### 1. Compile

```bash
javac ParkingLotSystem.java
```

### 2. Run

```bash
java ParkingLotSystem
```

### 3. Use Menu

* Admin Panel → Register, Assign Slot, Generate Ticket
* Driver Panel → View Slot, Ticket, Bill, Exit

---

## Billing Logic

* Charges are calculated per hour
* Formula:
  Duration (minutes) → converted to hours
* Minimum 1 hour charge is applied

---

## Features

✔ Multi-floor parking system
✔ Slot allocation based on vehicle type
✔ Ticket generation with timestamp
✔ Billing system
✔ Slot occupancy tracking
✔ Admin & Driver interface

---

## Edge Cases Handled

* No slot available → "Lot Full"
* No ticket found → error handling
* Minimum billing applied even for short duration

---

## Technologies Used

* Java
* OOP Concepts
* HashMap (In-memory storage)

---

## Future Enhancements

* Database integration (MySQL)
* Online payment system
* Monthly pass feature
* Lost ticket handling
* Real-time dashboard

---

## Author

* Ishitha S

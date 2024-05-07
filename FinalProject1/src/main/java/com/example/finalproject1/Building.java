package com.example.finalproject1;

import java.util.ArrayList;
import java.util.List;

public class Building {
    private final int floors; // Total number of floors in the building
    private final List<Passenger> passengersOnFloors; // List of passengers waiting on floors

    // Constructor
    public Building(int floors) {
        this.floors = floors;
        this.passengersOnFloors = new ArrayList<>(); // Initialize list of passengers
    }

    // Getter method for floors
    public int getFloors() { return floors; }

    // Method to add a passenger to a specific floor
    public void addPassenger(Passenger passenger, int floor) {
        if (floor >= 0 && floor <= floors) {
            passengersOnFloors.add(passenger);
        } else {
            System.out.println("Invalid floor number");
        }
    }

    // Method to count the total number of passengers waiting in the building
    public int countPassengers() {
        return passengersOnFloors.size();
    }

    // Method to remove passengers who have reached their destination
    public void removePassengersThatAreDone() {
        for (int i = passengersOnFloors.size() - 1; i >= 0; i--) {
            Passenger passenger = passengersOnFloors.get(i);
            if (passenger.getDone())
                passengersOnFloors.remove(i);
        }
    }

    // Method to get passengers waiting on a specific floor
    public List<Passenger> getPassengersOnFloor(int floor) {
        List<Passenger> passengersOnFloor = new ArrayList<>();
        for (Passenger passenger : passengersOnFloors) {
            if (passenger.getStartingFloor() == floor) {
                passengersOnFloor.add(passenger);
            }
        }
        return passengersOnFloor;
    }

    // Method to count the number of patient passengers waiting on a specific floor
    public int getPatientPassangerCountOnFloor(int floor) {
        int count = 0;
        for (Passenger passenger : passengersOnFloors) {
            if (passenger.getStartingFloor() == floor && passenger.passengerType == Passenger.PassengerType.Patient) {
                count++;
            }
        }
        return count;
    }

    // Method to count the number of staff passengers waiting on a specific floor
    public int getStaffPassangerCountOnFloor(int floor) {
        int count = 0;
        for (Passenger passenger : passengersOnFloors) {
            if (passenger.getStartingFloor() == floor && passenger.passengerType == Passenger.PassengerType.Staff) {
                count++;
            }
        }
        return count;
    }

    // Method to generate elevator requests from passengers waiting in the building
    public List<ElevatorRequest> getElevatorRequests() {
        List<ElevatorRequest> elevatorRequests = new ArrayList<>();
        for (Passenger passenger : passengersOnFloors) {
            ElevatorRequest r = passenger.requestElevator();
            if (r == null) continue;

            elevatorRequests.add(passenger.requestElevator());
        }
        return elevatorRequests;
    }
}

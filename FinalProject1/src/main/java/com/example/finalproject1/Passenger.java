package com.example.finalproject1;

public abstract class Passenger {
    protected final int startingFloor; // Starting floor of the passenger
    protected final int destinationFloor; // Destination floor of the passenger
    protected final PassengerType passengerType; // Type of passenger (Patient or Staff)
    protected boolean isinElevator; // Indicates whether the passenger is inside an elevator
    protected boolean done; // Indicates whether the passenger has reached the destination

    // Constructor
    public Passenger(int startingFloor, int destinationFloor, PassengerType passengerType) {
        this.startingFloor = startingFloor;
        this.destinationFloor = destinationFloor;
        this.isinElevator = false; // Initially, the passenger is not in an elevator
        this.passengerType = passengerType; // Set the type of passenger
    }

    // Getter methods
    public int getStartingFloor() {
        return startingFloor;
    }
    public int getDestinationFloor() {
        return destinationFloor;
    }

    public boolean getIsinElevator() { return isinElevator;  }
    public void setIsinElevator(boolean isinElevator) { this.isinElevator = isinElevator;  }

    public boolean getDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

    // Method to determine the direction of the passenger's travel
    public int direction() { return destinationFloor - startingFloor < 0 ? -1 : 1; }

    // Abstract method to request an elevator
    public abstract ElevatorRequest requestElevator();

    // Enumeration for passenger types
    public enum PassengerType {
        Patient,
        Staff
    }
}

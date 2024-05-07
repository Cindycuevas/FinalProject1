package com.example.finalproject1;

public class StaffPassenger extends Passenger {

    // Constructor
    public StaffPassenger(int startingFloor, int destinationFloor) {
        // Call the constructor of the superclass (Passenger)
        super(startingFloor, destinationFloor, PassengerType.Staff);
    }

    // Override method to request an elevator
    @Override
    public ElevatorRequest requestElevator() {
        // Check if the passenger is done or already in an elevator
        if (done || isinElevator) return null;

        // Return an elevator request for an Express type elevator
        return new ElevatorRequest(Elevator.ElevatorType.Express, startingFloor, direction());
    }
}

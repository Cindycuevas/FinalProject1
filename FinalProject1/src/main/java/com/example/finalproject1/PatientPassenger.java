package com.example.finalproject1;

public class PatientPassenger extends Passenger {

    // Constructor
    public PatientPassenger(int startingFloor, int destinationFloor) {

        // Call the constructor of the superclass (Passenger)
        super(startingFloor, destinationFloor, PassengerType.Patient);
    }

    // Override method to request an elevator
    @Override
    public ElevatorRequest  requestElevator() {
        // Check if the passenger is done or already in an elevator
        if (done || isinElevator) return null;

        // Return an elevator request for a Standard type elevator
        return new ElevatorRequest(Elevator.ElevatorType.Standard, startingFloor, direction());
    }
}

package com.example.finalproject1;

import javafx.util.Pair;

import java.util.Collections;
import java.util.List;

// StandardElevator class representing a standard type of elevator
public class StandardElevator extends Elevator {

    // Constructor for StandardElevator
    public StandardElevator(int capacity) {
        // Call the superclass constructor with the specified capacity and ElevatorType
        super(capacity, ElevatorType.Standard);
    }

    // Method to accept passengers onto the elevator
    @Override
    public Pair<Integer, Integer> acceptPassengers(List<Passenger> passengers) {
        if (isFull())
        {
            // If the elevator is full, print a message and return null
            System.out.println("  " + getType() + " elevator is full");
            return null;
        }

        // Shuffle the list of passengers to simulate random boarding order
        Collections.shuffle(passengers);
        int counter = 0;
        int patientCounter = 0;
        int staffCounter = 0;

        // Loop through the list of passengers
        for (Passenger passenger : passengers) {
            if (isFull()) break; // If elevator is full, exit loop
            if (passenger.getDone()) continue; // If passenger is done, skip
            if (passenger.getIsinElevator()) continue; // If passenger is already in elevator, skip

            // If passenger's direction matches elevator's direction or elevator is empty, accept passenger
            if (passenger.direction() != direction) {
                if (isEmpty()) direction = passenger.direction();
                else continue;
            }

            // Increment appropriate counters based on passenger type
            if (passenger instanceof StaffPassenger) staffCounter++;
            else patientCounter++;

            // Add passenger to the elevator and update counters
            curPassengers.add(passenger);
            passenger.setIsinElevator(true);
            counter++;
        }

        // Print the number of passengers accepted by the elevator
        System.out.println("  " + getType() + " elevator is accepted " + counter + " passengers");

        // Return a Pair containing the count of staff and patient passengers accepted
        return new Pair<>(staffCounter, patientCounter);
    }

    // Method to move the elevator based on elevator requests
    @Override
    public void move(List<ElevatorRequest> elevatorRequests) {
        // Implementation of elevator movement logic goes here
        // This method determines the next floor the elevator should move to based on requests
        int closestFloor = 10000;
        ElevatorRequest acceptedElevatorRequest = null;

        // If the elevator is full, prioritize dropping off passengers
        if (isFull()) {
            for (Passenger passenger : curPassengers) {
                if (Math.abs(currentFloor - passenger.getDestinationFloor()) <= Math.abs(currentFloor - closestFloor)) {
                    closestFloor = passenger.getDestinationFloor();
                }
            }
        }
        else {
            // Iterate through elevator requests to determine the next floor to visit
            for (ElevatorRequest elevatorRequest : elevatorRequests) {
                if (elevatorRequest.done) continue; // If request is done, skip

                // If request direction doesn't match elevator's direction and elevator is not empty, skip
                if (elevatorRequest.direction != direction && !isEmpty()) continue;

                // Update closest floor if the current request is closer
                if (Math.abs(currentFloor - elevatorRequest.floor) <= Math.abs(currentFloor - closestFloor))
                {
                    closestFloor = elevatorRequest.floor;
                    acceptedElevatorRequest = elevatorRequest;
                }
            }

            // Mark the accepted elevator request as done
            if (acceptedElevatorRequest != null) {
                acceptedElevatorRequest.done = true;
            }

            // If elevator is empty and no requests, stay at the current floor
            if (!isEmpty()) {
                for (Passenger passenger : curPassengers) {
                    if (Math.abs(currentFloor - passenger.getDestinationFloor()) <= Math.abs(currentFloor - closestFloor)) {
                        closestFloor = passenger.getDestinationFloor();
                    }
                }
            }
            else if (elevatorRequests.isEmpty()) {
                System.out.println("  " + getType() + " elevator is moving staying at the same floor " + currentFloor);
                return;
            }
        }

        // Move the elevator to the closest floor determined
        System.out.println("  " + getType() + " elevator is moving to " + closestFloor);
        currentFloor = closestFloor;
    }
}

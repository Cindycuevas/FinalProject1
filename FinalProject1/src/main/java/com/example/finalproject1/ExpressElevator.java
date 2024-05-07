package com.example.finalproject1;

import javafx.util.Pair;

import java.util.List;

public class ExpressElevator extends Elevator {

    // Constructor
    public ExpressElevator(int capacity) {
        super(capacity, ElevatorType.Express);
    }

    // Override acceptPassengers method
    @Override
    public Pair<Integer, Integer> acceptPassengers(List<Passenger> passengers) {
        if (isFull())
        {
            System.out.println("  " + getType() + " elevator is full");
            return null;
        }

        int counter = 0;
        int patientCounter = 0;
        int staffCounter = 0;

        // Prioritize accepting staff passengers
        for (Passenger passenger : passengers) {
            if (isFull()) break;
            if (passenger.getDone()) continue;
            if (passenger.getIsinElevator()) continue;

            if (passenger instanceof StaffPassenger) {
                if (passenger.direction() != direction) {
                    if (isEmpty()) direction = passenger.direction();
                    else continue;
                }

                curPassengers.add(passenger);
                passenger.setIsinElevator(true);
                counter++;
                staffCounter++;
            }
        }

        // then allow Patients
        for (Passenger passenger : passengers) {
            if (isFull()) break;
            if (passenger.getDone()) continue;
            if (passenger.getIsinElevator()) continue;

            if (passenger instanceof PatientPassenger) {
                if (passenger.direction() != direction) {
                    if (isEmpty()) direction = passenger.direction();
                    else continue;
                }

                curPassengers.add(passenger);
                passenger.setIsinElevator(true);
                counter++;
                patientCounter++;
            }
        }

        System.out.println("  " + getType() + " elevator is accepted " + counter + " passengers");
        return new Pair<>(staffCounter, patientCounter);
    }

    // Override move method
    @Override
    public void move(List<ElevatorRequest> elevatorRequests) {
        int closestFloor = 10000;
        ElevatorRequest acceptedElevatorRequest = null;
        int count = 0;

        if (isFull()) {
            // Find closest destination floor for full elevator
            for (Passenger passenger : curPassengers) {
                if (Math.abs(currentFloor - passenger.getDestinationFloor()) < Math.abs(currentFloor - closestFloor)) {
                    closestFloor = passenger.getDestinationFloor();
                }
            }
        } else {

            // Find closest floor based on elevator requests
            for (ElevatorRequest elevatorRequest : elevatorRequests) {
                if (elevatorRequest.done) continue;
                if (elevatorRequest.elevatorType != ElevatorType.Express) continue;
                if (elevatorRequest.direction != direction && !isEmpty()) continue;

                if (Math.abs(currentFloor - elevatorRequest.floor) < Math.abs(currentFloor - closestFloor))
                {
                    closestFloor = elevatorRequest.floor;
                    acceptedElevatorRequest = elevatorRequest;
                    count++;
                }
            }

            // Mark accepted request as done
            if (acceptedElevatorRequest != null) {
                acceptedElevatorRequest.done = true;
            }

            // If elevator is empty and no requests are accepted, stay at the same floor
            if (!isEmpty()) {
                for (Passenger passenger : curPassengers) {
                    if (Math.abs(currentFloor - passenger.getDestinationFloor()) < Math.abs(currentFloor - closestFloor)) {
                        closestFloor = passenger.getDestinationFloor();
                    }
                }
            }
            else if (count == 0) {
                System.out.println("  " + getType() + " elevator is moving staying at the same floor " + currentFloor);
                return;
            }
        }

        // Move elevator to the closest floor
        System.out.println("  " + getType() + " elevator is moving to " + closestFloor);
        currentFloor = closestFloor;
    }
}

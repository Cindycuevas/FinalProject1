package com.example.finalproject1;

import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Create a building with 30 floors
        Building building = new Building(30);

        // Add 500 random passengers to the building
        addRandomPassengers(building, 500);

        // Create instances of two types of elevators: StandardElevator and ExpressElevator
        Elevator standardElevator = new StandardElevator(10);
        Elevator expressElevator = new ExpressElevator(8);

        // Start the main simulation loop
        runMain(building, standardElevator, expressElevator, 30);
    }

    // Main simulation loop
    private static void runMain(Building building, Elevator standardElevator, Elevator expressElevator, int iterations) {
        int count = 1;

        // Continue simulation until there are no passengers left in the building
        while (building.countPassengers() > 0) {
            System.out.println("----Iteration " + count + " started ----");
            System.out.println("Total number of passengers in the building: " + building.countPassengers());

            // Stop both elevators at their current floors
            standardElevator.stop();
            expressElevator.stop();

            // Remove passengers who have reached their destinations from the building
            building.removePassengersThatAreDone();

            // Let elevators accept passengers waiting on their current floors
            standardElevator.acceptPassengers(building.getPassengersOnFloor(standardElevator.getCurrentFloor()));
            expressElevator.acceptPassengers(building.getPassengersOnFloor(expressElevator.getCurrentFloor()));

            // Get elevator requests from the building and move both elevators accordingly
            List<ElevatorRequest> requests = building.getElevatorRequests();
            standardElevator.move(requests);
            expressElevator.move(requests);

            System.out.println("----Iteration " + count + " completed ----");
            count++;
        }
    }

    // Method to add random passengers to the building
    private  static void addRandomPassengers(Building building, int count) {
        for (int i = 0; i < count; i++) {

            // Randomly determine if the passenger is staff (30% chance) or patient (70% chance)
            boolean staff = Math.random() < 0.3;
            int randomStartFloor = 0;
            int randomDestinationFloor = 0;

            // Ensure that randomStartFloor is not equal to randomDestinationFloor
            while (randomStartFloor == randomDestinationFloor) {
                randomStartFloor = new Random().nextInt(1, building.getFloors());
                randomDestinationFloor = new Random().nextInt(1, building.getFloors());
            }

            // Create a passenger based on the random type (staff or patient)
            Passenger passenger;
            if (staff) {
                passenger = new StaffPassenger(randomStartFloor, randomDestinationFloor);
            } else {
                passenger = new PatientPassenger(randomStartFloor, randomDestinationFloor);
            }

            // Add the passenger to the building at their starting floor
            building.addPassenger(passenger, passenger.startingFloor);
        }
    }
}
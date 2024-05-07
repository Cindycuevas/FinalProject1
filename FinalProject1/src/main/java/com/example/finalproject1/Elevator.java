package com.example.finalproject1;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class Elevator {
    protected int currentFloor; // Current floor of the elevator
    protected int direction; // Direction of movement (up or down)
    protected int capacity; // Maximum capacity of the elevator
    protected ElevatorType elevatorType; // Type of elevator (Standard or Express)
    public List<Passenger> curPassengers; // List of passengers currently in the elevator

    // Constructor
    public Elevator(int capacity, ElevatorType elevatorType) {
        this.capacity = capacity;
        this.currentFloor = 1; // Elevator starts at floor 1 by default
        this.direction = 1; // Elevator starts moving up by default
        this.curPassengers = new ArrayList<>(); // Initialize the list of passengers
        this.elevatorType = elevatorType; // Set the type of elevator
    }

    // Abstract method to accept passengers
    public abstract Pair<Integer, Integer> acceptPassengers(List<Passenger> passengers);

    // Abstract method to move the elevator
    public abstract void move(List<ElevatorRequest> elevatorRequests);

    // Method to stop the elevator at a floor
    public void stop() {
        int count = 0;
        for (int i = curPassengers.size() - 1; i >= 0; i--) {
            Passenger passenger = curPassengers.get(i);
            if (passenger.getDestinationFloor() == currentFloor) {
                passenger.setIsinElevator(false);
                passenger.setDone(true);
                count++;
                curPassengers.remove(i);
            }
        }
        System.out.println("  " + getType() + " elevator stopped at " + currentFloor + ": " + count + " passengers left");
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getCapacity() {
        return capacity;
    }

    // Method to check if the elevator is full
    public boolean isFull() {
        return curPassengers.size() >= capacity;
    }

    // Method to check if the elevator is empty
    public boolean isEmpty() {
        return curPassengers.isEmpty();
    }

    // Method to add a passenger to the elevator
    public void addPassenger(Passenger passenger) {
        if (!isFull()) {
            curPassengers.add(passenger);
            System.out.println(passenger.getClass().getSimpleName() + " added to elevator at floor " + currentFloor);
        } else {
            System.out.println("Elevator is full, cannot add passenger");
        }
    }

    // Method to remove a passenger from the elevator
    public void removePassenger(Passenger passenger) {
        curPassengers.remove(passenger);
        System.out.println(passenger.getClass().getSimpleName() + " removed from elevator at floor " + currentFloor);
    }

    // Method to get the type of elevator (Standard or Express)
    protected String getType() {
        return elevatorType == ElevatorType.Standard ? "Standard" : "Express";
    }

    // Enumeration for elevator types
    public enum ElevatorType {
        Standard,
        Express
    }
}

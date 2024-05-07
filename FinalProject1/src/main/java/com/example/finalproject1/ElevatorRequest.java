package com.example.finalproject1;

public class ElevatorRequest {
    public final Elevator.ElevatorType elevatorType; // Type of elevator requested
    public final int floor; // Floor where the request is made
    public final int direction; // Direction of the request (up or down)
    public boolean done; // Indicates whether the request has been serviced

    // Constructor
    public ElevatorRequest(Elevator.ElevatorType elevatorType, int floor, int direction) {
        this.elevatorType = elevatorType;
        this.floor = floor;
        this.direction = direction;
        done = false; // Initially, the request is not serviced
    }
}

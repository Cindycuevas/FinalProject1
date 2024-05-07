package com.example.finalproject1;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BuildingScene extends Application {
    int w = 500; // Width of the building scene
    int h = 750; // Height of the building scene
    ArrayList<FloorScene> floors; // List to store floor scenes
    int maxFloor = 30; // Maximum number of floors in the building
    int floorH; // Height of each floor
    int floorW; // Width of each floor

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World");
        Group root = new Group();
        Scene scene = new Scene(root, w, h);
        Button btn = new Button("Start");
        btn.setLayoutX(w / 2 - 100);
        btn.setLayoutY(h / 2 - 35);
        btn.setPrefWidth(200);
        btn.setPrefHeight(70);

        int elevatorW = 100;
        floorH = h / maxFloor;
        floorW = w - elevatorW;

        floors = new ArrayList<>(Collections.nCopies(maxFloor, null));

        Building building = new Building(maxFloor);
        addRandomPassengers(building, 300); // TOTAL random passengers to building
        Elevator standardElevator = new StandardElevator(10);
        Elevator expressElevator = new ExpressElevator(8);

        Rectangle rect1 = new Rectangle(2, 2, elevatorW, h - 4);
        rect1.setFill(Color.TRANSPARENT);
        rect1.setStroke(Color.BLACK);
        rect1.setStrokeWidth(2);
        root.getChildren().add(rect1);

        ElevatorScene expressElevatorScene = new ElevatorScene(2, getFloorY(1), elevatorW / 2 - 2, floorH);
        expressElevatorScene.Show(root, Color.RED);
        expressElevatorScene.ShowLabel(0, root);

        ElevatorScene standardElevatorScene = new ElevatorScene(elevatorW / 2 + 2, getFloorY(1), elevatorW / 2 - 2, floorH);
        standardElevatorScene.Show(root, Color.BLUE);
        standardElevatorScene.ShowLabel(0, root);

        // Show floors
        int curY = 0;
        for (int i = maxFloor; i > 0; i--) {
            FloorScene floorScene = new FloorScene(elevatorW, curY, floorW, floorH, i);
            floorScene.Show(root);
            floorScene.showPassengers(building.getStaffPassangerCountOnFloor(i), building.getPatientPassangerCountOnFloor(i), root);
            floors.set(i-1, floorScene);
            curY += floorH;
        }

        // Animation setup
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        final int[] count = {1};
        pause.setOnFinished(event -> {

            // Animation logic
            System.out.println("One second has passed!");
            //floors.get(0).removePassangers(2, 1, root);

            System.out.println("----Iteration " + count[0] + " started ----");
            System.out.println("Total number of passengers in the building: " + building.countPassengers());

            // Update elevator scenes
            standardElevatorScene.UpdateY(getFloorY(standardElevator.getCurrentFloor()));
            expressElevatorScene.UpdateY(getFloorY(expressElevator.getCurrentFloor()));
            standardElevatorScene.UpdateCount(standardElevator.curPassengers.size());
            expressElevatorScene.UpdateCount(expressElevator.curPassengers.size());

            // Stop elevators
            standardElevator.stop();
            expressElevator.stop();

            // Update elevator scenes after stopping
            standardElevatorScene.UpdateCount(standardElevator.curPassengers.size());
            expressElevatorScene.UpdateCount(expressElevator.curPassengers.size());

            // Remove passengers who have reached their destination
            building.removePassengersThatAreDone();

            // Accept passengers onto elevators and remove them from the floors
            Pair<Integer, Integer> standardP = standardElevator.acceptPassengers(building.getPassengersOnFloor(standardElevator.getCurrentFloor()));
            Pair<Integer, Integer> expressP = expressElevator.acceptPassengers(building.getPassengersOnFloor(expressElevator.getCurrentFloor()));

            if (standardP != null)
                floors.get(standardElevator.getCurrentFloor() - 1).removePassengers(standardP.getKey(), standardP.getValue(), root);
            if (expressP != null)
                floors.get(expressElevator.getCurrentFloor() - 1).removePassengers(expressP.getKey(), expressP.getValue(), root);

            // Move elevators
            List<ElevatorRequest> requests = building.getElevatorRequests();
            standardElevator.move(requests);
            expressElevator.move(requests);

            // Update elevator scenes after moving
            standardElevatorScene.UpdateCount(standardElevator.curPassengers.size());
            expressElevatorScene.UpdateCount(expressElevator.curPassengers.size());

            System.out.println("----Iteration " + count[0] + " completed ----");
            count[0]++;

            // Check if simulation is finished
            if (building.countPassengers() > 0)
                pause.playFromStart();
            else
                System.out.println("Simulation DONE");
        });

        // Start button event handler
        btn.setOnAction(e -> {
            pause.play(); // Start the animation
            btn.setDisable(true); // Disable the button
            root.getChildren().remove(btn); // Remove the button from the scene
        });

        root.getChildren().add(btn);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to calculate Y-coordinate of a floor
    public int getFloorY(int floor) {
        return (maxFloor - floor) * floorH;
    }

    // Method to add random passengers to the building
    private void addRandomPassengers(Building building, int count) {
        for (int i = 0; i < count; i++) {
            boolean staff = Math.random() < 0.2;
            int randomStartFloor = 0;
            int randomDestinationFloor = 0;
            while (randomStartFloor == randomDestinationFloor) {
                randomStartFloor = new Random().nextInt(1, building.getFloors() + 1);
                randomDestinationFloor = new Random().nextInt(1, building.getFloors() + 1);
            }

            Passenger passenger;
            if (staff) {
                passenger = new StaffPassenger(randomStartFloor, randomDestinationFloor);
            } else {
                passenger = new PatientPassenger(randomStartFloor, randomDestinationFloor);
            }
            building.addPassenger(passenger, passenger.startingFloor);
        }
    }

    // Inner class representing an elevator scene
    public class ElevatorScene {
        public int x;
        public int y;

        public int w;
        public int h;

        private Rectangle rect;
        private Label label;
        public ElevatorScene(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        // Method to show the elevator scene
        public void Show(Group root, Color color) {
            rect = new Rectangle(x, y, w, h);
            rect.setFill(color);
            rect.setStroke(color);
            rect.setStrokeWidth(2);

            root.getChildren().add(rect);
        }

        // Method to update the Y-coordinate of the elevator scene
        public void UpdateY(int y) {
            this.y = y;
            rect.setY(y);
            label.setLayoutY(y + (h / 4));
        }

        // Method to update the passenger count label in the elevator scene
        public void UpdateCount(int count) {
            label.setText(""+count);
        }

        // Method to show the passenger count label in the elevator scene
        public void ShowLabel(int count, Group root) {
            label = new Label( ""+count);

            label.setLayoutX(x + w / 2);
            label.setLayoutY(y + (h / 4));

            root.getChildren().add(label);
        }
    }

    // Inner class representing a floor scene
    public class FloorScene {
        public int floor;
        public int x;
        public int y;

        public int w;
        public int h;

        public List<Line> expressPassengers = new ArrayList<>();
        public List<Line> standardPassengers = new ArrayList<>();

        public FloorScene(int x, int y, int w, int h, int floor) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.floor = floor;
        }

        // Method to show the floor scene
        public void Show(Group root) {
            Rectangle rect1 = new Rectangle(x, y, w, h);
            rect1.setFill(Color.TRANSPARENT);
            rect1.setStroke(Color.BLACK);
            rect1.setStrokeWidth(2);

            Label label = new Label("Floor " + floor);
            label.setLayoutX(x + w - 50);
            label.setLayoutY(y);

            root.getChildren().add(rect1);
            root.getChildren().add(label);
        }

        // Method to show passengers waiting on the floor
        public void showPassengers(int expressCount, int standardCount, Group root){
            int passangerX = x + 10;
            for (int i = 0; i < expressCount; i++) {
                Line line = new Line(passangerX, y + 4 , passangerX, y + h - 4);
                line.setStrokeWidth(2);
                line.setStroke(Color.RED);
                passangerX += 10;
                root.getChildren().add(line);
                expressPassengers.add(line);
            }

            for (int i = 0; i < standardCount; i++) {
                Line line = new Line(passangerX, y + 4 , passangerX, y + h - 4);
                line.setStrokeWidth(2);
                line.setStroke(Color.BLUE);
                passangerX += 10;
                root.getChildren().add(line);
                standardPassengers.add(line);
            }
        }

        // Method to remove passengers from the floor
        public void removePassengers(int expressCount, int standartCount, Group root) {
            while (expressCount > 0) {
                if (expressPassengers.isEmpty()) break;

                int index = expressPassengers.size() - 1;
                root.getChildren().remove(expressPassengers.get(index));
                expressPassengers.remove(index);
                expressCount--;
            }

            while (standartCount > 0) {
                if (standardPassengers.isEmpty()) break;
                int index = standardPassengers.size() - 1;

                root.getChildren().remove(standardPassengers.get(index));
                standardPassengers.remove(index);
                standartCount--;
            }
        }
    }
}

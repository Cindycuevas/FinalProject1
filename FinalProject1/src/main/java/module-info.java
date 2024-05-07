module com.example.finalproject1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.finalproject1 to javafx.fxml;
    exports com.example.finalproject1;
}
module com.example.prog3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.management;


    opens com.example.prog3 to javafx.fxml;
    exports com.example.prog3;
}
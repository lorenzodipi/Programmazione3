module com.example.prog3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.prog3 to javafx.fxml;
    exports com.example.prog3;
}
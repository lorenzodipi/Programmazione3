module com.example.index {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.index to javafx.fxml;
    exports com.example.index;
}
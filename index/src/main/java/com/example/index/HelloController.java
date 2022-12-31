package com.example.index;

import javafx.css.converter.PaintConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class HelloController {
    @FXML
    private Label label;
    @FXML
    private Button bottone;
    @FXML
    private Circle circle;

    @FXML
    public void initialize(){
        Image image = new Image(getClass().getResource("account.jpg").toExternalForm());
        circle.setFill(new ImagePattern(image));

        bottone.setOnMouseClicked(this::nascondi);
    }

    private void nascondi(MouseEvent mouseEvent) {
        label.setVisible(false);
        label.setManaged(false);
    }
}
package com.example.prog3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("client.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Client");
        stage.setScene(scene);
        stage.show();

        /*stage.setOnCloseRequest(e -> {
            Thread.getAllStackTraces().keySet().forEach(Thread::interrupt);
        });*/
    }



    public static void main(String[] args) {
        launch();
    }
}
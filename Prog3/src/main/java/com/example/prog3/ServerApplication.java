package com.example.prog3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerApplication extends Application {
    private ServerController serverController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("server.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        serverController = fxmlLoader.getController();
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        serverController.close();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}

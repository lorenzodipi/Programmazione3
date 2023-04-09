package com.example.prog3;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;

public class ServerController {
    Server server;
    @FXML
    ListView<String> serverList;

    @FXML
    protected void initialize() throws IOException {
        server = new Server();
        serverList.itemsProperty().bind(server.getLogList());

    }
}

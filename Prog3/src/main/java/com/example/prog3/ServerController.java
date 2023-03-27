package com.example.prog3;

import javafx.fxml.FXML;

import java.io.IOException;

public class ServerController {
    Server server;
    @FXML
    protected void initialize() throws IOException {
        System.out.println("00000000000000000000000000000");
        server = new Server();
    }
}

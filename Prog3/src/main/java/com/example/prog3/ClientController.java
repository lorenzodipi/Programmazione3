package com.example.prog3;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class ClientController {
    @FXML
    private Label lblFrom;
    @FXML
    private Label lblTo;
    @FXML
    private Label lblSubject;
    @FXML
    private Label lblUsername;
    @FXML
    private TextArea txtEmailContent;
    @FXML
    private ListView<Email> lstEmails;
    private Client model;
    private Email selectedEmail;

    private Email emptyEmail;

    @FXML
    protected void initialize() {
        if (this.model != null)
            throw new IllegalStateException("Model can only be initialized once");

        model = new Client("studente@unito.it");

        model.generateRandomEmails(10);
        selectedEmail = null;

        lstEmails.itemsProperty().bind(model.inboxProperty());
        lstEmails.setOnMouseClicked(this::showSelectedEmail);
        lblUsername.textProperty().bind(model.emailAddressProperty());

        emptyEmail = new Email("", List.of(""), "", "");
        txtEmailContent.setEditable(false);
        txtEmailContent.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");


        updateDetailView(emptyEmail);
    }
    protected void showSelectedEmail(MouseEvent mouseEvent) {
        Email email = lstEmails.getSelectionModel().getSelectedItem();

        selectedEmail = email;
        updateDetailView(email);
    }

    protected void updateDetailView(Email email) {
        if(email != null) {
            lblFrom.setText(email.getSender());
            lblTo.setText(String.join(", ", email.getReceivers()));
            lblSubject.setText(email.getSubject());
            txtEmailContent.setText(email.getText());
        }
    }

    public void onDeleteButtonClick(MouseEvent mouseEvent) {
    }
}
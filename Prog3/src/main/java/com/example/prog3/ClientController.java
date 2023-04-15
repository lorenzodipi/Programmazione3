package com.example.prog3;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ClientController {
    @FXML
    private Label lblFrom;
    @FXML
    private Label errorLabel;
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
    @FXML
    private Label lblEntrata;
    @FXML
    private Label lblUscita;
    @FXML
    private Button btnNewMail;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSender;
    @FXML
    private Button btnReply;
    @FXML
    private Button btnForward;
    @FXML
    private SplitPane splitPane;
    @FXML
    private GridPane gridPaneSender;
    @FXML
    private TextField txtFieldDestinatario;
    @FXML
    private TextField txtFieldOggetto;
    @FXML
    private TextArea txtAreaSender;
    @FXML
    private Button btnSenderAnnulla;
    @FXML
    private Button btnSenderInvia;
    private Client model;
    private Email selectedEmail;
    private Email emptyEmail;
    private int casella;

    @FXML
    protected void initialize() throws IOException {
        if (this.model != null)
            throw new IllegalStateException("Model can only be initialized once");

        model = new Client();

        lblUsername.textProperty().bind(model.getUser());

        errorLabel.setText("");

        onClickEntrata(null);


        lblEntrata.setOnMouseClicked(this::onClickEntrata);
        lblUscita.setOnMouseClicked(this::onClickUscita);
        btnNewMail.setOnMouseClicked(this::onClickNew);
        btnSenderAnnulla.setOnMouseClicked(this::onClickEntrata);
        btnSenderInvia.setOnMouseClicked(this::onClickSendEmail);
        btnDelete.setOnMouseClicked(this::onClickDelete);
        btnReply.setOnMouseClicked(this::onClickReply);
        btnForward.setOnMouseClicked(this::onClickForward);
    }
    private void showSelectedEmail(MouseEvent mouseEvent) {
        Email email = lstEmails.getSelectionModel().getSelectedItem();
        selectedEmail = email;
        updateDetailView(email);
    }
    @FXML
    private void onClickNew(MouseEvent mouseEvent){
        txtFieldDestinatario.clear();
        txtFieldOggetto.clear();
        txtAreaSender.clear();

        txtFieldDestinatario.setEditable(true);
        txtFieldOggetto.setEditable(true);

        gridPaneSender.setManaged(true);
        gridPaneSender.setVisible(true);
        splitPane.setManaged(false);
        splitPane.setVisible(false);
    }
    protected void updateDetailView(Email email) {
        if (email != null) {
            if(casella == 0){
                lblFrom.setText(email.getSender());
                lblTo.setText(email.getReceiver());
                lblSubject.setText(email.getSubject());
                txtEmailContent.setText(email.getText());
            }else {
                lblFrom.setText(email.getReceiver());
                lblTo.setText(email.getSender());
                lblSubject.setText(email.getSubject());
                txtEmailContent.setText(email.getText());
            }

        }
    }
    private void onClickDelete(MouseEvent mouseEvent){
        if (casella == 1){
            model.socketDelete2(lblUsername.getText(),lstEmails.getSelectionModel().getSelectedIndex(),lstEmails.getSelectionModel().getSelectedItem());
            onClickUscita(null);
        } else {
            model.socketDelete1(lblUsername.getText(),lstEmails.getSelectionModel().getSelectedIndex(),lstEmails.getSelectionModel().getSelectedItem());
            onClickEntrata(null);
        }
    }
    private void onClickSendEmail(MouseEvent mouseEvent){
        String destinatario = txtFieldDestinatario.getText();
        String oggetto = txtFieldOggetto.getText();
        String testo = txtAreaSender.getText();

        if(destinatario.isEmpty()){
            txtFieldDestinatario.setBorder(new Border(new BorderStroke( Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1))));
            txtFieldDestinatario.setBackground(new Background(new BackgroundFill(new Color(Color.RED.getRed(),0,0,0.1), CornerRadii.EMPTY, Insets.EMPTY)));
        }else {
            txtFieldDestinatario.setBorder(new Border(new BorderStroke( Color.GRAY, BorderStrokeStyle.SOLID,new CornerRadii(4), new BorderWidths(1))));
            txtFieldDestinatario.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        if(oggetto.isEmpty()){
            txtFieldOggetto.setBorder(new Border(new BorderStroke( Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1))));
            txtFieldOggetto.setBackground(new Background(new BackgroundFill(new Color(Color.RED.getRed(),0,0,0.1), CornerRadii.EMPTY, Insets.EMPTY)));
        }else {
            txtFieldOggetto.setBorder(new Border(new BorderStroke( Color.GRAY, BorderStrokeStyle.SOLID,new CornerRadii(4), new BorderWidths(1))));
            txtFieldOggetto.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        if(testo.isEmpty()){
            txtAreaSender.setBorder(new Border(new BorderStroke( Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1))));
        }else {
            txtAreaSender.setBorder(new Border(new BorderStroke( Color.GRAY, BorderStrokeStyle.SOLID,new CornerRadii(4), new BorderWidths(1))));
        }

        if(!destinatario.equals("") && !destinatario.equals(lblUsername.getText()) && !oggetto.equals("") && !testo.equals("")){

            model.socketSend(lblUsername.getText(),destinatario,oggetto,testo);
            onClickEntrata(null);
        }

    }
    private void onClickEntrata(MouseEvent mouseEvent) {

        casella = 0;


        lblFrom.setText("");
        lblTo.setText("");
        lblSubject.setText("");
        txtEmailContent.setText("");

        errorLabel.textProperty().bind(model.getError());
        txtEmailContent.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");
        selectedEmail = null;
        lstEmails.itemsProperty().bind(model.inboxProperty());
        lstEmails.setOnMouseClicked(this::showSelectedEmail);
        txtEmailContent.setEditable(false);
        gridPaneSender.setManaged(false);
        gridPaneSender.setVisible(false);
        splitPane.setManaged(true);
        splitPane.setVisible(true);

    }
    private void onClickUscita(MouseEvent mouseEvent) {
        casella = 1;

        model.socketUscita(lblUsername.getText());

        lblFrom.setText("");
        lblTo.setText("");
        lblSubject.setText("");
        txtEmailContent.setText("");

        errorLabel.textProperty().bind(model.getError());
        txtEmailContent.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");
        selectedEmail = null;

        lstEmails.itemsProperty().bind(model.outboxProperty());
        lstEmails.setOnMouseClicked(this::showSelectedEmail);
        emptyEmail = new Email("", "", "", "");
        txtEmailContent.setEditable(false);
    }
    private void onClickReply(MouseEvent mouseEvent){
        if(!lblFrom.getText().isEmpty() || !lblTo.getText().isEmpty() || !lblSubject.getText().isEmpty() || !txtEmailContent.getText().isEmpty()){
            txtFieldDestinatario.clear();
            txtFieldOggetto.clear();
            txtAreaSender.clear();

            txtFieldDestinatario.setText(lblFrom.getText());
            txtFieldOggetto.setText(lblSubject.getText());

            txtFieldDestinatario.setEditable(false);
            txtFieldOggetto.setEditable(false);

            gridPaneSender.setManaged(true);
            gridPaneSender.setVisible(true);
            splitPane.setManaged(false);
            splitPane.setVisible(false);

        }
    }
    private void onClickForward(MouseEvent mouseEvent){
        if(!lblFrom.getText().isEmpty() || !lblTo.getText().isEmpty() || !lblSubject.getText().isEmpty() || !txtEmailContent.getText().isEmpty()){
            txtFieldDestinatario.clear();
            txtFieldOggetto.clear();
            txtAreaSender.clear();

            txtAreaSender.setText(txtEmailContent.getText());
            txtFieldOggetto.setText(lblSubject.getText());

            txtFieldDestinatario.setEditable(true);
            txtFieldOggetto.setEditable(false);

            gridPaneSender.setManaged(true);
            gridPaneSender.setVisible(true);
            splitPane.setManaged(false);
            splitPane.setVisible(false);


        }
    }

}
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

    //C:\Users\Lorenzo Di Palma\Desktop\MAIN\Progetti\Programmazione3\Prog3\src\main\java\com\example\prog3\mail\
    ///Users/lorenzodipalma/Documents/GitHub/Programmazione3/Prog3/src/main/java/com/example/prog3/mail
    String path = "/Users/lorenzodipalma/Documents/GitHub/Programmazione3/Prog3/src/main/java/com/example/prog3/mail/";
    ///Users/lorenzodipalma/Documents/GitHub/Programmazione3/Prog3/username.txt
    //C:\Users\Lorenzo Di Palma\Desktop\MAIN\Progetti\Programmazione3\Prog3\\username.txt
    String path_user ="/Users/lorenzodipalma/Documents/GitHub/Programmazione3/Prog3/username.txt";
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
    private String email;
    private int casella;

    /*----------------------------------------------------------------------------------------------------------------------*/

    protected String getUsername() {
        ArrayList<String> mail = new ArrayList<>();
        try {
            File myObj = new File(path_user);
            FileReader reader = new FileReader(myObj);
            Scanner myReader = new Scanner(reader);
            while (myReader.hasNextLine()) {
                mail.add(myReader.nextLine());
            }
            reader.close();
            myReader.close();
            Random rand = new Random();
            return "lorenzo.dipalma@unito.it";
            //return mail.get(rand.nextInt(mail.size()));
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    /*----------------------------------------------------------------------------------------------------------------------*/
    private void showSelectedEmail(MouseEvent mouseEvent) {
        Email email = lstEmails.getSelectionModel().getSelectedItem();
        selectedEmail = email;
        updateDetailView(email);
    }
    @FXML
    protected void initialize() throws IOException {
        if (this.model != null)
            throw new IllegalStateException("Model can only be initialized once");

        /*try {
            Server server = new Server(new ServerSocket(1234));
        }catch (IOException e) {
            System.out.println("Error creating server socket");
        }*/

        email = getUsername();
        model = new Client(email);

        onClickEntrata(null);

        //OnClick Section
        lblEntrata.setOnMouseClicked(this::onClickEntrata);
        lblUscita.setOnMouseClicked(this::onClickUscita);
        btnNewMail.setOnMouseClicked(this::onClickNew);
        btnSenderAnnulla.setOnMouseClicked(this::onClickEntrata);
        btnSenderInvia.setOnMouseClicked(this::onClickSendEmail);
        btnDelete.setOnMouseClicked(this::onClickDelete);
    }
    private void onClickNew(MouseEvent mouseEvent){

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
            model.socketDelete2(email,lstEmails.getSelectionModel().getSelectedIndex());
            System.out.println("11111");
            onClickUscita(null);
        } else {
            System.out.println("2222");
            model.socketDelete1(email,lstEmails.getSelectionModel().getSelectedIndex());
            onClickEntrata(null);
        }
        System.out.println("------------");
        System.out.println("------------");

    }
    private void onClickSendEmail(MouseEvent mouseEvent){
        String destinatario = txtFieldDestinatario.getText();
        String oggetto = txtFieldOggetto.getText();
        String testo = txtAreaSender.getText();

        if(destinatario.equals("") || destinatario.equals(email)){
            txtFieldDestinatario.setBorder(new Border(new BorderStroke( Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1))));
            txtFieldDestinatario.setBackground(new Background(new BackgroundFill(new Color(Color.RED.getRed(),0,0,0.1), CornerRadii.EMPTY, Insets.EMPTY)));
        }else {
            txtFieldDestinatario.setBorder(new Border(new BorderStroke( Color.GRAY, BorderStrokeStyle.SOLID,new CornerRadii(4), new BorderWidths(1))));
            txtFieldDestinatario.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        if(oggetto.equals("")){
            txtFieldOggetto.setBorder(new Border(new BorderStroke( Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1))));
            txtFieldOggetto.setBackground(new Background(new BackgroundFill(new Color(Color.RED.getRed(),0,0,0.1), CornerRadii.EMPTY, Insets.EMPTY)));
        }else {
            txtFieldOggetto.setBorder(new Border(new BorderStroke( Color.GRAY, BorderStrokeStyle.SOLID,new CornerRadii(4), new BorderWidths(1))));
            txtFieldOggetto.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        if(testo.equals("")){
            txtAreaSender.setBorder(new Border(new BorderStroke( Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(1))));
        }else {
            txtAreaSender.setBorder(new Border(new BorderStroke( Color.GRAY, BorderStrokeStyle.SOLID,new CornerRadii(4), new BorderWidths(1))));
        }

        if(!destinatario.equals("") && !destinatario.equals(email) && !oggetto.equals("") && !testo.equals("")){

            model.socketSend(email,destinatario,oggetto,testo);
            onClickEntrata(null);
            /*System.out.println("dentro");
            try {
                File myObj = new File(path+destinatario+".txt");
                FileReader reader = new FileReader(myObj);
                Scanner myReader = new Scanner(reader);
                String text= "";

                while(myReader.hasNextLine())
                    text = text + myReader.nextLine() +"\n";

                text = text.substring(0, text.length()-1);

                myReader.close();
                reader.close();

                FileWriter writer = new FileWriter(myObj);
                writer.append(text).append("\n").append(email).append("\n").append(oggetto).append("\n").append(testo).append("\n------------------");
                writer.close();

                myObj = new File(path+email+".txt");
                reader = new FileReader(myObj);
                myReader = new Scanner(reader);
                text= "";

                while(myReader.hasNextLine())
                    text = text + myReader.nextLine() +"\n";

                text = text.substring(0, text.length()-1);

                myReader.close();
                reader.close();

                writer = new FileWriter(myObj);
                writer.append(text).append("\n").append(email).append("\n").append(destinatario).append("\n").append(oggetto).append("\n").append(testo).append("\n------------------");
                writer.close();

                onClickEntrata(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
        }

    }
    private void onClickEntrata(MouseEvent mouseEvent) {

        casella = 0;

        model.socketEntrata(email);

        lblUsername.textProperty().bind(model.emailAddressProperty());
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

        model.socketUscita(email);

        lblUsername.textProperty().bind(model.emailAddressProperty());
        txtEmailContent.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");
        selectedEmail = null;
        lstEmails.itemsProperty().bind(model.inboxProperty());
        lstEmails.setOnMouseClicked(this::showSelectedEmail);
        emptyEmail = new Email("", "", "", "");
        txtEmailContent.setEditable(false);
    }
}
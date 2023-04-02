package com.example.prog3;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Client{
    Socket socket = null;
    //ObjectOutputStream outputStream = null;
    //ObjectInputStream inputStream = null;
    private final ListProperty<Email> inbox;
    private final ObservableList<Email> inboxContent;
    private final StringProperty emailAddress;
    String host = "127.0.0.1";
    int port = 4445;

    /**
     * Costruttore della classe.
     *
     * @param emailAddress   indirizzo email
     *
     */

    public Client(String emailAddress) {
        this.inboxContent = FXCollections.observableList(new LinkedList<>());
        this.inbox = new SimpleListProperty<>();
        this.inbox.set(inboxContent);
        this.emailAddress = new SimpleStringProperty(emailAddress);

        inboxContent.addListener(new ListChangeListener<Email>() {
            @Override
            public void onChanged(Change<? extends Email> change) {
            }
        });
    }

    /**
     * @return      lista di email
     *
     */
    public ListProperty<Email> inboxProperty() {
        return inbox;
    }

    /**
     *
     * @return   indirizzo email della casella postale
     *
     */
    public StringProperty emailAddressProperty() {
        return emailAddress;
    }

    /**
     *
     * @return   elimina l'email specificata
     *
     */
    public void deleteEmail(Email email) {
        inboxContent.remove(email);
    }

    public void setEmail(ArrayList<Email> em) throws IOException {
        if (!em.isEmpty()) {
            inboxContent.clear();
            for (Email email : em) {
                inboxContent.add(email);
            }
        }

    }

    /*public void getEmailUscita(String username) throws IOException {
        File myObj = new File("C:\\Users\\Lorenzo Di Palma\\Desktop\\MAIN\\Progetti\\Programmazione3\\Prog3\\src\\main\\java\\com\\example\\prog3\\mail\\"+username+".txt");
        if (myObj.exists()){
            FileReader reader = new FileReader(myObj);
            Scanner myReader = new Scanner(reader);
            inboxContent.clear();
            while (myReader.hasNextLine()) {
                String sender = myReader.nextLine();
                if(!sender.equals(username)){
                    while (!myReader.nextLine().equals("------------------")){}
                }else {
                    String receiver = myReader.nextLine();
                    String object = myReader.nextLine();
                    String content = "";
                    String riga;
                    while (!(riga = myReader.nextLine()).equals("------------------"))
                        content = content + riga;
                    Email email = new Email(receiver, sender, object, content);
                    inboxContent.add(email);
                }
            }

            reader.close();
            myReader.close();
        }
    }*/

    @Override
    public String toString() {
        return "Client{" +
                "inbox=" + inbox +
                ", inboxContent=" + inboxContent +
                ", emailAddress=" + emailAddress +
                '}';
    }

    public void runClients() {
        System.out.println("siuuuuuuuuuuu");
        /*List<Runnable> clients = new ArrayList<>();

        clients.add(this::communicate);

        for (Runnable client : clients) {
            Thread thread = new Thread(client);
            thread.start();
        }*/
    }

    /*public void communicate(){

        boolean success = false;
        while(!success) {
            System.out.println("[Client "+ this.emailAddress +"]");

            success = tryCommunication();

            if(success) {
                continue;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tryCommunication() {
        try {
            connectToServer();

            Thread.sleep(5000);

            sendStudents(emailAddress);//TODO
            receiveModifiedStudents();

            return true;
        } catch (ConnectException ce) {
            // nothing to be done
            return false;
        } catch (IOException | ClassNotFoundException se) {
            se.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnections();
        }
    }

    private void connectToServer() throws IOException {
        System.out.println("prova");
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        // Dalla documentazione di ObjectOutputStream
        // callers may wish to flush the stream immediately to ensure that constructors for receiving
        // ObjectInputStreams will not block when reading the header.
        outputStream.flush();

        inputStream = new ObjectInputStream(socket.getInputStream());

        System.out.println("[Client "+ this.emailAddress + "] Connesso");
    }*/

    /*private void sendStudents(StringProperty email) throws IOException, ClassNotFoundException {
        outputStream.writeObject(email);
        outputStream.flush();
    }*/

    /*private void receiveModifiedStudents() throws IOException, ClassNotFoundException {
        StringProperty modifiedStudents = (StringProperty) inputStream.readObject();

        if (modifiedStudents != null)
            System.out.println("[Client " + this.emailAddress + "]");
    }*/

    /*private void closeConnections() {
        if (socket != null) {
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public void socketEntrata(String username){
        try {
            socket = new Socket(InetAddress.getLocalHost(),7);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.flush();
            out.writeObject(username);
            out.writeObject("entrata");

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ArrayList<Email> ee = (ArrayList<Email>) in.readObject();
            System.out.println(ee);
            setEmail(ee);

        } catch (IOException e) {
            System.out.println("ERRORE a");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("ERRORE b");
            throw new RuntimeException(e);
        }
    }

    public void socketUscita(String username){
        try {
            socket = new Socket(InetAddress.getLocalHost(),7);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.flush();
            out.writeObject(username);
            out.writeObject("uscita");

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ArrayList<Email> eu = (ArrayList<Email>) in.readObject();
            System.out.println(eu);
            setEmail(eu);

        } catch (IOException e) {
            System.out.println("ERRORE a");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("ERRORE b");
            throw new RuntimeException(e);
        }
    }


}

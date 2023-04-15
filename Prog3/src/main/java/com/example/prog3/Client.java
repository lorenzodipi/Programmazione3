package com.example.prog3;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Client {

    Socket socket = null;
    private ListProperty<Email> inbox;
    private ObservableList<Email> inboxContent;
    private ObjectProperty<String> error = new SimpleObjectProperty<>();
    private ObjectProperty<String> user = new SimpleObjectProperty<>();
    private ListProperty<Email> outbox;
    private ObservableList<Email> outboxContent;
    String username = "";

    public Client() {
        this.inboxContent = FXCollections.observableList(new LinkedList<>());
        this.inbox = new SimpleListProperty<>(inboxContent);

        this.outboxContent = FXCollections.observableList(new LinkedList<>());
        this.outbox = new SimpleListProperty<>(outboxContent);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        while (username.isEmpty())
                            socketUsername();
                        socketEntrata(username);
                        socketUscita(username);
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

        inboxContent.addListener(new ListChangeListener<Email>() {
            @Override
            public void onChanged(Change<? extends Email> change) {
            }
        });

        outboxContent.addListener(new ListChangeListener<Email>() {
            @Override
            public void onChanged(Change<? extends Email> change) {
            }
        });
    }

    public ObjectProperty<String> getError(){
        return error;
    }
    public ObjectProperty<String> getUser(){
        return user;
    }

    public void setError(String e){
        Platform.runLater(() -> error.set(e));
    }
    public void setUser(String u){
        Platform.runLater(() -> user.set(u));
    }
    public ListProperty<Email> inboxProperty() {
        return inbox;
    }
    public ListProperty<Email> outboxProperty() {
        return outbox;
    }
    public void setEmail(ArrayList<Email> em,int c) throws IOException {

        if(c==0)
            Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int size = inbox.size();
                if(inbox.size()==0){
                    inboxContent.addAll(em);
                    Collections.reverse(inboxContent);
                }else{
                    for (int i = 0; i < em.size()-size; i++) {
                        inboxContent.add(em.get(inbox.size()+i));
                    }
                    Collections.reverse(inboxContent);
                }

            }
        });
        else
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    int size = outbox.size();
                    if(outbox.size()==0){
                        outboxContent.addAll(em);
                        Collections.reverse(outboxContent);
                    }else{
                        for (int i = 0; i < em.size()-size; i++) {
                            outboxContent.add(em.get(outbox.size()+i));
                        }
                        Collections.reverse(outboxContent);
                    }

                    /*outboxContent.clear();
                    if (!em.isEmpty()) {
                        for (Email email : em) {
                            outboxContent.add(email);
                        }
                    }*/
                }
            });

    }
    @Override
    public String toString() {
        return "Client{" +
                "inbox=" + inbox +
                ", inboxContent=" + inboxContent +
                '}';
    }
    public void socketEntrata(String username) {
        try {
            socket = new Socket(InetAddress.getLocalHost(), 7);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.flush();
            out.writeObject(username);
            out.writeObject("entrata");
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            ArrayList<Email> ee = (ArrayList<Email>) in.readObject();

            if(ee.size() > inbox.getSize())
                setEmail(ee,0);

            setError("");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERRORE a entrata");

            setError("Errore di connessione al server!");
        }
    }
    public void socketUscita(String username) {
        try {
            socket = new Socket(InetAddress.getLocalHost(), 7);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.flush();
            out.writeObject(username);
            out.writeObject("uscita");

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ArrayList<Email> eu = (ArrayList<Email>) in.readObject();

            if(eu.size() > outbox.getSize())
                setEmail(eu,1);

            setError("");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERRORE a Uscita");
            setError("Errore di connessione al server!");
        }
    }
    public void socketSend(String username, String destinatario, String oggetto, String testo) {
        try {
            Email send = new Email(username, destinatario, oggetto, testo);

            socket = new Socket(InetAddress.getLocalHost(), 7);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.flush();
            out.writeObject(username);
            out.writeObject("send");
            out.writeObject(send);
            setError("");

        } catch (IOException e) {
            System.out.println("ERRORE a send");
            setError("Errore nel mandare la mail!");
        }
    }
    public void socketDelete1(String username, int index,Email dEmail) { //ENTRATA
        try {
            socket = new Socket(InetAddress.getLocalHost(), 7);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.flush();
            out.writeObject(username);
            out.writeObject("delete1");
            out.writeObject(inbox.size()-index);

            inboxContent.remove(dEmail);

            setError("");

        } catch (IOException e) {
            System.out.println("ERRORE a delete1");
            setError("Errore nel cancellare la mail!");
        }
        /*System.out.println("inboxSize: " + inboxContent.size());
        System.out.println("index: " + index);*/
    }
    public void socketDelete2(String username, int index,Email dEmail) { //Uscita
        try {
            socket = new Socket(InetAddress.getLocalHost(), 7);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.flush();
            out.writeObject(username);
            out.writeObject("delete2");
            out.writeObject(outbox.size()-index);

            outboxContent.remove(dEmail);

            setError("");

        } catch (IOException e) {
            System.out.println("ERRORE a delete2");
            setError("Errore nel cancellare la mail!");
        }
    }
    public void socketUsername() {
            try {
                System.out.println("Dentro inizio "+username);
                socket = new Socket(InetAddress.getLocalHost(), 7);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                out.flush();
                out.writeObject("");
                out.writeObject("username");

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                username = (String) in.readObject();

                setError("");
                System.out.println("Dentro fine "+username);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("ERRORE a username");
                setError("Errore di connessione al server!");
            }
        System.out.println("Fuori "+username);
        setUser(username);
    }
}

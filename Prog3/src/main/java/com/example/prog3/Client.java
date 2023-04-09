package com.example.prog3;

import javafx.application.Platform;
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

public class Client {

    Socket socket = null;
    private ListProperty<Email> inbox;
    private ObservableList<Email> inboxContent;
    private ListProperty<Email> outbox;
    private ObservableList<Email> outboxContent;
    private final StringProperty emailAddress;
    String username;

    public Client() {
        this.inboxContent = FXCollections.observableList(new LinkedList<>());
        this.inbox = new SimpleListProperty<>();
        this.inbox.set(inboxContent);

        this.outboxContent = FXCollections.observableList(new LinkedList<>());
        this.outbox = new SimpleListProperty<>();
        this.outbox.set(outboxContent);

        this.emailAddress = new SimpleStringProperty(socketUsername());

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        socketEntrata(username);
                        Thread.sleep(5000);
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

    public String getEmailAddress() {
        return this.emailAddress.getValue();
    }

    public ListProperty<Email> inboxProperty() {
        return inbox;
    }
    public ListProperty<Email> outboxProperty() {
        return outbox;
    }

    public StringProperty emailAddressProperty() {
        return emailAddress;
    }

    public void deleteEmail(Email email) {
        inboxContent.remove(email);
    }

    public void setEmail(ArrayList<Email> em,int c) throws IOException {

        if(c==0)
            Platform.runLater(new Runnable() {
            @Override
            public void run() {
                inboxContent.clear();
                if (!em.isEmpty()) {
                    for (Email email : em) {
                        inboxContent.add(email);
                    }
                }
            }
        });
        else
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    outboxContent.clear();
                    if (!em.isEmpty()) {
                        for (Email email : em) {
                            outboxContent.add(email);
                        }
                    }
                }
            });

    }

    @Override
    public String toString() {
        return "Client{" +
                "inbox=" + inbox +
                ", inboxContent=" + inboxContent +
                ", emailAddress=" + emailAddress +
                '}';
    }

    public void runClients() {
        /*List<Runnable> clients = new ArrayList<>();

        clients.add(this::communicate);

        for (Runnable client : clients) {
            Thread thread = new Thread(client);
            thread.start();
        }*/
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
            setEmail(ee,0);

        } catch (IOException e) {
            System.out.println("ERRORE a entrata");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("ERRORE b entrata");
            throw new RuntimeException(e);
        }
    }

    public void socketUscita(String username) {
        try {
            socket = new Socket(InetAddress.getLocalHost(), 7);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.flush();
            out.writeObject(username);
            out.writeObject("uscita");
            //out.writeObject(username);

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ArrayList<Email> eu = (ArrayList<Email>) in.readObject();
            setEmail(eu,1);

        } catch (IOException e) {
            System.out.println("ERRORE a Uscita");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("ERRORE b Uscita");
            throw new RuntimeException(e);
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

        } catch (IOException e) {
            System.out.println("ERRORE a send");
            e.printStackTrace();
        }
    }

    public void socketDelete1(String username, int index) {
        try {
            socket = new Socket(InetAddress.getLocalHost(), 7);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.flush();
            out.writeObject(username);
            out.writeObject("delete1");
            //out.writeObject(username);
            out.writeObject(index);

        } catch (IOException e) {
            System.out.println("ERRORE a delete1");
            e.printStackTrace();
        }
    }

    public void socketDelete2(String username, int index) {
        try {
            socket = new Socket(InetAddress.getLocalHost(), 7);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.flush();
            out.writeObject(username);
            out.writeObject("delete2");
            //out.writeObject(username);
            out.writeObject(index);

        } catch (IOException e) {
            System.out.println("ERRORE a delete2");
            e.printStackTrace();
        }
    }

    public String socketUsername() {
        try {
            socket = new Socket(InetAddress.getLocalHost(), 7);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.flush();
            out.writeObject("");
            out.writeObject("username");

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            username = (String) in.readObject();
            return username;

        } catch (IOException e) {
            System.out.println("ERRORE a username");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

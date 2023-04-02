package com.example.prog3;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    String path = "C:\\Users\\Lorenzo Di Palma\\Desktop\\MAIN\\Progetti\\Programmazione3\\Prog3\\src\\main\\java\\com\\example\\prog3\\mail\\";

    Socket socket;
    ServerSocket s;
    ArrayList<Email> server_email;

    String username;

    public Server(){
        try {
            s = new ServerSocket(7);
            new Thread(new RunnableServer()).start();

        } catch (IOException e) {
            System.out.println("ERRORE ggggg");
        }
    }

    public ArrayList<Email> getEmailEntrata(String username) throws IOException {
        server_email = new ArrayList<>();
        File myObj = new File(path+username+".txt");
        if (myObj.exists()){
            FileReader reader = new FileReader(myObj);
            Scanner myReader = new Scanner(reader);
            while (myReader.hasNextLine()) {
                String sender = myReader.nextLine();
                if(sender.equals(username)){
                    while (!myReader.nextLine().equals("------------------")){}
                }else {
                    String object = myReader.nextLine();
                    String content = "";
                    String text;
                    while (!(text = myReader.nextLine()).equals("------------------"))
                        content = content + text;
                    Email email = new Email(sender, username, object, content);

                    server_email.add(email);
                }
            }
            reader.close();
            myReader.close();
        }
        return server_email;
    }

    public ArrayList<Email> getEmailUscita(String username) throws IOException {
        server_email = new ArrayList<>();
        File myObj = new File(path+username+".txt");
        if (myObj.exists()){
            FileReader reader = new FileReader(myObj);
            Scanner myReader = new Scanner(reader);

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
                    server_email.add(email);
                }
            }
            reader.close();
            myReader.close();
        }
        return server_email;
    }

    class ThreadEntrata implements Runnable {
        ObjectOutputStream out;
        public ThreadEntrata(ObjectOutputStream out){
            this.out = out;
        }

        @Override
        public void run() {
            try {
                server_email = new ArrayList<>();
                server_email = getEmailEntrata(username);

                System.out.println(server_email);
                out.writeObject(server_email);
            } catch (IOException e) {
                System.out.println("ERRORE NEL MANDARE ELENCO MAIL ENTRATA");
                e.printStackTrace();
            }

            //System.out.println("----- " + string); //TODO: ThreadPool con FixedThread

        }
    }

    class ThreadUscita implements Runnable {
        ObjectOutputStream out;
        public ThreadUscita(ObjectOutputStream out){
            this.out = out;
        }

        @Override
        public void run() {
            try {
                server_email = new ArrayList<>();
                server_email = getEmailUscita(username);

                System.out.println(server_email);
                out.writeObject(server_email);
            } catch (IOException e) {
                System.out.println("ERRORE NEL MANDARE ELENCO MAIL ENTRATA");
                e.printStackTrace();
            }

            //System.out.println("----- " + string); //TODO: ThreadPool con FixedThread

        }
    }

    class RunnableServer implements Runnable {
        public RunnableServer(){}

        @Override
        public void run() {
            while (true){
                try {
                    socket = s.accept();
                    System.out.println("Accettato socket " + socket);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.flush();
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    username = (String) in.readObject();
                    System.out.println(username);
                    String command = (String) in.readObject();
                    System.out.println(command);
                    switch (command) {

                        case "entrata":
                            ThreadPoolExecutor executor_entrata = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
                            executor_entrata.execute(new ThreadEntrata(out));
                            break;

                        case "uscita":
                            ThreadPoolExecutor executor_uscita = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
                            executor_uscita.execute(new ThreadUscita(out));
                            break;

                        default:
                            System.out.println("default");
                            break;
                    }
                }catch (Exception e){
                    System.out.println("ERRORE AAAA");
                    e.printStackTrace();
                }
            }

        }
    }
}

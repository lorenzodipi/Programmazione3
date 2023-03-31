package com.example.prog3;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

    Socket socket;
    ServerSocket s;
    ArrayList<Email> server_email;


    /*public void prova(){//TODO: da mettere tutto in un thread
        try {
            //ServerSocket s = new ServerSocket(7);
            //Socket socket = s.accept();
            System.out.println("Accettato socket " + socket);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String string = (String) in.readObject();

            System.out.println("----- "+string); //TODO: ThreadPool con FixedThread
            socket.close();
            s.close();

        }catch (Exception e){
            System.out.println("ERRORE");
        }
    }*/
    public Server(){

        try {
            s = new ServerSocket(7);
            socket = s.accept();
            new Thread(new RunnableServer()).start();

        } catch (IOException e) {
            System.out.println("ERRORE ggggg");
        }
    }

    public ArrayList<Email> getEmail(String username) throws IOException {
        server_email = new ArrayList<>();
        File myObj = new File("/Users/lorenzodipalma/Documents/GitHub/Programmazione3/Prog3/src/main/java/com/example/prog3/mail/"+username+".txt");
        if (myObj.exists()){
            ArrayList<Email> mail = new ArrayList<>();
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

    class RunnableServer implements Runnable {
        public RunnableServer(){}

        @Override
        public void run() {
            try {
                System.out.println("Accettato socket " + socket);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                String username = (String) in.readObject();
                System.out.println("username");
                System.out.println(username);
                String oriv = (String) in.readObject();
                System.out.println(oriv);
                switch (oriv) {
                    case "entrata":
                        server_email = new ArrayList<>();
                        server_email = getEmail(username);
                        //String string = (String) in.readObject();

                        try {
                            //Socket socket = new Socket(InetAddress.getLocalHost(),7);

                            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                            System.out.println(server_email);
                            out.writeObject(server_email);
                        } catch (IOException e) {
                            System.out.println("ERRORE NEL MANDARE ELENCO MAIL ENTRATA");
                            e.printStackTrace();
                        }

                        //System.out.println("----- " + string); //TODO: ThreadPool con FixedThread

                        socket.close();
                        s.close();
                        break;
                    default:
                        System.out.println("default");
                        break;
                }
            }catch (Exception e){
                System.out.println("ERRORE AAAA");
            }
        }
    }
}

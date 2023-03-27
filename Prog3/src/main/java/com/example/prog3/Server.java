package com.example.prog3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    Socket socket;
    ServerSocket s;

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
            new Thread(new RunnableServer());

        } catch (IOException e) {
            System.out.println("ERRORE");
        }
    }

    class RunnableServer implements Runnable {

        public RunnableServer(){}

        @Override
        public void run() {
            try {
                System.out.println("Accettato socket " + socket);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                String string = (String) in.readObject();

                System.out.println("----- "+string); //TODO: ThreadPool con FixedThread
                socket.close();
                s.close();

            }catch (Exception e){
                System.out.println("ERRORE");
            }
        }
    }
}

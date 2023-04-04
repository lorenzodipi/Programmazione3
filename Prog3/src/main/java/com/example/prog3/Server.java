package com.example.prog3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    //C:\Users\Lorenzo Di Palma\Desktop\MAIN\Progetti\Programmazione3\Prog3\src\main\java\com\example\prog3\mail\
    ///Users/lorenzodipalma/Documents/GitHub/Programmazione3/Prog3/src/main/java/com/example/prog3/mail
    String path = "/Users/lorenzodipalma/Documents/GitHub/Programmazione3/Prog3/src/main/java/com/example/prog3/mail/";

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
                server_email = new ArrayList<>(getEmailUscita(username));
                //server_email = getEmailUscita(username);
                System.out.println(server_email);
                out.writeObject(server_email);
            } catch (IOException e) {
                System.out.println("ERRORE NEL MANDARE ELENCO MAIL ENTRATA");
                e.printStackTrace();
            }

        }
    }

    class ThreadSend implements Runnable {
        ObjectOutputStream out;
        Email send ;
        public ThreadSend(ObjectOutputStream out, Email send){
            this.out = out;
            this.send = send;
        }

        @Override
        public void run() {
            try {
                File myObj = new File(path+send.getSender()+".txt");
                FileReader reader = new FileReader(myObj);
                Scanner myReader = new Scanner(reader);
                String text= "";

                while(myReader.hasNextLine())
                    text = text + myReader.nextLine() +"\n";

                text = text.substring(0, text.length()-1);

                myReader.close();
                reader.close();

                FileWriter writer = new FileWriter(myObj);
                writer.append(text).append("\n").append(send.getReceiver()).append("\n").append(send.getSubject()).append("\n").append(send.getText()).append("\n------------------");
                writer.close();

                myObj = new File(path+send.getReceiver()+".txt");
                reader = new FileReader(myObj);
                myReader = new Scanner(reader);
                text= "";

                while(myReader.hasNextLine())
                    text = text + myReader.nextLine() +"\n";

                text = text.substring(0, text.length()-1);

                myReader.close();
                reader.close();

                writer = new FileWriter(myObj);
                writer.append(text).append("\n").append(send.getReceiver()).append("\n").append(send.getSender()).append("\n").append(send.getSubject()).append("\n").append(send.getText()).append("\n------------------");
                writer.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    class ThreadDelete1 implements Runnable {
        ObjectOutputStream out;
        int index;
        public ThreadDelete1(ObjectOutputStream out, int index){
            this.out = out;
            this.index = index;
        }

        @Override
        public void run() {
            File myObj = new File(path+username+".txt");
            if (myObj.exists()){
                try {
                    FileReader reader = new FileReader(myObj);
                    Scanner myReader = new Scanner(reader);

                    int i = 0;
                    String testo = "";
                    while ( myReader.hasNextLine()) {
                        String sender = myReader.nextLine();

                        if(i != index || sender.equals(username)){
                            if (!sender.equals(username))
                                i = i + 1;
                            testo = testo + sender + "\n";
                            String riga;
                            while (!(riga = myReader.nextLine()).equals("------------------")){
                                testo = testo + riga + "\n";
                            }
                            testo = testo + "------------------\n";
                        }else {
                            while (!myReader.nextLine().equals("------------------")){}
                            i = i+1;
                        }
                    }
                    reader.close();
                    myReader.close();

                    FileWriter writer = new FileWriter(myObj);
                    writer.write(testo);
                    writer.close();

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        }
    }

    class ThreadDelete2 implements Runnable {
        ObjectOutputStream out;
        int index;
        public ThreadDelete2(ObjectOutputStream out,int index){
            this.out = out;
            this.index = index;
        }

        @Override
        public void run() {
            File myObj = new File(path+username+".txt");
            if (myObj.exists()){
                try {
                    FileReader reader = new FileReader(myObj);
                    Scanner myReader = new Scanner(reader);

                    int i = 0;
                    String testo = "";
                    while ( myReader.hasNextLine()) {
                        String sender = myReader.nextLine();

                        if(i != index || !sender.equals(username)){
                            if (sender.equals(username))
                                i = i + 1;
                            testo = testo + sender + "\n";
                            String riga;
                            while (!(riga = myReader.nextLine()).equals("------------------")){
                                testo = testo +riga + "\n";
                            }
                            testo = testo + "------------------\n";
                        }else {
                            while (!myReader.nextLine().equals("------------------")){}
                            i = i + 1;
                        }
                    }
                    reader.close();
                    myReader.close();

                    FileWriter writer = new FileWriter(myObj);
                    writer.write(testo);
                    writer.close();

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
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
                    System.out.println(username);
                    String command = (String) in.readObject();
                    System.out.println(command);
                    switch (command) {

                        case "entrata":
                            username = (String) in.readObject();
                            ThreadPoolExecutor executor_entrata = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
                            executor_entrata.execute(new ThreadEntrata(out));
                            break;

                        case "uscita":
                            username = (String) in.readObject();
                            ThreadPoolExecutor executor_uscita = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
                            executor_uscita.execute(new ThreadUscita(out));
                            break;

                        case "send":
                            Email send = (Email) in.readObject();
                            ThreadPoolExecutor executor_send = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
                            executor_send.execute(new ThreadSend(out, send));
                            break;

                        case "delete1":
                            username = (String) in.readObject();
                            int index = (int) in.readObject();
                            ThreadPoolExecutor executor_delete = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
                            executor_delete.execute(new ThreadDelete1(out, index));
                            break;

                        case "delete2":
                            username = (String) in.readObject();
                            int index2 = (int) in.readObject();
                            ThreadPoolExecutor executor_delete2 = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
                            executor_delete2.execute(new ThreadDelete2(out, index2));
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

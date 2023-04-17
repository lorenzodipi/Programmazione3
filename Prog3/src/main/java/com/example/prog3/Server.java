package com.example.prog3;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Server {


    //C:\Users\Lorenzo Di Palma\Desktop\MAIN\Progetti\Programmazione3\Prog3\src\main\java\com\example\prog3\mail\       papà
    ///Users/lorenzodipalma/Documents/GitHub/Programmazione3/Prog3/src/main/java/com/example/prog3/mail/     mac
    //C:\Users\loren\Desktop\MAIN\Programmazione3\Prog3\src\main\java\com\example\prog3\mail\            mamma
    String path = "C:\\Users\\loren\\Desktop\\MAIN\\Programmazione3\\Prog3\\src\\main\\java\\com\\example\\prog3\\mail\\";

    ///Users/lorenzodipalma/Documents/GitHub/Programmazione3/Prog3/username.txt     mac
    //C:\Users\Lorenzo Di Palma\Desktop\MAIN\Progetti\Programmazione3\Prog3\\username.txt       papà
    //C:\Users\loren\Desktop\MAIN\Programmazione3\Prog3\\username.txt        mamma
    String path_user ="C:\\Users\\loren\\Desktop\\MAIN\\Programmazione3\\Prog3\\username.txt";
    HashMap<String,File> hashMap = new HashMap<>();
    ArrayList<String> userList = new ArrayList<>();
    Socket socket;
    ServerSocket s;
    ObjectInputStream in;
    ArrayList<Email> server_email;
    private ListProperty<String> logList;
    private ObservableList<String> logListContent;
    String username;
    boolean running;
    ThreadPoolExecutor executor;


    public Server(){
        try {

            this.logListContent = FXCollections.observableList(new LinkedList<>());
            this.logList = new SimpleListProperty<>();
            this.logList.set(logListContent);

            logListContent.addListener(new ListChangeListener<String>() {
                @Override
                public void onChanged(Change<? extends String> change) {

                }
            });

            initializeHashMap();
            s = new ServerSocket(7);
            new Thread(new RunnableServer()).start();

        } catch (IOException e) {
            System.out.println("ERRORE ggggg");
        }
    }

    public ListProperty<String> getLogList(){
        return logList;
    }

    private void initializeHashMap() {
        try {
            File myObj = new File(path_user);
            FileReader reader = new FileReader(myObj);
            Scanner myReader = new Scanner(reader);

            while(myReader.hasNextLine()){
                String line = myReader.nextLine();
                hashMap.put(line,new File(path+line+".txt"));
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Email> getEmailEntrata(String username) throws IOException {
        server_email = new ArrayList<>();
        File myObj = new File(path+username+".txt");
        if (myObj.exists()){
            FileReader reader = new FileReader(myObj);
            Scanner myReader = new Scanner(reader);
            while (myReader.hasNextLine()) {
                String date = myReader.nextLine();
                String sender = myReader.nextLine();
                if(sender.equals(username)){
                    while (!myReader.nextLine().equals("------------------")){}
                }else {
                    String receiver = myReader.nextLine();
                    String object = myReader.nextLine();
                    String content = "";
                    String text;
                    while (!(text = myReader.nextLine()).equals("------------------"))
                        content = content + text;
                    Email email = new Email(sender, receiver, object, content);

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
                String date = myReader.nextLine();
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

    public void sendEmail(String[] m, Email send, String elenco) throws IOException {
        if(elenco.equals("")){
            File myObj = new File(path+username+".txt");
            FileReader reader = new FileReader(myObj);
            Scanner myReader = new Scanner(reader);
            String text= "";

            while(myReader.hasNextLine())
                text = text + myReader.nextLine() +"\n";

            if(text.length()>1)
                text = text.substring(0, text.length()-1);

            myReader.close();
            reader.close();


            synchronized (hashMap.get(username)){
                LocalDateTime datetime = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = datetime.format(format);
                FileWriter writer = new FileWriter(myObj);
                if(text.length()==0)
                    writer.append(formattedDateTime).append("\n").append("SERVER").append("\n").append(username).append("\n").append("ERRORE").append("\n").append("Non è stato possibile mandare una mail a "+send.getReceiver()+" perchè l'indirizzo è inesistente.").append("\n------------------");
                else
                    writer.append(text).append("\n").append(formattedDateTime).append("\n").append("SERVER").append("\n").append(username).append("\n").append("ERRORE").append("\n").append("Non è stato possibile mandare una mail a "+send.getReceiver()+" perchè l'indirizzo è inesistente.").append("\n------------------");
                writer.close();
            }
        }else {
            for (String user : m) {
                if(hashMap.get(user)!=null){

                    File myObj = new File(path+user+".txt");
                    FileReader reader = new FileReader(myObj);
                    Scanner myReader = new Scanner(reader);
                    String text= "";
                    while(myReader.hasNextLine())
                        text = text + myReader.nextLine() +"\n";

                    if(text.length()>1)
                        text = text.substring(0, text.length()-1);
                    myReader.close();
                    reader.close();



                    synchronized (hashMap.get(user)){
                        LocalDateTime datetime = LocalDateTime.now();
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        String formattedDateTime = datetime.format(format);
                        FileWriter writer = new FileWriter(myObj);
                        if(text.length()==0)
                            writer.append(formattedDateTime).append("\n").append(send.getSender()).append("\n").append(elenco).append("\n").append(send.getSubject()).append("\n").append(send.getText()).append("\n------------------");
                        else
                            writer.append(text).append("\n").append(formattedDateTime).append("\n").append(send.getSender()).append("\n").append(elenco).append("\n").append(send.getSubject()).append("\n").append(send.getText()).append("\n------------------");
                        writer.close();
                    }

                }
                else {
                    File myObj = new File(path+username+".txt");
                    FileReader reader = new FileReader(myObj);
                    Scanner myReader = new Scanner(reader);
                    String text= "";

                    while(myReader.hasNextLine())
                        text = text + myReader.nextLine() +"\n";

                    if(text.length()>1)
                        text = text.substring(0, text.length()-1);

                    myReader.close();
                    reader.close();

                    synchronized (hashMap.get(username)){
                        LocalDateTime datetime = LocalDateTime.now();
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        String formattedDateTime = datetime.format(format);
                        FileWriter writer = new FileWriter(myObj);
                        if(text.length()==0)
                            writer.append(formattedDateTime).append("\n").append("SERVER").append("\n").append(username).append("\n").append("ERRORE").append("\n").append("Non è stato possibile mandare una mail a "+user+" perchè l'indirizzo è inesistente.").append("\n------------------");
                        else
                            writer.append(text).append("\n").append(formattedDateTime).append("\n").append("SERVER").append("\n").append(username).append("\n").append("ERRORE").append("\n").append("Non è stato possibile mandare una mail a "+user+" perchè l'indirizzo è inesistente.").append("\n------------------");
                        writer.close();
                    }
                }
            }
        }

    }

    public void close() {
        /*try {
            Socket close = new Socket(InetAddress.getLocalHost(), 7);
            ObjectOutputStream out = new ObjectOutputStream(close.getOutputStream());

            out.flush();
            out.writeObject("");
            out.writeObject("close");
            running = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        running = false;
        executor.shutdown();
        try {
            s.close();
        } catch (IOException e) {
            System.err.println("Errore nella chiusura del socket: " + e.getMessage());
        }



    }

    class ThreadUser implements Runnable {
        ObjectOutputStream out;
        public ThreadUser(ObjectOutputStream out){
            this.out = out;
        }

        @Override
        public void run() {
            try {
                File myObj = new File(path_user);
                FileReader reader = new FileReader(myObj);
                Scanner myReader = new Scanner(reader);
                while (myReader.hasNextLine()) {
                    userList.add(myReader.nextLine());
                }
                reader.close();
                myReader.close();
                Random rand = new Random();
                username = userList.get(rand.nextInt(userList.size()));
                out.writeObject(username);

                Platform.runLater(() -> logList.add(username+" ha fatto l'accesso."));

            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
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

                out.writeObject(server_email);

            } catch (IOException e) {
                System.out.println("ERRORE NEL MANDARE ELENCO MAIL ENTRATA");
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
                out.writeObject(server_email);
            } catch (IOException e) {
                System.out.println("ERRORE NEL MANDARE ELENCO MAIL USCITA");
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
                String p = send.getReceiver()+" "+username;
                String[] m = p.split("[\\s,;]+");
                String elenco = "";
                for (String r : m) {
                    if(hashMap.get(r)!=null && !r.equals(m[m.length-1]))
                        elenco = elenco + r + " ";
                }
                sendEmail(m,send,elenco);

                Platform.runLater(() -> logList.add(username+" ha inviato una mail a "+send.getReceiver()+"."));

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

                    int i = 1;
                    String testo = "";
                    while ( myReader.hasNextLine()) {
                        String date = myReader.nextLine();
                        String sender = myReader.nextLine();

                        if(i != index || sender.equals(username)){
                            if (!sender.equals(username))
                                i = i + 1;
                            testo = testo + date + "\n" + sender + "\n";
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

                    Platform.runLater(() -> logList.add(username+" ha cancellato una mail."));



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

                    int i = 1;
                    String testo = "";
                    while ( myReader.hasNextLine()) {
                        String date = myReader.nextLine();
                        String sender = myReader.nextLine();

                        if(i != index || !sender.equals(username)){
                            if (sender.equals(username))
                                i = i + 1;
                            testo = testo + date + "\n" + sender + "\n";
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

                    Platform.runLater(() -> logList.add(username+" ha cancellato una mail."));

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
            executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
            running = true;
            while (running){
                try {

                    socket = s.accept();
                    System.out.println("Accettato socket");
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.flush();
                    in = new ObjectInputStream(socket.getInputStream());
                    username = (String) in.readObject();
                    String command = (String) in.readObject();
                    switch (command) {

                        case "username":
                            executor.execute(new ThreadUser(out));
                            break;

                        case "entrata":
                            executor.execute(new ThreadEntrata(out));
                            break;

                        case "uscita":
                            executor.execute(new ThreadUscita(out));
                            break;

                        case "send":
                            Email send = (Email) in.readObject();
                            executor.execute(new ThreadSend(out, send));
                            break;

                        case "delete1":
                            int index = (int) in.readObject();
                            executor.execute(new ThreadDelete1(out, index));
                            break;

                        case "delete2":
                            int index2 = (int) in.readObject();
                            executor.execute(new ThreadDelete2(out, index2));
                            break;

                        case "disconnect":
                            Platform.runLater(() -> logList.add(username+" si è disconnesso."));
                            break;

                        default:
                            System.out.println("default");
                            break;
                    }
                }catch (Exception e){
                    System.out.println("ERRORE");
                }
            }

            /*executor.shutdown();
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
            }*/
        }
    }
}

package cn.chu.com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @program: IdeaProjects
 * @description:
 * @author: ChuChu
 * @create: 2021-11-17
 **/


public class ChatServer {
    ArrayList clientOutputStreams;

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        //structure function
        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    tellEveryone(message);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    public void tellEveryone(String message) {
        Iterator it = clientOutputStreams.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    public void go() {
        clientOutputStreams = new ArrayList();
        try {
            ServerSocket serverSock1 = new ServerSocket(5000);

            //modify
            ServerSocket serverSock2 = new ServerSocket(5001);
            //end


            // always listen
            while(true) {
                Socket clientSocket1 = serverSock1.accept();
                //modify
                Socket clientSocket2 = serverSock2.accept();
                //end

                PrintWriter writer = new PrintWriter(clientSocket1.getOutputStream());
                //array
                clientOutputStreams.add(writer);

                //modify
                writer = new PrintWriter(clientSocket2.getOutputStream());
                //array
                clientOutputStreams.add(writer);
                //end

                Thread t1 = new Thread(new ClientHandler(clientSocket1));
                t1.start();
                System.out.println("got a connection to ChuChu");

                //modify
                Thread t2 = new Thread(new ClientHandler(clientSocket2));
                t2.start();
                System.out.println("got a connection to ChuFan");
                //end
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public static void main(String[] args) {
        new ChatServer().go();
    }
}

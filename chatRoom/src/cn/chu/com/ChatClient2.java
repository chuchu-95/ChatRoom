package cn.chu.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @program: IdeaProjects
 * @description:
 * @author: ChuChu
 * @create: 2021-11-22
 **/
public class ChatClient2 {
    JTextArea incoming2;
    JTextField outgoing2;
    BufferedReader reader2;
    PrintWriter writer2;
    Socket sock;

    public void go(){
        //setUpNetWorking
        //1. framework struct GUI
        JFrame frame = new JFrame("chat room ChuFan");
        JPanel mainPanel2 = new JPanel();

        incoming2 = new JTextArea(15, 50);
        incoming2.setLineWrap(true);
        incoming2.setWrapStyleWord(true);
        incoming2.setEditable(false);

        JScrollPane qScroller = new JScrollPane(incoming2);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        outgoing2 = new JTextField(20);

        JButton sentButton = new JButton("Send");
        sentButton.addActionListener(new SendButtonListener2());

        mainPanel2.add(qScroller);
        mainPanel2.add(outgoing2);
        mainPanel2.add(sentButton);

        setUpNetWorking();

        //2. start new thread, to read server's socket and show it on JTextArea
        Thread readerThread2 = new Thread(new ChatClient2.IncomingReader2());
        readerThread2.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel2);
        frame.setSize(500,450);
        frame.setVisible(true);

    }
    private void setUpNetWorking(){
        //set up Socket, PrintWriter
        try {
            // set up connection to server
            sock = new Socket("127.0.0.1", 5001);
            //client read from socket
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader2 = new BufferedReader(streamReader);

            //client write to server
            writer2 = new PrintWriter(sock.getOutputStream());
            System.out.println("networking established");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public class SendButtonListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                //get the content
                writer2.println("ChuFan: "+outgoing2.getText());
                writer2.flush();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            // after sending let the JTextField to None
            outgoing2.setText("");
            // mouse focus
            outgoing2.requestFocus();
        }
    }
    class IncomingReader2 implements Runnable{
        public void run() {
            String message;
            try {
                while ((message = reader2.readLine()) != null){
                    System.out.println("ChuFan client read " + message);
                    incoming2.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        ChatClient2 chatClient2 = new ChatClient2();
        chatClient2.go();
    }
}

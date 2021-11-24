package cn.chu.com;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @program: IdeaProjects
 * @description:
 * @author: ChuChu
 * @create: 2021-11-17
 **/
public class ChatClient1 {
    JTextArea incoming1;
    JTextField outgoing1;
    BufferedReader reader1;
    PrintWriter writer1;
    Socket sock;

    public void go(){
        //setUpNetWorking
        //1. framework struct GUI
        JFrame frame = new JFrame("chat room ChuChu");
        JPanel mainPanel = new JPanel();

        incoming1 = new JTextArea(15, 50);
        incoming1.setLineWrap(true);
        incoming1.setWrapStyleWord(true);
        incoming1.setEditable(false);

        JScrollPane qScroller = new JScrollPane(incoming1);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        outgoing1 = new JTextField(20);

        JButton sentButton = new JButton("Send");
        sentButton.addActionListener(new SendButtonListener1());

        mainPanel.add(qScroller);
        mainPanel.add(outgoing1);
        mainPanel.add(sentButton);

        setUpNetWorking();

        //2. start new thread, to read server's socket and show it on JTextArea
        Thread readerThread1 = new Thread(new IncomingReader1());
        readerThread1.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500,450);
        frame.setVisible(true);

    }
    private void setUpNetWorking(){
        //set up Socket, PrintWriter
        try {
            // set up connection to server
            sock = new Socket("127.0.0.1", 5000);
            //client read from socket
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader1 = new BufferedReader(streamReader);

            //client write to server
            writer1 = new PrintWriter(sock.getOutputStream());
            System.out.println("networking established");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public class SendButtonListener1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                //get the content
                writer1.println("ChuChu: "+outgoing1.getText());
                writer1.flush();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            // after sending let the JTextField to None
            outgoing1.setText("");
            // mouse focus
            outgoing1.requestFocus();
        }
    }
    class IncomingReader1 implements Runnable{
        public void run() {
            String message;
            try {
                while ((message = reader1.readLine()) != null){
                    System.out.println("ChuChu client read " + message);
                    incoming1.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        ChatClient1 chatClient1 = new ChatClient1();
        chatClient1.go();
    }
}




package chat_server;

import java.util.*;
import java.net.*;
import java.io.*;

public class Chat_server implements Runnable {

    private Socket socket = null;
    private ServerSocket server = null;
    ChatServerThread client = new ChatServerThread();
    private Thread thread = null;
    public static ArrayList<Socket> ClientConnections = new ArrayList<Socket>();
    public static ArrayList<String> UserList = new ArrayList<String>();

    public static void main(String[] args) {
        Chat_server server = new Chat_server(8001);
    }

    public Chat_server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server is up on port: " + port);
            System.out.println("version 3 ");
            start();
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void run() {
        System.out.println("INSIDE run");
        while (thread != null) {
            try {
                socket = server.accept();
                System.out.println("Client accepted: " + socket);
                ClientConnections.add(socket);
                addThread(socket);
            } catch (IOException ex) {
                System.out.println("Error:" + ex);
            }
        }
        close();

    }

    private void addThread(Socket socket) {

        client = new ChatServerThread(this, socket);
        try {
            //clients.open();
            //clients.start();
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }
    }
}

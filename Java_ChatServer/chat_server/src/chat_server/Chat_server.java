package chat_server;

import java.net.*;
import java.io.*;

public class Chat_server implements Runnable {

    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private Thread thread = null;

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

    public void run() {
        while (thread != null) {
            try {
                socket = server.accept();
                System.out.println("Client accepted: " + socket);

                boolean exit = false;

                while (!exit) {
                    try {

                        streamIn = new DataInputStream(socket.getInputStream());
                        BufferedReader d = new BufferedReader(new InputStreamReader(streamIn));
                        String strInput = d.readLine();

                        if (strInput.equals("HELO BASE_TEST")) {
                            
                            System.out.println("inside run 1 =" + strInput);
                            socket.getOutputStream();
                            streamOut = new DataOutputStream(socket.getOutputStream());
                            String strWelcome = strInput + "\nIP:10.62.0.81\nPort:8001\nStudentID:17317640\n";
                            streamOut.writeUTF(strWelcome);
                            streamOut.flush();

                        } else if (strInput.equals("KILL_SERVICE"))
                        {   
                            exit=true;
                            close();
                        }
                        else
                        {
                            //Joinchatroom
                            //Leavechatroom
                            //Message
                        }

                    } catch (IOException ex) {
                        exit = true;
                    }
                }

                close();
            } catch (IOException ie) {
                System.out.println("Error: " + ie);
            }
        }
    }

    public void start() {
        System.out.println("INnside start1");
        if (thread == null) {
            System.out.println("INnside start2");
            thread = new Thread(this);
            thread.start();
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

    public static void main(String[] args) {
        Chat_server server = new Chat_server(8001);
    }
}
package chat_server;

import java.util.*;
import java.net.*;
import java.io.*;

public class Chat_server implements Runnable {

    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private Thread thread = null;
    //public static ArrayList<Socket> ClientConnections = new ArrayList<Sockets>();
    //public static ArrayList<String> UserList = new ArrayList<String>();

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
                //addClientToList(socket);
                //ClientConnections.add(socket);
                System.out.println("Client accepted: " + socket);

                boolean exit = false;

                while (!exit) {
                    try {
                        socket.getOutputStream();
                        streamOut = new DataOutputStream(socket.getOutputStream());

                        streamIn = new DataInputStream(socket.getInputStream());
                        BufferedReader d = new BufferedReader(new InputStreamReader(streamIn));
                        String strInput = d.readLine();

                        String[] strPayloadParts = strInput.split("\n");

                        String strWelcome = "HELO BASE_TEST";
                        String strJoin = "JOIN_CHATROOM";
                        String strLeaveRoom = "LEAVE_CHATROOM";
                        String strChat = "CHAT";
                        String strDisconnect = "DISCONNECT";

                        HashMap<String, String> hmPayload = new HashMap<String, String>();

                        for (int i = 0; i <= (strPayloadParts.length); i++) {
                            String[] strKeyValues = strPayloadParts[i].split(":");
                            hmPayload.put(strKeyValues[0], strKeyValues[1]);
                        }

                        String strKey = strInput.substring(0, strInput.indexOf(':'));

                        if (strKey.equals(strWelcome)) {

                            System.out.println("inside strWelcome =" + strInput);
                            String strReply = strInput + "\nIP:10.62.0.81\nPort:8001\nStudentID:17317640\n";
                            streamOut.writeUTF(strReply);
                            streamOut.flush();

                        } else if (strKey.equals(strJoin)) {

                            //call Join function
                            String strRoomNo = "";
                            String strJoinId = "";
                            String strReply = "JOINED_CHATROOM:" + hmPayload.get("JOIN_CHATROOM")
                                    + "\nSERVER_IP:10.62.0.81\nPORT:8001\nROOM_REF:" + strRoomNo
                                    + "\nJOIN_ID:" + strJoinId + "\n";
                            streamOut.writeUTF(strReply);
                            streamOut.flush();

                        } else if (strKey.equals(strLeaveRoom)) {

                            //call Leave Room Function
                            String strRoomNo = "";
                            String strJoinId = "";
                            String strReply = " LEFT_CHATROOM:" + strRoomNo + "\nJOIN_ID:" + strJoinId + "\n";
                            streamOut.writeUTF(strReply);
                            streamOut.flush();

                        } else if (strKey.equals(strChat)) {

                            //Call Chat function
                            String strRoomNo = "";
                            String strClientName = "";
                            String strMessage = "";
                            String strReply = "CHAT:" + strRoomNo + "\nCLIENT_NAME="
                                    + strClientName + "\nMESSAGE=" + strMessage + "\n\n";
                            streamOut.writeUTF(strReply);
                            streamOut.flush();

                        } else if (strKey.equals(strDisconnect)) {

                            //Call Disconnect function
                            String strReply = "adios";
                            streamOut.writeUTF(strReply);
                            streamOut.flush();

                        } else {
                            String strReply = "ERROR_CODE:200\nERROR_DESCRIPTION:Invalid Message";
                            streamOut.writeUTF(strReply);
                            streamOut.flush();
                        }

                    } catch (IOException ex) {
                        System.out.println("Error:" + ex);
                        exit = true;
                    }
                }

                close();
            } catch (IOException ie) {
                System.out.println("Error: " + ie);
            }
        }
    }
    
    //public void synchronised Welcome(Socket S){}
    //public void synchronised Join(Socket S){}
    //public void synchronised Leave(Socket S){}
    //public void synchronised Chat(Socket S){}
    //public void synchronised Disconnect(Socket S){}

//    public static void synchronised AddClientToList(Socket S) {
//
//        DataInputStream objClientData = new DataInputStream(S.getInputStream());
//        BufferedReader d = new BufferedReader(new InputStreamReader(objClientData));
//        String strInput = d.readLine();
//    }

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

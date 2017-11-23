package chat_server;

import java.net.*;
import java.io.*;

public class Chat_server implements Runnable 
{
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream streamIn = null;
    private Thread thread = null;

    public Chat_server(int port) 
    {
        try 
        {
            server = new ServerSocket(port);
            System.out.println("Server is up on port: " + port);
            System.out.println("version 1 ");
            start();
        } 
        catch (IOException ex) 
        {
            System.out.println("Error: " + ex);
        }
    }

    public void run() 
    {
        while (thread != null) 
        {
            try 
            {
                socket = server.accept();
                System.out.println("1Client accepted: " + socket.getInputStream());
                streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                boolean exit = false;
                
                while (!exit) 
                {
                    try 
                    {
                        String line = streamIn.readUTF();
                        System.out.println("@@@@@@@@@@@@");
                        System.out.println(line);
                        //exit = line.equals("KILL_SERVICE\n");
                    } 
                    catch (IOException ex) 
                    {
                        exit = true;
                    }
                }
                
                close();
            } 
            catch (IOException ie) 
            {
                System.out.println("Error: " + ie);
            }
        }
    }

    public void start() 
    {
        if (thread == null) 
        {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void close() throws IOException 
    {
        if (socket != null) 
        {
            socket.close();
        }
        if (streamIn != null) 
        {
            streamIn.close();
        }
    }

    public static void main(String[] args) 
    {
        Chat_server server = new Chat_server(8001);
    }
}
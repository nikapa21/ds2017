package Test;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MenuListeningThread extends Thread{

    ServerSocket providerSocket;
    Socket connection = null;
    int port ;
    File requestedFile;

    public MenuListeningThread(int port) {
        // TODO Auto-generated constructor stub
        this.port = port;
    }

    @Override
    public void run(){

        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            //1) Create a socket to port 4321 and initialize its capacity to 10 connections
            providerSocket = new ServerSocket(port, 7776);


            while (true) {
                //2) Accept connections
                connection = providerSocket.accept();

                //3) Get input and output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());

                int flag = in.readInt();

                requestedFile = (File)in.readObject();

                if (requestedFile == null) {
                    System.out.println("The file you want doesn't exist");
                } else {
                    System.out.println("The requested file is: " + requestedFile);
                }

            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                providerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}

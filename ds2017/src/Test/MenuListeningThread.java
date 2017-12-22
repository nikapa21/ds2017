package Test;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MenuListeningThread extends Thread {

    ServerSocket providerSocket;
    Socket connection = null;
    File requestedFile;

    @Override
    public void run() {

        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            // Create a socket
            providerSocket = new ServerSocket(7776);

            while (true) {

                // Accept connections
                connection = providerSocket.accept();

                // Get input and output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());

                int flag = in.readInt();

                requestedFile = (File) in.readObject(); //read the requested file from server

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

package Test;

import Chord.FileEntry;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MenuListeningThread extends Thread {

    ServerSocket providerSocket;
    Socket connection = null;
    FileEntry requestedFile;

    @Override
    public void run() {

        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {

            //System.out.println("Number of active threads from the given thread: " + Thread.activeCount());

            // Create a socket
            providerSocket = new ServerSocket(7776);

            while (true) {

                // Accept connections
                connection = providerSocket.accept();

                // Get input and output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());

                int flag = in.readInt();

                requestedFile = (FileEntry) in.readObject(); //read the requested file from server

                if (requestedFile.getFileData() == null) {

                    System.out.println("The file " + requestedFile.getFile() + " with origin " + requestedFile.getOrigin() + " and destination " + requestedFile.getDestination() + " doesn't exist");
                    //TODO find the file from the Google API

                } else {

                    System.out.println("The requested file is: " + requestedFile);
                    System.out.println("The requested file name is: " + requestedFile.getFile().getName());
                    //We have the file, no need for Google API
                }
                //TODO Render the route
                Menu.printMenu();
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

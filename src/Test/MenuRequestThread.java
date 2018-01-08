package Test;

import Chord.FileEntry;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class MenuRequestThread extends Thread {

    FileEntry fileEntry;

    int port;
    int flag;

    public MenuRequestThread(FileEntry fileEntry, int port, int flag) {

        this.fileEntry = fileEntry;
        this.port = port;
        this.flag = flag;

    }

    private InetAddress findMyIp(){
        InetAddress ip = null;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();/*
            hostname = ip.getHostName();*//*
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);*/

        } catch (UnknownHostException e) {

            e.printStackTrace();
        }
        return ip;
    }

    public void run() {

        Socket requestSocket = null;
        /*ServerSocket providerSocket = null;
        Socket connection = null;
*/
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
/*
        ObjectOutputStream outListening = null;
        ObjectInputStream inListening = null;*/

        try {

            // Create a socket to the MasterNode ip and port (7777):
            requestSocket = new Socket("localhost", port);

            //System.out.println("menu is opening a socket to the master node's port " + port);//debug

            InetAddress myIp = findMyIp();
            //System.out.println("Menu has an IP " + myIp); debug

            // Get input and output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            /*connection = providerSocket.accept();

            // Get input and output streams
            outListening = new ObjectOutputStream(connection.getOutputStream());
            inListening = new ObjectInputStream(connection.getInputStream());
*/
            //int flag = in.readInt();

            //First step is to send the flag to the Master - Which action we will take.
            out.writeInt(flag);
            out.flush();

            if(flag==2){ //commit-save file

                out.writeObject(fileEntry);
                out.flush();
                out.writeObject(myIp);
                out.flush();

            }
            else if(flag==3) { // search file

                out.writeObject(fileEntry);
                out.flush();
                out.writeObject(myIp);
                out.flush();

                /*System.out.println("Waiting for the master's response ");
                FileEntry requestedFile = null;

                requestedFile = (FileEntry) in.readObject(); //read the requested file from Master-server

                if (requestedFile.getFileData() == null) {

                    System.out.println("The file " + requestedFile.getFile() + " with origin " + requestedFile.getOrigin() + " and destination " + requestedFile.getDestination() + " doesn't exist");
                    //TODO find the file from the Google API

                } else {

                    System.out.println("The requested file is: " + requestedFile);
                    System.out.println("The requested file name is: " + requestedFile.getFile().getName());
                    //We have the file, no need for Google API
                }*/

            }

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (Exception ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
               /* inListening.close();
                outListening.close();*/
                requestSocket.close();
                //providerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}

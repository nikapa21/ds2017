package Test;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class MenuRequestThread extends Thread {

    File file;
    String fileData;
    int port;
    int flag ;

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

    public MenuRequestThread(File file, int port, int flag) {

        this.file = file;
        this.port = port;
        this.flag = flag;

    }

    public MenuRequestThread(File file, String fileData, int port, int flag) {

        this.file = file;
        this.fileData = fileData;
        this.port = port;
        this.flag = flag;

    }

    public void run() {

        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {

            // Create a socket to the MasterNode ip and port (7777):
            requestSocket = new Socket("localhost", port);
            //System.out.println("menu is opening a socket to the master node's port " + port);//debug

            InetAddress myIp = findMyIp();
            //System.out.println("Menu has an IP " + myIp); debug

            // Get input and output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            //First step is to send the flag to the Master - Which action we will take.
            out.writeInt(flag);
            out.flush();

            if(flag==2){ //commit-save file

                out.writeObject(file);
                out.writeObject(fileData);
                out.writeObject(myIp);
                out.flush();

            }
            else if(flag==3) { // search file

                out.writeObject(file);
                out.writeObject(myIp);
                out.flush();

            }

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (Exception ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}

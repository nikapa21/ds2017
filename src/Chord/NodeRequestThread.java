package Chord;

import java.io.*;
import java.net.*;

public class NodeRequestThread extends Thread {

    int flag;
    Node n;
    int i;
    int fileKey;
    File file;
    FileEntry fileEntry;
    int counterForExistanceOfFile;
    String clientIp;

    public NodeRequestThread(Node n, int flag) { //constructor 1

        this.n = n;
        this.flag = flag;
    }

    public NodeRequestThread(int i, int flag) {//constructor 2

        this.i = i;
        this.flag = flag;
    }

    public NodeRequestThread(Node n, int fileKey, FileEntry fileEntry, int flag,int counterForExistanceOfFile, String clientIp) {//constructor 3

        this.n = n;
        this.flag = flag;
        this.fileKey = fileKey;
        this.fileEntry = fileEntry;
        this.counterForExistanceOfFile=counterForExistanceOfFile;
        this.clientIp = clientIp;

    }

    public NodeRequestThread(FileEntry fileEntry, int flag, String clientIp) {//constructor 4

        this.fileEntry = fileEntry;
        this.flag = flag;
        this.clientIp = clientIp;

    }

    //method to send to node the result
    public Node call() throws InterruptedException {

        Thread.sleep(2000);
        return n;

    }

    public void run() {

        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {

            //Create a socket
            if (flag == 2) { // send to another node

                requestSocket = new Socket("localhost", n.getPort());

            } else if (flag == 4) { //send to master node and he will send to menu

                //System.out.println("Sending back to Client IP: " + clientIp);
                requestSocket = new Socket("localhost", 7777);

            } else { //send to server

                requestSocket = new Socket("localhost", 7777);

            }

            // Get input and output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            out.writeInt(flag);//send the flag
            out.flush();

            if (flag == 0) { // initialize node from master

                out.writeObject(n);
                out.flush();

                n = (Node) in.readObject(); //read the initialized node

                //this is for test
                System.out.println("Result from Server > " + n.toString());

            } else if (flag == 1) { // for finger table

                out.writeInt(i);
                out.flush();

                n = (Node) in.readObject();

                System.out.println("flag = 1 > " + n.toString());

            } else if (flag == 2) { //search

                System.out.println("sending search request " + n.toString());
                out.writeInt(fileKey);
                out.flush();

                out.writeObject(fileEntry);
                out.flush();

                out.writeInt(counterForExistanceOfFile);
                out.flush();

                out.writeObject(clientIp);
                out.flush();

            } else if (flag == 4) {//return file to menu

                System.out.println("Sending reply to master and from there back to the menu with client IP: " + clientIp);
                out.writeObject(fileEntry);
                out.flush();

            } else if(flag ==5 ){

                out.writeObject(n);
                out.flush();

                if(n.files.size()>0){
                    System.out.println("successor's port is " + n.fingerTable[0].getPort());
                    requestSocket = new Socket("localhost", n.fingerTable[0].getPort());
                    out = new ObjectOutputStream(requestSocket.getOutputStream());

                    out.writeInt(flag);
                    out.flush();

                    out.writeObject(n.files);
                    out.flush();

                    out.writeObject(n.filesKeys);
                    out.flush();

                    out.writeObject(n.memory);
                    out.flush();

                    out.writeObject(n.memoryKeys);
                    out.flush();
                }

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
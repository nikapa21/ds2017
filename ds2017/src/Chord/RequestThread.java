package Chord;

import java.io.*;
import java.net.*;

public class RequestThread extends Thread {

    int flag;
    Node n;
    int i;
    int fileKey;
    File file;

    public RequestThread(Node n, int flag) {
        this.n = n;
        this.flag = flag;
    }

    public RequestThread(int i, int flag) {
        this.i = i;
        this.flag = flag;
    }

    public RequestThread(Node n,int fileKey, int flag) {
        this.n = n;
        this.flag = flag;
        this.fileKey = fileKey;
    }

    public RequestThread(File file, int flag) {
        this.file = file;
        this.flag = flag;
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


            if(flag == 2)   requestSocket = new Socket("localhost", n.getPort());
            else {
                //1) Create a socket to ip and to port 7777:
                requestSocket = new Socket("localhost", 7777);
            }


            //2) Get input and output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            out.writeInt(flag);
            out.flush();

            if (flag == 0) {//insert node

                //3) Write the integers one by one and flush the stream
                out.writeObject(n);
                out.flush();

                n = (Node) in.readObject();

                //this is for test
                System.out.println("Result from Server>" + n.toString());

            } else if (flag == 1) { // fingertable

                out.writeInt(i);
                out.flush();

                n = (Node) in.readObject();

                System.out.println("flag =1>" + n.toString());

            }
            else  if (flag == 2)//search
            {
                System.out.println("sending search request " + n.toString());
                out.writeInt(fileKey);
                out.flush();


                File fileReturned = (File) in.readObject();
                if(fileReturned != null)                System.out.println("file found from other node: " + n.toString());
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
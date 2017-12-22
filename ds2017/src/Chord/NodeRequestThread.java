package Chord;

import java.io.*;
import java.net.*;

public class NodeRequestThread extends Thread implements Serializable {

    int flag;
    Node n;
    int i;
    int fileKey;
    File file;
    int sucId;
    int port;

    public void setPort(int port) {
        this.port = port;
    }

    public NodeRequestThread(int flag) { //constructor 0

        this.flag = flag;

    }

    public NodeRequestThread(Node n, int flag) { //constructor 1

        this.n = n;
        this.flag = flag;

    }

    public NodeRequestThread(int i, int flag) {//constructor 2

        this.i = i;
        this.flag = flag;

    }

    public NodeRequestThread(Node n, int fileKey, int flag) {//constructor 3

        this.n = n;
        this.flag = flag;
        this.fileKey = fileKey;

    }

    public NodeRequestThread(Node n, int sucId,int port, int flag) {//constructor 3

        this.n = n;
        this.flag = flag;
        this.sucId = sucId;
        this.port = port;
    }

    public NodeRequestThread(File file, int flag) {//constructor 4

        this.file = file;
        this.flag = flag;

    }

    //method to send to node the result
    public Node call() throws InterruptedException {

        this.sleep(2000);
        return n;

    }

    public void run() {

        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            System.out.println("flag in node request" + flag);
            //Create a socket
            if (flag == 2  ) { // send to another node

                requestSocket = new Socket("localhost", n.getPort());

            }else if (flag == 6 || flag == 7 || flag == 8 || flag == 9) { //send to another node


                requestSocket = new Socket("localhost", port);

            } else if (flag == 3) { //send to menu

                requestSocket = new Socket("localhost", 7776);

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
                System.out.println("Result from Server>" + n.toString());


            } else if (flag == 1) { // for finger table

                out.writeInt(i);
                out.flush();

                n = (Node) in.readObject();

                System.out.println("flag =1>" + n.toString());

            } else if (flag == 2) { //search

                System.out.println("sending search request " + n.toString());
                out.writeInt(fileKey);
                out.flush();

            } else if (flag == 3) { // file to menu for testing

                System.out.println("return file to menu " + file.getName());
                out.writeObject(file);
                out.flush();

            } else if (flag == 4) {//return file to menu

                out.writeObject(file);
                out.flush();

            }
            else  if(flag == 5)//return
            {
                out.writeObject(n);
                out.flush();
                n = (Node) in.readObject();
                //System.out.println("flag =5>" + n.toString());
            }
            else  if(flag == 6)//search for closest first successor
            {
                //System.out.println("flag =6>" + sucId);
                out.writeInt(sucId);
                out.flush();
                n = (Node) in.readObject();
                //System.out.println("flag =6> in readobject " + n.toString());
            }
            else  if(flag == 7)//search for closest first successor
            {
                System.out.println("flag =7> " + sucId);
                out.writeInt(sucId);
                out.flush();
                n = (Node) in.readObject();
                //System.out.println("flag =7> in readobject " + n.toString());
            }
            else  if(flag == 8)//notify
            {
                out.writeObject(n);
                out.flush();
                //n = (Node) in.readObject();
            }
            else if(flag == 9)//return self
            {
                //out.writeObject(n);
                //out.flush();
                n = (Node) in.readObject();
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
                return;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
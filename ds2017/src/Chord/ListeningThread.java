package Chord;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ListeningThread extends Thread {

    ServerSocket providerSocket;
    Socket connection = null;
    int port;
    int id;
    Node n;
    boolean a;
    ArrayList<Integer> files = new ArrayList<Integer>();

    public ListeningThread(int port, int id, Node n) {
        // TODO Auto-generated constructor stub
        this.port = port;
        this.id = id;
        this.n = n;
        System.out.println("sdadas " + port);
    }


    @Override
    public void run() {

        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            //1) Create a socket to port 4321 and initialize its capacity to 10 connections
            providerSocket = new ServerSocket(port, 100);


            while (true) {
                //2) Accept connections
                connection = providerSocket.accept();

                //3) Get input and output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());

                //4) Read two integers from the stream

                int flag = (int) in.readInt();

                if (flag == 0) {
                    a = in.readBoolean();

                    if (a) {
                        n.calculateFinger();
                        n.printFinger();
                    }
                } else if (flag == 1) {
                    ActionNode an = new ActionNode(out, in, files);
                    an.start();

                }


            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException e) {
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


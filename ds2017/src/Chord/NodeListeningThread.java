package Chord;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class NodeListeningThread extends Thread {

    ServerSocket providerSocket;
    Socket connection = null;
    int port;
    int id;
    Node n;
    boolean a;
    ArrayList<File> files = new ArrayList<File>();
    ArrayList<Integer> filesKeys = new ArrayList<Integer>();

    public NodeListeningThread(int port, int id, Node n) {

        this.port = port;
        this.id = id;
        this.n = n;
        System.out.println(port); //for debug

    }

    @Override
    public void run() {

        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {

            // Create a socket
            providerSocket = new ServerSocket(port, 100);

            while (true) {

                // Accept connections
                connection = providerSocket.accept();

                // Get input and output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());

                int flag = (int) in.readInt(); //read the flag

                if (flag == 0) { //new node arrived update finger table

                    a = in.readBoolean();

                    if (a) {

                        n.calculateFinger();
                        n.printFinger();

                    }

                } else if (flag == 1) { //commit file

                    NodeActionForClients an = new NodeActionForClients(out, in, files, flag, null, filesKeys);
                    an.start();

                } else if (flag == 2) { //search file

                    NodeActionForClients an = new NodeActionForClients(out, in, files, flag, n, filesKeys);
                    an.start();

                }
                else if (flag == 6) { //search for fist successor
                    //System.out.println("start searching for successor 6");
                    NodeActionForClients an = new NodeActionForClients(out, in, flag, n);
                    an.start();

                }
                else if (flag == 7) { //search for fist successor
                    //System.out.println("start searching for successor 7");
                    NodeActionForClients an = new NodeActionForClients(out, in, flag, n);
                    an.start();

                }
                else if (flag == 8) { //search for fist successor
                    NodeActionForClients an = new NodeActionForClients(out, in, flag, n);
                    an.start();

                }
                else if (flag == 9) { //return self
                    //NodeActionForClients an = new NodeActionForClients(out, in, flag, n);
                    //an.start();
                    out.writeObject(n);
                    out.flush();
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


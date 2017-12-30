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


    public NodeListeningThread(int port, int id, Node n) {

        this.port = port;
        this.id = id;
        this.n = n;
        System.out.println(port); //for debug

    }

    void printFiles() {

        for (int i = 0; i < n.files.size(); i++) {

            System.out.println(n.files.get(i).getName());

        }

    }

    void printFilesKeys() {

        for (int i = 0; i < n.filesKeys.size(); i++) {

            System.out.println("the size is " + n.filesKeys.size());
            System.out.println(n.filesKeys.get(i));

        }
    }

    @Override
    public void run() {

        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {

            System.out.println("Number of active threads from the given thread: " + Thread.activeCount());


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

                    NodeActionForClients an = new NodeActionForClients(out, in, n.files, flag, null, n.filesKeys,n.memoryKeys,n.memory);
                    an.start();

                } else if (flag == 2) { //search file

                    NodeActionForClients an = new NodeActionForClients(out, in, n.files, flag, n, n.filesKeys,n.memoryKeys,n.memory);
                    an.start();

                } else if(flag==4){
                    a = in.readBoolean();
                    if(!a){
                        n.calculateFinger();
                        n.printFinger();
                    }
                } else if(flag==5){
                    ArrayList<File>  ar = (ArrayList<File>) in.readObject();
                    ArrayList<Integer> ar1 = (ArrayList<Integer>) in.readObject();

                    n.files.addAll(ar);
                    n.filesKeys.addAll(ar1);
                    printFiles();
                    printFilesKeys();

                    ArrayList<File>  mem = (ArrayList<File>) in.readObject();
                    ArrayList<Integer> memKeys = (ArrayList<Integer>) in.readObject();

                    n.memory.addAll(mem);
                    n.memoryKeys.addAll(memKeys);
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


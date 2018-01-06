package Master;

import Chord.FileEntry;
import Chord.Node;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MasterNode {

    ArrayList<Node> catalogueOfNodes = new ArrayList<Node>(); // catalogue for all the nodes in system
    int port = 7777; // port of server
    //int id = 1;

    public static void main(String args[]) {

        new MasterNode().openServer();  //open server

    }

    ServerSocket providerSocket;
    Socket connection = null;

    void openServer() {

        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            //System.out.println("Number of active threads from the given thread: " + Thread.activeCount()); //debug

            // Create a socket to port 7777
            providerSocket = new ServerSocket(port);

            System.out.println("The server (Master node) is now open at port: " + port); //debug

            while (true) {

                // Accept connections
                connection = providerSocket.accept();

                // Get input and output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());

                int flag = (int) in.readInt(); // read the flag, so to know what to do

                if (flag == 0) { // insert node

                    insertNode(out,in);

                } else if (flag == 1) { // for finger table

                    MasterActionForClients a = new MasterActionForClients(catalogueOfNodes, out, in, 0);
                    a.start();

                } else if (flag == 2) { // commit file

                    MasterActionForClients a = new MasterActionForClients(catalogueOfNodes, out, in, 1);
                    a.start();

                } else if (flag == 3) { // search file

                    MasterActionForClients a = new MasterActionForClients(catalogueOfNodes,out,in,2);
                    a.start();

                } else if (flag == 4) { // receive the requested file from node and sending back to user

                    FileEntry requestedFile = (FileEntry) in.readObject();
                    System.out.println("Received the requested file from node " );

                    //create a new request to menu to return the file
                    MasterRequestThread mrt = new MasterRequestThread(7776, requestedFile, 3);
                    mrt.start();

                } else if(flag == 5){

                    Node n= (Node) in.readObject();
                   // catalogueOfNodes.remove(n);

                    System.out.println("The size is : " + catalogueOfNodes.size());

                    for (int i = 0; i < catalogueOfNodes.size(); i++) {
                       if(catalogueOfNodes.get(i).getId()==n.getId()){
                           catalogueOfNodes.remove(i);
                       }

                    }

                    System.out.println("The size is : " + catalogueOfNodes.size());

                    for (int i = 0; i < catalogueOfNodes.size(); i++) {
                        MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get(i).getPort(), 4);
                        mrt.start();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                providerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    void AddNodeToList(Node n)
    {
        //add the node to the list
        catalogueOfNodes.add(n);

        Collections.sort(catalogueOfNodes, new Comparator<Node>() { // sort the catalogue of nodes by id
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getId() > o2.getId())
                    return 1;
                else if (o1.getId() < o2.getId())
                    return -1;
                else
                    return 0;
            }
        });

        /*//print the nodes,for test
        for (int i = 0; i < catalogueOfNodes.size(); i++) {
            System.out.println(catalogueOfNodes.get(i).toString());
        }*/

    }

    void insertNode( ObjectOutputStream out, ObjectInputStream in) {

        // Read the node from the stream

        try {
            Node n = null;
            n = (Node) in.readObject();

            port = port + 1;

            //set the port and id of the node
            n.setPort(port);
            // n.setId(catalogueOfNodes.get(catalogueOfNodes.size() - 1).getId() + 1);

            AddNodeToList(n);

            //send the node with the new port and id to the stream
            out.writeObject(n);
            out.flush();

            //inform all the other nodes that a node is inserted in system
            for (int i = 0; i < catalogueOfNodes.size(); i++) {
                if (catalogueOfNodes.get(i).getId() != n.getId()) {
                    MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get(i).getPort(), 0);
                    mrt.start();
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
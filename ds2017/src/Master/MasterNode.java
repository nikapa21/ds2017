package Master;

import Chord.Node;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MasterNode {

    ArrayList<Node> catalogueOfNodes = new ArrayList<Node>();
    int port = 7777;
    //int id = 1;

    public static void main(String args[]) {

        new MasterNode().openServer();

    }

    ServerSocket providerSocket;
    Socket connection = null;

    void openServer() {

        System.out.println("The server (Master node) is now open");
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            //1) Create a socket to port 7777 and initialize its capacity to 100 connections
            providerSocket = new ServerSocket(port);

            while (true) {

                //2) Accept connections
                connection = providerSocket.accept();

                //3) Get input and output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());

                int flag = (int) in.readInt();

                if (flag == 0) { // insert node
                    //4) Read the node from the stream
                    Node n = (Node) in.readObject();
                    port = port + 1;

                    //set the port and id of the node

                    n.setPort(port);
                    // n.setId(catalogueOfNodes.get(catalogueOfNodes.size() - 1).getId() + 1);

                    //add the node to the list
                    catalogueOfNodes.add(n);

                    Collections.sort(catalogueOfNodes, new Comparator<Node>() {
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

                    //send the node with the new port and id to the stream
                    out.writeObject(n);
                    out.flush();

                    //print the nodes
                    for (int i = 0; i < catalogueOfNodes.size(); i++) {
                        System.out.println(catalogueOfNodes.get(i).toString());
                    }

                    int flag2 = 0;
                    //inform nodes that a node is inserted in system
                    for (int i = 0; i < catalogueOfNodes.size(); i++) {
                        MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get(i).getPort(), flag2);
                        mrt.start();
                    }

                } else if (flag == 1) { // for finger table

                    Action a = new Action(catalogueOfNodes, out, in, 0);
                    a.start();

                } else if (flag == 2) {

                    int flag2 = 1;

                    Action a = new Action(catalogueOfNodes, out, in, flag2);
                    a.start();

                } else if (flag == 3) {

                    File file = (File) in.readObject();
                    int flag2 = 2;
                    String sha1Hash = HashGeneratorUtils.generateSHA1(file.getName());
                    int fileKey = new BigInteger(sha1Hash, 16).intValue();
                    fileKey = fileKey % 64;
                    MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get(0).getPort(), file, flag2,fileKey);
                    mrt.start();
                    File requestFile = mrt.call();
                    mrt.join();
                    out.writeObject(requestFile);
                    out.flush();

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
}
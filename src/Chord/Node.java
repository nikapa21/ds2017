package Chord;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

public class Node implements Serializable {

    static int flag;
    String addr;
    int port;
    int id;
    int m = 6; // the size of chord system is 2^m
    Node[] fingerTable = new Node[m];
    ArrayList<FileEntry> files = new ArrayList<FileEntry>();
    ArrayList<Integer> filesKeys = new ArrayList<Integer>();
    ArrayList<FileEntry> memory = new ArrayList<FileEntry>();
    ArrayList<Integer> memoryKeys = new ArrayList<Integer>();

    public Node(String addr, int id) {

        this.addr = addr;
        this.id = id;

    }

    public Node(String addr, int port, int id) {

        this.addr = addr;
        this.port = port;
        this.id = id;

    }

    public static void main(String[] args) {

        //int a = Integer.parseInt(args[0]); //get the id as parameter

        //create the node
        Node n = new Node("localhost", 0, 44);

        flag = 0;

        //call the client thread to initialize the node
        NodeRequestThread rt = new NodeRequestThread(n, flag);

        //start the thread
        rt.start();

        // take the result from server
        try {
            n = rt.call();

            //wait until the thread finished
            rt.join();

            //test to see if node has correct port and id
            System.out.println(n.toString());

            //create the server thread of node that is always open and wait
            NodeListeningThread lt = new NodeListeningThread(n.getPort(), n.getId(), n);
            lt.start();

            n.calculateFinger();
            n.printFinger();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Node finalN = n;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                gracefulFailover(finalN);
            }
        }));

    }


    //calculate successor
    public int findSuccessor(int id, int i) {
        return (int) (id + Math.pow(2, i - 1));
    }

    //calculate finger table
    public void calculateFinger() {

        Node temp;
        flag = 1;

        for (int i = 1; i <= m; i++) {

            int j = findSuccessor(this.id, i);
            j = (int) (j % Math.pow(2, m));
            System.out.println("Node " + id + " asks the master about node " + j + " while calculating finger table");

            NodeRequestThread rft = new NodeRequestThread(j, flag);

            rft.start();

            try {
                temp = rft.call();


                rft.join();

                fingerTable[i - 1] = temp;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Finger table of Node " + id + " is "+fingerTable);
    }

    public static void gracefulFailover(Node n){
        NodeRequestThread nrt = new NodeRequestThread(n,5);
        nrt.start();
        try {
            nrt.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //look up for the file
    public void lookUp(int fileKey, FileEntry fileEntry, int counterForExistenceOfFile, String clientIp) {


        counterForExistenceOfFile++;

        System.out.println("inside lookup. Number of hops " + counterForExistenceOfFile + " and FileKey " + fileKey); //debug

        for (int i = fingerTable.length - 1; i >= 0; i--) { //looking in finger table for a node id <= filekey

            System.out.println("inside lookup for ,node: " + fingerTable[i].getId());

            if (Math.abs(fileKey % 64)>= fingerTable[i].getId() && fingerTable[i].getId() > id) {

                System.out.println("request thread from node: " + fingerTable[i].getId());

                NodeRequestThread rt = new NodeRequestThread(fingerTable[i], fileKey, fileEntry, 2, counterForExistenceOfFile, clientIp);
                rt.start();

                return;

            }

        }

        System.out.println("no node found in the finger table");

        //if no such node found in the finger table look in the successor
        NodeRequestThread rt = new NodeRequestThread(fingerTable[0], fileKey, fileEntry, 2,counterForExistenceOfFile, clientIp);
        rt.start();
    }

    public void printFinger() {

        for (int i = 0; i < m; i++) {

            System.out.println(fingerTable[i]);

        }

    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Node [addr=" + addr + ", port=" + port + ", id=" + id + "]";
    }

}

package Chord;

import java.io.Serializable;

public class Node implements Serializable {

    static int flag;


    public static void main(String[] args) throws InterruptedException {

        //create the node
        Node n = new Node("localhost", 0, 56);

        flag=0;

        //call the thread to initialize the node
        RequestThread rt = new RequestThread(n,flag);
        //start the thread
        rt.start();
        // take the result from server
        n=rt.call();
        //wait until the thread finished
        rt.join();

        //test to see if node has correct port and id
        System.out.println(n.toString());

        //create the server thread of node that is always open and wait
        ListeningThread lt = new ListeningThread(n.getPort(), n.getId(),n);
        lt.start();


        n.calculateFinger();
        n.printFinger();


    }

    String addr;
    int port;
    static int counter = 0;
    int id;
    int m = 6;
    Node[] fingerTable = new Node[m];

    public Node(String addr, int port, int id) {
        this.addr = addr;
        this.port = port;
        this.id = id;
        counter++;

    }

    public int findSuccessor(int id, int i) {
        return (int) (id + Math.pow(2, i - 1));
    }

    public void calculateFinger( ) throws InterruptedException {
        //fingerTable = new Node[m];
        Node temp;//=new Node("localhost",0,0);// = new Node("localhost",0,0);
        flag=1;


        for (int i = 1; i <= m; i++) {

            int j = findSuccessor(this.id,i);
            j = (int) (j%Math.pow(2,m));
            RequestThread rft = new RequestThread(j,flag);

            rft.start();

            temp=rft.call();

            rft.join();

            fingerTable[i-1]=temp;


        }
    }

    public void printFinger() {
        for (int i = 0; i < m; i++) {
            System.out.println(fingerTable[i] + "sd");
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

    public void setId(int id) {
        this.id = id;
    }

    public void lookUp() {

    }

    @Override
    public String toString() {
        return "Node [addr=" + addr + ", port=" + port + ", id=" + id + "]";
    }

}

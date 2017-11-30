package Chord;

import java.io.Serializable;

public class Node implements Serializable {

    static int flag;

    public static void main(String[] args) throws InterruptedException {

        int a = Integer.parseInt(args[0]);

        //create the node
        Node n = new Node("localhost", 0, a);

        flag = 0;

        //call the thread to initialize the node
        RequestThread rt = new RequestThread(n, flag);
        //start the thread
        rt.start();
        // take the result from server
        n = rt.call();
        //wait until the thread finished
        rt.join();

        //test to see if node has correct port and id
        System.out.println(n.toString());

        //create the server thread of node that is always open and wait
        ListeningThread lt = new ListeningThread(n.getPort(), n.getId(), n);
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

    public void calculateFinger() throws InterruptedException {

        Node temp;
        flag = 1;

        for (int i = 1; i <= m; i++) {

            int j = findSuccessor(this.id, i);
            j = (int) (j % Math.pow(2, m));

            RequestThread rft = new RequestThread(j, flag);

            rft.start();

            temp = rft.call();

            rft.join();

            fingerTable[i - 1] = temp;

        }
    }

    public int lookUp(int file) {

        for (int i = fingerTable.length - 1; i >= 0; i--) {
            if (file == fingerTable[i].getId()) {
                return fingerTable[i].getPort();

            }
        }
        return 0;

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

    @Override
    public String toString() {
        return "Node [addr=" + addr + ", port=" + port + ", id=" + id + "]";
    }


}

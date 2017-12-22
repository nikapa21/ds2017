package Chord;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.io.Serializable;

public class Node implements Serializable {

    static int flag;
    public static boolean counterForLookUp = false;
    String addr;
    int port;
    int id;
    int m = 6; // the size of chord system is 2^m
    Node[] fingerTable = new Node[m];
    public  Node predecessor;
    int next = 0;
    NodeRequestThread[] updateFingerTable;

    public Node(String addr, int port, int id) {

        this.addr = addr;

        this.port = port;
        this.id = id;

    }

    public static void main(String[] args) throws InterruptedException {

        //int a = Integer.parseInt(args[0]); //get the id as parameter
        //create the node
        Node n = new Node("localhost", 0, 7);


        flag = 0;

        //call the client thread to initialize the node
        NodeRequestThread rt = new NodeRequestThread(n, flag);

        //start the thread
        rt.start();

        // take the result from server
        n = rt.call();

        //wait until the thread finished
        rt.join();

        //test to see if node has correct port and id
        System.out.println(n.toString());

        //create the server thread of node that is always open and wait
        NodeListeningThread lt = new NodeListeningThread(n.getPort(), n.getId(), n);
        lt.start();

        //get a random node from server to start join procedure
        flag = 5;
        NodeRequestThread randomNodeRequest = new NodeRequestThread(n, flag);
        //start the thread
        randomNodeRequest.start();
        // take the result from server
        Node randomNode = randomNodeRequest.call();
        //wait until the thread finished
        randomNodeRequest.join();
        System.out.println("request random node " + randomNode);
        n.updateFingerTable = new NodeRequestThread[n.m];

        if(randomNode == null)// create a new Chord ring.
        {
           //don t do anything. only one node in the system
            System.out.println("random node is null " + randomNode);
            for(int i = 0;i< n.fingerTable.length; i++) {
                n.fingerTable[i] = n;
            }

        }
        else // join a Chord ring containing node random node
        {
           //randomNode.FindSuccessor

            //search for position inside the circle
            flag = 6;
            Node n1 = null;
            while (n1 == null)
            {
                NodeRequestThread joinRequest = new NodeRequestThread(n1,n.getId()+1,randomNode.getPort(), flag);
                //start the thread
                joinRequest.start();
                // take the result from server
                n1 = joinRequest.call();
                n.fingerTable[0] = n1;
                //wait until the thread finished
                joinRequest.join();
                System.err.println("first successor " + n.fingerTable[0]);

                for(int i = 1;i< n.fingerTable.length; i++) {
                    n.fingerTable[i] = n;
                }
            }


            n.fingerTable[0] = n.FindRealNode(n.fingerTable[0]);
        }


        Stabilizer stab = new Stabilizer(n);
        stab.start();

        //n.calculateFinger();
        //n.printFinger();

    }

    public  void  UpdateFingerTable()
    {
        this.printFinger();
        //create finger table
        flag = 7;
        NodeRequestThread searchSucRequest;
        for(int i = 1;i<= this.fingerTable.length; i++)
        {
            System.out.println("updating suc id "+this.findSuccessor(this.id,i)+"  " + this.fingerTable[0]);
            searchSucRequest = new NodeRequestThread(this.fingerTable[0],this.findSuccessor(this.id,i),this.id, flag);
            //start the thread
            searchSucRequest.start();
            // take the result from server
            try {
                this.fingerTable[i-i] = searchSucRequest.call();
                //wait until the thread finished
                searchSucRequest.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        this.printFinger();
    }


    // ask node n to find the successor of id
    public Node FindSuccessor(int sucId)
    {
        System.out.println("find successor " + sucId + " "+this.toString() + " "+ this.fingerTable[0].toString());
       if(Node.BelongsTo(sucId,this.getId(),this.fingerTable[0].getId()))//  sucId > this.getId() && sucId <= this.fingerTable[0].getId())
        {
            //System.out.println("between");
            return this.fingerTable[0];
        }
        else
        {
            //System.out.println("not between");
            flag = 7;
            Node n0 = new Node("default",1,-1);
            NodeRequestThread closestPreReq = new NodeRequestThread(n0,sucId,this.fingerTable[0].getPort(), flag);
            //start the thread
            closestPreReq.start();
            // take the result from server
            try {
                n0 = closestPreReq.call();
               // System.out.println("closest precending returnsed " + n0.toString());
                //wait until the thread finished
                closestPreReq.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //System.out.println("first successor " + n.fingerTable[0]);
            //Node n0 = fingerTable[0].ClosestPrecendingNode(sucId);

            Node n1 = new Node("default",1,-1);
            flag = 6;
            closestPreReq = new NodeRequestThread(n1,sucId,n0.getPort(), flag);
            //start the thread
            closestPreReq.start();
            // take the result from server
            try {
                n1 = closestPreReq.call();
                //wait until the thread finished
                closestPreReq.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return n1;
            //return n0.FindSuccessor(sucId);
        }

    }

    // ask node n to find the successor of id
    public Node FindSuccessor2(int sucId) {

        //if the id is this node's id
        if(sucId == getId()) return this;
        //else if it is not
        Node tempNode = FindPredecessor(sucId);
        System.out.println("Successor of id "+sucId + " is " +tempNode.fingerTable[0]);
        return tempNode.fingerTable[0];
        //return tempNode.fingerTable[0];

    }

    Node FindPredecessor(int sucId)
    {
        Node tempNode = this;
        while (!(Node.BelongsTo(sucId,tempNode.getId(),tempNode.fingerTable[0].getId()) || sucId == tempNode.fingerTable[0].getId()))
        {
            if(tempNode == this) tempNode = tempNode.ClosestPrecendingNode(sucId);
            else
            {

                flag = 7;
                NodeRequestThread closestPreReq = new NodeRequestThread(tempNode,sucId,tempNode.getPort(), flag);
                //start the thread
                closestPreReq.start();
                // take the result from server
                try {
                    tempNode = closestPreReq.call();
                    //wait until the thread finished
                    closestPreReq.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempNode;
    }

    Node FindRealNode(Node requestedNode)
    {
        System.out.println("find real ");
        if(requestedNode == this) return this;
        if(requestedNode == null)
        {
            System.err.println("Error find real node is null");
            return null;
        }
          flag = 9;
          Node tempNode = null;
        while (tempNode == null)
        {
            NodeRequestThread closestPreReq = new NodeRequestThread(null,-1,requestedNode.getPort(), flag);
            //start the thread
            closestPreReq.start();
            // take the result from server
            try {
                tempNode = closestPreReq.call();
                //wait until the thread finished
                closestPreReq.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return tempNode;
    }

    Node FindRealNode(Node requestedNode,int i)
    {
        if(updateFingerTable[i] != null && updateFingerTable[i].isAlive()) return null;
        if(requestedNode == this) return this;
        if(requestedNode == null)
        {
            System.err.println("Error find real node is null");
            return null;
        }
        flag = 9;
        Node tempNode = null;
        updateFingerTable[i] = new NodeRequestThread(null,-1,requestedNode.getPort(), flag);
        //start the thread
        updateFingerTable[i].start();
        // take the result from server
        try {
            tempNode =  updateFingerTable[i].call();
            //wait until the thread finished
            updateFingerTable[i].join(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return tempNode;
    }


    // search the local table for the highest predecessor of id
    public  Node ClosestPrecendingNode(int sucId)
    {
        for (int i = fingerTable.length-1; i >= 0; i--)
        {
            if(fingerTable[i] != null &&  Node.BelongsTo(fingerTable[i].getId(),this.getId(),sucId))
            {
                return  fingerTable[i];
            }
        }
        return this;
    }


    public  void  Stabilize()
    {
        System.out.println("Number of active threads from the given thread: " + Thread.activeCount());
        if(this.fingerTable[0] == null) return;
        Node x = this.fingerTable[0].predecessor;

        if(x!=null && Node.BelongsTo(x.getId(),this.getId(),this.fingerTable[0].getId()))
        {
            System.err.println("Stabilized found successor to be "+x);
            this.fingerTable[0] = x;
           /* Node realNode;
            realNode = FindRealNode(this.fingerTable[0]);
            if((realNode != null))            this.fingerTable[0] = realNode;*/
            //this.fingerTable[0] = FindRealNode(this.fingerTable[0]);
        }
        if(this == fingerTable[0] || this.fingerTable[0] == this) return;//don t want to notify myself or my successor if i know i m his predecessor
        //send notify request to the node this.fingerTable[0]
        flag = 8;
        NodeRequestThread notifyRequest = new NodeRequestThread(this,-1,this.fingerTable[0].getPort(), flag);
        //start the thread
        notifyRequest.start();
    }

    // called periodically. refreshes finger table entries.
    // next stores the index of the finger to fix
    public  void  FixFingers()
    {

        //int i = (int)(Math.random()*((this.fingerTable.length-1 - 1) + 1) + 1);
        next++;
        if(next > fingerTable.length) next = 1;
        int i = next;
        Node realNode;
        if(i== 1)
        {
            //int k = fingerTable[i-1].getId();
            realNode = FindRealNode(fingerTable[i-1],i-1);
            if((realNode != null))            fingerTable[i-1] = realNode;
        }
        else
        {
            fingerTable[i-1] = this.FindSuccessor2(findSuccessor(this.getId(),i));
            //we dont need to know the real node of these nodes. Just our successors real node
            realNode = FindRealNode(fingerTable[i-1],i-1);
            if((realNode != null))            fingerTable[i-1] = realNode;
        }

        //System.out.println("Print predecessor of "+this.fingerTable[i-1] + " is " +this.fingerTable[i-1].predecessor+" at position "+i +" with id "+findSuccessor(this.getId(),i));

    }

    // called periodically. checks whether predecessor has failed.
    public void Check_Predecessor()
    {

    }


    // k thinks it might be this node's predecessor.
    public void Notify(Node k)
    {
        System.out.println("In notify from " +k);
        if(this.fingerTable[0] == this && k != this)
        {
            System.err.println("Change successor from " +this.fingerTable[0]);
            this.fingerTable[0] = k;
            System.err.println("Change successor to " +k);
        }
        if(this.predecessor == null || (Node.BelongsTo(k.getId(),this.predecessor.getId(),this.getId())))
        {
            System.err.println("Change predecessor of from " +this.predecessor + " to " +k);
            this.predecessor = k;
        }
    }

    static boolean BelongsTo(int id,int a,int b)
    {
        if(a<b) return(a<id && id<b);
        else    return(id>a || id<b);
    }


      //calculate successor
    public int findSuccessor(int id, int i) {
        return (int) (id + Math.pow(2, i - 1));
    }

    //calculate finger table
    public void calculateFinger() throws InterruptedException {

        Node temp;
        flag = 1;

        for (int i = 1; i <= m; i++) {

            int j = findSuccessor(this.id, i);
            j = (int) (j % Math.pow(2, m));

            NodeRequestThread rft = new NodeRequestThread(j, flag);

            rft.start();

            temp = rft.call();

            rft.join();

            fingerTable[i - 1] = temp;

        }
    }

    //look up for the file
    public void lookUp(int fileKey) {

        //System.out.println(counterForLookUp);

        //if(counterForLookUp){

            //counterForLookUp=false;

        //          NodeRequestThread rt = new NodeRequestThread((File) null, 4);
        //        rt.start();
        //      return;

         // }


        //      System.out.println(counterForLookUp);

        System.out.println("inside lookup"); //debug
        for (int i = fingerTable.length - 1; i >= 0; i--) { //looking in finger table for a node id <= filekey

            System.out.println("inside lookup for ,node: " + fingerTable[i].getId());

            if (fileKey >= fingerTable[i].getId()) {

                System.out.println("request thread from node: " + fingerTable[i].getId());
                this.counterForLookUp = true;
                NodeRequestThread rt = new NodeRequestThread(fingerTable[i], fileKey, 2);
                rt.start();
                return;

            }

        }

        System.out.println("no node found in the finger table");
        counterForLookUp = true;

        //if no such node found in the finger table look in the successor
        NodeRequestThread rt = new NodeRequestThread(fingerTable[0], fileKey, 2);
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

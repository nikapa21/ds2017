package Master;

import Chord.FileEntry;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MasterRequestThread extends Thread {

    int port;

    FileEntry fileEntry;
    int flag2;
    int fileKey;
    InetAddress clientIp;

    public MasterRequestThread(int port, int flag2) { //constructor 1

        this.port = port;
        this.flag2 = flag2;

    }

    public MasterRequestThread(int port, FileEntry fileEntry, int flag2) { //constructor 2

        this.port = port;
        this.fileEntry = fileEntry;
        this.flag2 = flag2;

    }

    public MasterRequestThread(int port, FileEntry fileEntry, int flag2, int fileKey, InetAddress clientIp) { //constructor 2

        this.port = port;
        this.fileEntry = fileEntry;
        this.flag2 = flag2;
        this.fileKey = fileKey;
        this.clientIp = clientIp;

    }

    public void run() {

        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        String chordNodeIP = "localhost";
        int chordNodePort = port;

        try {

            //System.out.println("master sends flag action " + flag2); //debug
            //System.out.println("master forwards client IP " + clientIp); //debug

            // open a socket from the master to the chord node that will handle the flag action
            requestSocket = new Socket(chordNodeIP, chordNodePort);

            // Get input and output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            out.writeInt(flag2); // the master sends the flag to the node
            out.flush();

            if (flag2 == 0) { // to inform nodes that a new node is on system


                out.writeBoolean(true);
                out.flush();

            } else if (flag2 == 1) { // commit(save) file

                out.writeObject(fileEntry); // send the file name
                out.flush();

                out.writeInt(fileKey); // send the key
                out.flush();

                out.writeObject(clientIp); // send(piggyback) the ip that requested the file
                out.flush();

            } else if (flag2 == 2) { // search file

                System.out.println("Assigning the search to node with port " + chordNodePort);

                out.writeInt(fileKey);
                out.flush();

                out.writeObject(fileEntry);
                out.flush();

                //Counter of hops
                out.writeInt(0);
                out.flush();

                out.writeObject(clientIp);
                out.flush();

            }else if (flag2 == 3) { // send the requested file back to user

                System.out.println("Sending the requested file from Master back to user " );
                out.writeObject(fileEntry);
                out.flush();

            } else if(flag2==4){
                out.writeBoolean(false);
                out.flush();
            }

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (Exception ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                /*in.close();
                out.close();*/
                requestSocket.close();
            } catch (Exception ioException) {
                ioException.printStackTrace();

            }
        }

    }

}
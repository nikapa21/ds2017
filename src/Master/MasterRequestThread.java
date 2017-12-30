package Master;

import Chord.Node;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MasterRequestThread extends Thread {

    int port;
    File file;
    int flag2;
    int fileKey;

    public MasterRequestThread(int port, int flag2) { //constructor 1

        this.port = port;
        this.flag2 = flag2;

    }

    public MasterRequestThread(int port, File file, int flag2, int fileKey) { //constructor 2

        this.port = port;
        this.file = file;
        this.flag2 = flag2;
        this.fileKey = fileKey;

    }

    public MasterRequestThread(int port, File file, int flag2) { //constructor 3

        this.port = port;
        this.file = file;
        this.flag2 = flag2;

    }

    public void run() {

        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {

            // Create a socket
            requestSocket = new Socket("localhost", port);
            System.out.println("master request port"+port);

            // Get input and output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            out.writeInt(flag2); // send flag
            out.flush();

            if (flag2 == 0) { // to inform nodes that a new node is on system

                out.writeBoolean(true);
                out.flush();

            } else if (flag2 == 1) { // commit(save) file

                out.writeObject(file);
                out.flush();

                out.writeInt(fileKey);
                out.flush();

            } else if (flag2 == 2) { // search file

                out.writeInt(fileKey);
                out.flush();

                //Counter of hops
                out.writeInt(0);
                out.flush();

            } else if (flag2 == 3) { // send the requested file back to user

                out.writeObject(file);
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
                in.close();
                out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

}
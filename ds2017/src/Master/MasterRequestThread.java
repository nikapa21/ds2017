package Master;

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
    File requestFile;
    int fileKey;

    public MasterRequestThread(int port, int flag2) {
        this.port = port;
        this.flag2 = flag2;
    }

    public MasterRequestThread(int port, File file, int flag2, int fileKey) {
        this.port = port;
        this.file = file;
        this.flag2 = flag2;
        this.fileKey = fileKey;
    }

    public MasterRequestThread(int port, File file, int flag2) {
        this.port = port;
        this.file = file;
        this.flag2 = flag2;
    }

    public File call() throws InterruptedException {
        Thread.sleep(2000);
        return requestFile;
    }

    public void run() {
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {

            //1) Create a socket to ip and to port 7777:
            requestSocket = new Socket("localhost", port);

            //2) Get input and output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            out.writeInt(flag2);
            out.flush();

            if (flag2 == 0) { //insert node
                out.writeBoolean(true);
                out.flush();
            } else if (flag2 == 1) { // commit file
                out.writeObject(file);
                out.flush();

                out.writeInt(fileKey);
                out.flush();
            } else if (flag2 == 2) { // search file
                //out.writeObject(file);
               // out.flush();

                out.writeInt(fileKey);
                out.flush();

                requestFile = (File)in.readObject();
            }else if (flag2 == 3) { // search file
                //out.writeObject(file);
                // out.flush();

                out.writeObject(file);
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
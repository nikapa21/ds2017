package Test;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MenuRequestThread extends Thread {

    File file;
    int port;
    int flag ;

    public MenuRequestThread(File file, int port, int flag) {

        this.file = file;
        this.port = port;
        this.flag = flag;

    }

    public void run() {

        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {

            // Create a socket to ip and to port 7777:
            requestSocket = new Socket("localhost", port);

            // Get input and output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            out.writeInt(flag);
            out.flush();

            if(flag==2){ // commit/save file

                out.writeObject(file);
                out.flush();

            }
            else if(flag==3) { // search file

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

package ds2017;
import java.io.*;
import java.net.*;

public class RequestThread extends Thread{

	int a, b;
	RequestThread(int a, int b) {
		this.a = a;
		this.b = b;
	}

	public void run() {
		Socket requestSocket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
			//1) Create a socket to ip and to port 4321: 

			requestSocket = new Socket("127.0.0.1", 8096);
			
			//2) Get input and output streams

			out = new ObjectOutputStream(requestSocket.getOutputStream());
			in = new ObjectInputStream(requestSocket.getInputStream());

			//3) Write the integers one by one and flush the stream

			out.writeInt(a);
			out.flush();

			out.writeInt(b);
			out.flush();

			System.out.println("Result from Server>" + in.readInt());
	
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				in.close();	out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}	
}
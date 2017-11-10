package ds2017;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ListeningThread extends Thread{

	ServerSocket providerSocket;
	Socket connection = null;
	int port;
	int id;
	
	public ListeningThread(int port, int id) {
		// TODO Auto-generated constructor stub
		this.port = port;
		this.id = id;
	}

	@Override
	public void run(){

		ObjectOutputStream out = null;
		ObjectInputStream in = null;

		try {
			//1) Create a socket to port 4321 and initialize its capacity to 10 connections
			providerSocket = new ServerSocket(port, 10);
			

			while (true) {
				//2) Accept connections
				connection = providerSocket.accept();

				//3) Get input and output streams
				out = new ObjectOutputStream(connection.getOutputStream());
				in = new ObjectInputStream(connection.getInputStream());

				//4) Read two integers from the stream

				int a = (int)in.readInt();

				int b = (int)in.readInt();

				System.out.println("Calculating "+a+" + "+b+" = "+(a+b) + " " + id);
				//5) Write the result back
				
				out.writeInt(a+b);

				//6) Always flush the stream!
				out.flush();
				
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				providerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
}

package ds2017;

import java.net.InetAddress;

public class Node {
	
	String addr;
	int port;
	static int counter = 0;
	int id;
	
	
	public Node(String addr, int port) {
		this.addr = addr;
		this.port = port;
		this.id=counter;
		counter++;
		ListeningThread lt = new ListeningThread(port, id);
		//lt.start();
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "Node [addr=" + addr + ", port=" + port + ", id=" + id + "]";
	}
	
}

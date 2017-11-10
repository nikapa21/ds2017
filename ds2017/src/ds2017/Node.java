package ds2017;

import java.net.InetAddress;

public class Node {
	
	String addr;
	int port;
	static int counter = 0;
	int id;
	int m = 2;
	int [] fingerTable;
	
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
	
	public int findSuccessor(int id, int i) {
		return (int) (id + Math.pow(2,i-1));
	}

	public void calculateFinger() {
		fingerTable = new int[m];
		
		for (int i=1;i<=m;i++) {
			if(findSuccessor(this.id, i) > Math.pow(2, m)-1) {
				fingerTable[i-1] = 0;
			}
			else {
				fingerTable[i-1] = findSuccessor(this.id, i);
			}
			
		}
	}
	
	public void printFinger() {
		for (int i=0;i<m;i++) {
			System.out.println(fingerTable[i]);
		}
	}
	
	@Override
	public String toString() {
		return "Node [addr=" + addr + ", port=" + port + ", id=" + id + "]";
	}
	
}

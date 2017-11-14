package chord;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
			
		System.out.println("Creating the distributed system, Chord");
			
		Node n1 = new Node("localhost", 8095,1);
		Node n2 = new Node("localhost", 8096,8);
		Node n3= new Node("localhost", 8097,14);
		Node n4 = new Node("localhost", 8098,21);
		Node n5 = new Node("localhost", 8099,32);
		Node n6 = new Node("localhost", 8100,38);
		Node n7 = new Node("localhost", 8101,47);
		Node n8 = new Node("localhost", 8102,48);
		Node n9 = new Node("localhost", 8103,51);
		Node n10 = new Node("localhost", 8104,56);
		
		ArrayList<Node> catalogueOfNodes = new ArrayList<Node>();
			
			
		catalogueOfNodes.add(n1);
		catalogueOfNodes.add(n2);
		catalogueOfNodes.add(n3);
		catalogueOfNodes.add(n4);
		catalogueOfNodes.add(n5);
		catalogueOfNodes.add(n6);
		catalogueOfNodes.add(n7);
		catalogueOfNodes.add(n8);
		catalogueOfNodes.add(n9);
		catalogueOfNodes.add(n10);
			
			
		n1.calculateFinger();
		n1.printFinger();
		System.out.println("First ");
		n5.calculateFinger();
		n5.printFinger();
		System.out.println("First ");
		n10.calculateFinger();
		n10.printFinger();
			
			
		Scanner sc = new Scanner(System.in);
		int i = 0;
			
		while(true){
				
			printMenu();

			while (!sc.hasNextInt()) {
				System.out.println("Not available! Please select one of the available actions below.");
				printMenu();
				sc.next();
			}
				
			i = sc.nextInt();
				
			switch (i){
				case 1: System.out.println("Commiting..."); // method to commit a file
							break;
				case 2: System.out.println("Searching..."); // method to search a file
							break;
				case 3: exitApplication();		// method to exit the application
							break;
				default: System.out.println("Not available! Please select one of the available actions below.");
							break;
			}
				
		}
			
			
			
			/*Node n1 = new Node("localhost", 8095);
			Node n2 = new Node("localhost", 8096);
			Node n3 = new Node("localhost", 8097);
			Node n4 = new Node("localhost", 8098);
			
			System.out.println(n1.toString());
			System.out.println(n2.toString());
			System.out.println(n3.toString());
			System.out.println(n4.toString());
			
			n1.calculateFinger();
			n2.calculateFinger();
			n3.calculateFinger();
			n4.calculateFinger();
			
			System.out.println("First ");
			n1.printFinger();
			System.out.println("Second ");
			n2.printFinger();
			System.out.println("Third ");
			n3.printFinger();
			System.out.println("Fourth ");
			n4.printFinger();*/

		}
		
		private static void exitApplication() {
			System.out.println("Thank you for visiting us!");
			System.exit(0);
			
		}

		private static void printMenu(){
			System.out.println();
			System.out.println("###############");
			System.out.println("Press <<1>> to commit a file...");
			System.out.println("Press <<2>> to search a file...");
			System.out.println("Press <<3>> to exit.");
			System.out.println("###############");
		}

}




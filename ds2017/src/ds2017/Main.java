package ds2017;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		/*Scanner sc = new Scanner(System.in);
		int i = 0;
		
		while(true) {
			
			System.out.println("###############");
			System.out.println("1. Commit file ");
			System.out.println("2. Search file ");
			System.out.println("3. Exit ");
			System.out.println("###############");
			
			System.out.println("\nPlease choose an option from the above menu(1-3): ");
			
			try {
				i = sc.nextInt();
				while (i!=1 && i!=2 && i!=3) {
					System.out.println("Please type the correct integer! ");
					i = sc.nextInt();
				}
				if (i==1) System.out.println("Commiting...");
				if (i==2) System.out.println("Searching..."); 
				if (i==3) {
					System.out.println("Bye Bye!!!");
					System.exit(0);
				}
			}
			catch(InputMismatchException e) {
				System.out.println("Please type the correct integer!\n");
				sc.next();
			}
			
			
		}*/
		Node n1 = new Node("localhost", 8095);
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
		n4.printFinger();

	}

}

package Test;

import java.util.Scanner;

public class Menu {

    static final int SERVER_PORT = 7777;

    public static void main(String[] args) {

        System.out.println("Creating the distributed system, Chord");

        Scanner sc = new Scanner(System.in);
        int i;


        while (true) {

            printMenu();

            while (!sc.hasNextInt()) {
                System.out.println("Not available! Please select one of the available actions below.");
                printMenu();
                sc.next();
            }

            i = sc.nextInt();

            switch (i) {
                case 1:
                    commit(sc); // method to commit a file
                    break;
                case 2:
                    System.out.println("Searching..."); // method to search a file
                    break;
                case 3:
                    exitApplication();        // method to exit the application
                    break;
                default:
                    System.out.println("Not available! Please select one of the available actions below.");
                    break;
            }

        }

    }

    private static void commit(Scanner sc) {
        System.out.println("Please commit the integer you want to save");
        while (!sc.hasNextInt()) {
            System.out.println("Not available! Please commit an integer.");
            sc.next();
        }
        int file = sc.nextInt();

        System.out.println("hisdfa " + file); //debug

        MenuRequestThread mrt = new MenuRequestThread(file, SERVER_PORT);
        mrt.start();


    }

    private static void exitApplication() {
        System.out.println("Thank you for visiting us!");
        System.exit(0);

    }

    private static void printMenu() {
        System.out.println();
        System.out.println("###############");
        System.out.println("Press <<1>> to commit a file...");
        System.out.println("Press <<2>> to search a file...");
        System.out.println("Press <<3>> to exit.");
        System.out.println("###############");
    }

}




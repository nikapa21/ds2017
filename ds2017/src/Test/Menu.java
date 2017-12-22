package Test;

import java.io.File;
import java.util.Scanner;

public class Menu {

    static final int SERVER_PORT = 7777;
    static int flag;

    public static void main(String[] args) throws InterruptedException {

        Scanner sc = new Scanner(System.in);
        int i;

        MenuListeningThread mlt = new MenuListeningThread();
        mlt.start();

        while (true) {

            printMenu();

            while (!sc.hasNextInt()) { //if the input is not correct

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
                    search(sc); // method to search a file
                    break;
                case 3:
                    exitApplication(); // method to exit the application
                    break;
                default:
                    System.out.println("Not available! Please select one of the available actions below.");
                    break;
            }

        }

    }

    private static void commit(Scanner sc) {

        System.out.println("Please commit the file, by the name, you want to save to the system");

        String fileName = sc.next();

        File file = new File(fileName);

        while (!file.exists()) { // if the file doen't exist in directory

            System.out.println("The file doesn't exist! Please select a valid file name.");
            fileName = sc.next();
            file = new File(fileName);

        }

        flag = 2;

        System.out.println(file.toPath().toString()); //debug

        MenuRequestThread mrt = new MenuRequestThread(file, SERVER_PORT, flag);
        mrt.start();

    }

    private static void search(Scanner sc) throws InterruptedException {

        System.out.println("Please select the file you want to search and take");

        String fileName = sc.next();
        File file = new File(fileName);

        flag = 3;

        MenuRequestThread mrt = new MenuRequestThread(file, SERVER_PORT, flag);
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
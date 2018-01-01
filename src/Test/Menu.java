package Test;

import java.io.*;
import java.util.Scanner;

public class Menu {

    static final int SERVER_PORT = 7777;
    static int flag;

    public static void main(String[] args)  {

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

        System.out.print("Please commit the file, by the name, you want to save to the system ");

        String fileName = sc.next();

        File file = new File(fileName); // read the filename
        BufferedReader reader = null; // to read the data of the file
        String line; //for every line of the file
        String fileData = "";//at first string is empty. TODO maybe this initialization is a workaround

        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException fnfex) {

            while (!file.exists()) { // if the file doesn't exist in directory

                System.out.println("The file doesn't exist! Please select a valid file name.");
                fileName = sc.next();
                file = new File(fileName);

            }
            try {
                reader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        try{// fill the fileData string with all contents of the file we read
            while ((line = reader.readLine()) != null){
                fileData += "\n" + line;
            }

        } catch (IOException ioex){
            System.out.println(ioex.getMessage() + "Error reading file ");
        }

        System.out.println(fileData.toString()); //debug

        flag = 2;

        MenuRequestThread mrt = new MenuRequestThread(file, fileData, SERVER_PORT, flag);//Here we pass the filename, the fileData, the server port and the flag action commit to the thread
        mrt.start();

    }

    private static void search(Scanner sc) {

        System.out.println("Please select the file you want to search and take");

        String fileName = sc.next();
        File file = new File(fileName);

        flag = 3;

        MenuRequestThread mrt = new MenuRequestThread(file, SERVER_PORT, flag);
        mrt.start();

    }

    private static void exitApplication() {

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
               System.out.println("Exiting........");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));

        System.out.println("Thank you for visiting us!");
        System.exit(0);

    }

    public static void printMenu() {

        System.out.println();
        System.out.println("###############");
        System.out.println("Press <<1>> to commit a file...");
        System.out.println("Press <<2>> to search a file...");
        System.out.println("Press <<3>> to exit.");
        System.out.println("###############");

    }

}
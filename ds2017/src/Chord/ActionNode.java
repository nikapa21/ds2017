package Chord;

import java.io.*;
import java.util.*;

public class ActionNode extends Thread {

    ObjectOutputStream out;
    ObjectInputStream in;
    ArrayList<Integer> files;

    public ActionNode(ObjectOutputStream out, ObjectInputStream in, ArrayList<Integer> files) {

        this.out = out;
        this.in = in;
        this.files = files;

    }

    public void run() {
        try {

            int file = in.readInt();
            files.add(file);
            printFiles();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void printFiles() {

        for (int i = 0; i < files.size(); i++) {
            System.out.println(files.get(i) + "sd");
        }

    }

}


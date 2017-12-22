package Chord;

import java.io.*;
import java.util.*;

public class NodeActionForClients extends Thread {

    ObjectOutputStream out;
    ObjectInputStream in;
    ArrayList<File> files;
    int flag;
    Node n;
    ArrayList<Integer> fileKeys;

    public NodeActionForClients(ObjectOutputStream out, ObjectInputStream in, ArrayList<File> files, int flag, Node n, ArrayList<Integer> fileKeys) {

        this.out = out;
        this.in = in;
        this.files = files;
        this.flag = flag;
        this.n = n;
        this.fileKeys = fileKeys;

    }

    public NodeActionForClients(ObjectOutputStream out, ObjectInputStream in, int flag, Node n) {

        this.out = out;
        this.in = in;
        this.flag = flag;
        this.n = n;

    }

    public void run() {

        try {

            if (flag == 1) { //commit(save) file

                File file = (File) in.readObject();
                files.add(file);
                printFiles();

                int keyFile = in.readInt();
                fileKeys.add(keyFile);
                printFilesKeys();

            } else if (flag == 2) { //search file

                int keyFile = in.readInt();

                for (int i = 0; i < fileKeys.size(); i++) { //search if file is in this node

                    PrintDebug();
                    if(fileKeys.get(i)==keyFile) {

                        PrintDebug2();
                        Node.counterForLookUp=false;

                        NodeRequestThread rt = new NodeRequestThread(files.get(i),4);
                        rt.start();

                        return;

                    }

                }

                n.lookUp(keyFile); //if file isn't in this node

            }
            else if (flag == 6) { //search for successor

                Node successor = n.FindSuccessor2(in.readInt());
                out.writeObject(successor);
                out.flush();
            }
            else if (flag == 7) { //search for closest precending node

                Node successor = n.ClosestPrecendingNode(in.readInt());
                out.writeObject(successor);
                out.flush();
            }
            else if (flag == 8) { //notify


                Node n0 = (Node)in.readObject();
                //System.out.println("Notify message from node "+n0.toString());
                n.Notify(n0);
            }
            else if (flag == 9) { //return self
                //Node n0 = (Node)in.readObject();
                out.writeObject(n);
                out.flush();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    void printFiles() {

        for (int i = 0; i < files.size(); i++) {

            System.out.println(files.get(i).getName());

        }

    }

    void printFilesKeys() {

        for (int i = 0; i < fileKeys.size(); i++) {

            System.out.println("the size is " + fileKeys.size());
            System.out.println(fileKeys.get(i));

        }

    }

    void  PrintDebug()
    {
        System.out.println("searching files ");
    }

    void  PrintDebug2() {
        System.out.println("found file ");
    }

}


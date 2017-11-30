package Chord;

import java.io.*;
import java.util.*;

public class ActionNode extends Thread {

    ObjectOutputStream out;
    ObjectInputStream in;
    ArrayList<File> files;
    int flag;
    Node n;
    ArrayList<Integer> fileKeys;

    public ActionNode(ObjectOutputStream out, ObjectInputStream in, ArrayList<File> files, int flag, Node n, ArrayList<Integer> fileKeys) {

        this.out = out;
        this.in = in;
        this.files = files;
        this.flag = flag;
        this.n = n;
        this.fileKeys = fileKeys;

    }

    public void run() {
        try {

            if (flag == 1) {
                File file = (File) in.readObject();
                files.add(file);
                printFiles();

                int keyFile = in.readInt();
                fileKeys.add(keyFile);
                printFilesKeys();
            } else if (flag == 2) {
                File file = (File)in.readObject();

                int keyFile = in.readInt();

                for (int i = 0; i < fileKeys.size(); i++) {

                    if(fileKeys.get(i)==keyFile){
                        out.writeObject(files.get(i));
                        out.flush();
                    }

                }

                //int nextNodeToLook = n.lookUp(file);

                //RequestThread rt = new RequestThread();

                 //for(int i=files.size()-1;i>=0;i--){
                   //if(files.get(i)==file){
                     //out.writeInt(files.get(i));
                   //out.flush();

               // }
                //else if(files.get(i)>file){

//                }
  //              }
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

}


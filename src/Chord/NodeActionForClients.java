package Chord;

import java.io.*;
import java.net.InetAddress;
import java.util.*;

public class NodeActionForClients extends Thread {

    ObjectOutputStream out;
    ObjectInputStream in;
    ArrayList<FileEntry> fileEntries;
    int flag;
    Node n;
    ArrayList<Integer> fileKeys;
    ArrayList<FileEntry> memory;
    ArrayList<Integer> memoryKeys;

    public NodeActionForClients(ObjectOutputStream out, ObjectInputStream in, ArrayList<FileEntry> files, int flag, Node n, ArrayList<Integer> fileKeys,ArrayList<Integer> memoryKeys,ArrayList<FileEntry> memory) {

        this.out = out;
        this.in = in;
        this.fileEntries = files;
        this.flag = flag;
        this.n = n;
        this.fileKeys = fileKeys;
        this.memoryKeys = memoryKeys;
        this.memory = memory;
    }

    public void run() {

        try {

            if (flag == 1) { //commit(save) file

                File file = (File) in.readObject();
                String fileData = (String) in.readObject();

                fileEntries.add(new FileEntry(file, fileData));
                printFiles();

                System.out.println("The Node received a commit action and commited the following data \n" + fileData); //debug

                int keyFile = in.readInt();
                fileKeys.add(keyFile);
                printFilesKeys();

                InetAddress clientIp = (InetAddress) in.readObject();
                System.out.println("The Node receive a flag action " + flag + " initially ordered by the client IP " + clientIp); //debug

            } else if (flag == 2) { //search file

                int keyFile = in.readInt();

                int counterForExistenceOfFile = in.readInt();

                InetAddress clientIp = (InetAddress) in.readObject();
                System.out.println("The Node receive a flag action " + flag + " initially ordered by the client IP " + clientIp); //debug

                if (counterForExistenceOfFile>10){

                    System.out.println("Could not find the file ");
                    NodeRequestThread rt = new NodeRequestThread((FileEntry) null, 4, clientIp);
                    rt.start();

                    return;
                }

                //first search in memory to find the file
                for(int i =0;i<memory.size();i++)
                {
                    if(memoryKeys.get(i)==keyFile)
                    {
                        PrintDebug3();
                        //return the file found, directly back to the menu with client IP
                        System.out.println("found keyfile in memory "+ keyFile);
                        NodeRequestThread rt = new NodeRequestThread(memory.get(i),4, clientIp);
                        rt.start();

                        return;//file found

                    }
                }

                //else search in disk
                for (int i = 0; i < fileKeys.size(); i++) { //search if file is in this node

                    PrintDebug();
                    if(fileKeys.get(i)==keyFile) {

                        PrintDebug2();
                        System.out.println("found keyfile in disk "+ keyFile);
                        if(memoryKeys.contains(keyFile))//last requested object added in the last position of the array
                        {
                            memoryKeys.remove(keyFile);
                            memoryKeys.add(keyFile);
                            memory.remove(fileKeys.get(i));
                            memory.add(fileEntries.get(i));
                        }

                        if(memoryKeys.size() > 2)//delete oldest element from memory
                        {

                            memoryKeys.remove(0);
                            memory.remove(0);
                            PrintDebug5();
                        }
                        PrintDebug4();
                        memoryKeys.add(keyFile);
                        memory.add(fileEntries.get(i));

                        NodeRequestThread rt = new NodeRequestThread(fileEntries.get(i),4, clientIp);
                        rt.start();

                        return;

                    }

                }

                n.lookUp(keyFile, counterForExistenceOfFile, clientIp); //if file isn't in this node lookup somewhere else

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    void printFiles() {

        for (int i = 0; i < fileEntries.size(); i++) {

            System.out.println(fileEntries.get(i).getFile().getName());

        }

    }

    void printFilesKeys() {

        for (int i = 0; i < fileKeys.size(); i++) {

            System.out.println("the size is " + fileKeys.size());
            System.out.println("Filekeys " + fileKeys.get(i));

        }

    }

    void  PrintDebug()
    {
        System.out.println("searching files ");
    }

    void  PrintDebug2() {
        System.out.println("found file ");
    }

    void  PrintDebug3() {
        System.out.println("found file in memory");
    }

    void  PrintDebug4() {
        System.out.println("add file in memory");
    }

    void  PrintDebug5() {
        System.out.println("remove file from memory");
    }
}

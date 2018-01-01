package Master;

import Chord.Node;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.*;

public class MasterActionForClients extends Thread {

    ObjectOutputStream out;
    ObjectInputStream in;
    ArrayList<Node> catalogueOfNodes;
    int flag2;

    public MasterActionForClients(ArrayList<Node> catalogueOfNodes, ObjectOutputStream out, ObjectInputStream in, int flag2) {

        this.out = out;
        this.in = in;
        this.catalogueOfNodes = catalogueOfNodes;
        this.flag2 = flag2;

    }

    public void run() {

        try {
            //System.out.println("Number of active threads from the given thread: " + Thread.activeCount()); //debug

            if (flag2 == 0) { // finger table

                int id = (int) in.readInt(); //read the id of requested node
                //System.out.println("Got a request to return a node close to the id " + id);

                for (int i = 0; i < catalogueOfNodes.size(); i++) { // find the requested node and send it back

                    if (catalogueOfNodes.get(i).getId() == id) {
                        //System.out.println("Found a node with the same id");
                        out.writeObject(catalogueOfNodes.get(i));
                        out.flush();
                        return;

                    } else if (catalogueOfNodes.get(i).getId() > id) {
                        // to lathos edw itan oti den espage ti loop otan evriske auto pou ithele, kai egrafe kai alla sto socket to opoio opmws eixe entoli apo ton client na kleisei
                        // uparxei mia in.close().
                        //System.out.println("Found a node "+node.toString()+" with index i "+i+" with bigger id than " + id);
                        out.writeObject(catalogueOfNodes.get(i));
                        out.flush();
                        return;

                    } else if (catalogueOfNodes.get(catalogueOfNodes.size() - 1).getId() < id) {

                        //System.out.println("Returning the first node which is " + catalogueOfNodes.get(0));
                        out.writeObject(catalogueOfNodes.get(0));
                        out.flush();
                        return;

                    }

                }

            } else if (flag2 == 1) { //commit(save) file to the chord system

                File file = (File) in.readObject(); //read the file

                String fileData = (String) in.readObject(); //read the file data
                System.out.println("The commited file contains following data" + fileData +"\n"); //debug

                InetAddress clientIp = (InetAddress) in.readObject();//read the requesting IP
                System.out.println("Received a commit request from client IP " + clientIp);


                String sha1Hash = HashGenerator.generateSHA1(file.getName()); //hash the name of file
                int fileKey = new BigInteger(sha1Hash, 16).intValue(); // convert hex to int
                fileKey = Math.abs(fileKey % 64);

                for (int i = 0; i < catalogueOfNodes.size(); i++) { //send the file in the correct(by id) node

                    if (fileKey <= catalogueOfNodes.get(i).getId()) {

                        MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get(i).getPort(), file, fileData, flag2, fileKey, clientIp);
                        mrt.start();
                        break;

                    } else if (fileKey > catalogueOfNodes.get(catalogueOfNodes.size() - 1).getId()) {

                        MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get(0).getPort(), file, fileData, flag2, fileKey, clientIp);
                        mrt.start();
                        break;

                    }

                }

            } else if(flag2==2){ //search file

                System.out.println("Received order from search action. Waiting for filename...");
                File file = (File) in.readObject();//read the file

                InetAddress clientIp = (InetAddress) in.readObject();//read the requesting IP
                System.out.println("Received a commit request from client IP " + clientIp);

                String sha1Hash = HashGenerator.generateSHA1(file.getName());// hash the name of file with sha1
                int fileKey = new BigInteger(sha1Hash, 16).intValue(); //convert the hex to big int
                fileKey = Math.abs(fileKey % 64);

                MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get((int) (Math.random()*((catalogueOfNodes.size()-1 - 0) + 1) + 0)).getPort(), null, 2,fileKey, clientIp);
                mrt.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (HashGenerationException e) {
            e.printStackTrace();
        }
    }

}

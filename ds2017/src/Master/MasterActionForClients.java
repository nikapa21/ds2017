package Master;

import Chord.Node;

import java.io.*;
import java.math.BigInteger;
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

            if (flag2 == 0) { // finger table

                int id = (int) in.readInt(); //read the id of requested node

                for (int i = 0; i < catalogueOfNodes.size(); i++) { // find the requested node and send it back

                    if (catalogueOfNodes.get(i).getId() == id) {

                        out.writeObject(catalogueOfNodes.get(i));
                        out.flush();

                    } else if (catalogueOfNodes.get(i).getId() > id) {

                        out.writeObject(catalogueOfNodes.get(i));
                        out.flush();

                    } else if (catalogueOfNodes.get(catalogueOfNodes.size() - 1).getId() < id) {

                        out.writeObject(catalogueOfNodes.get(0));
                        out.flush();

                    }

                }

            } else if (flag2 == 1) { //commit(save) file to the chord system

                File file = (File) in.readObject(); //read the file

                String sha1Hash = HashGenerator.generateSHA1(file.getName()); //hash the name of file
                int fileKey = new BigInteger(sha1Hash, 16).intValue(); // convert hex to int
                fileKey = Math.abs(fileKey % 64);

                for (int i = 0; i < catalogueOfNodes.size(); i++) { //send the file in the correct(by id) node

                    if (fileKey <= catalogueOfNodes.get(i).getId()) {

                        MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get(i).getPort(), file, flag2, fileKey);
                        mrt.start();
                        break;

                    } else if (fileKey > catalogueOfNodes.get(catalogueOfNodes.size() - 1).getId()) {

                        MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get(0).getPort(), file, flag2, fileKey);
                        mrt.start();
                        break;

                    }

                }

            } else if(flag2==2){ //search file

                File file = (File) in.readObject();

                String sha1Hash = HashGenerator.generateSHA1(file.getName());// hash the name of file with sha1
                int fileKey = new BigInteger(sha1Hash, 16).intValue(); //convert the hex to big int
                fileKey = Math.abs(fileKey % 64);

                for(int i=0;i<catalogueOfNodes.size()-1;i++){

                    catalogueOfNodes.get(i).counterForLookUp=false;
                    System.out.print(catalogueOfNodes.get(i).counterForLookUp);

                }

                MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get((int) (Math.random()*((catalogueOfNodes.size()-1 - 0) + 1) + 0)).getPort(), null, 2,fileKey);
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

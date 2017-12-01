package Master;

import Chord.Node;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Action extends Thread {

    ObjectOutputStream out;
    ObjectInputStream in;
    ArrayList<Node> catalogueOfNodes;
    int flag2;

    public Action(ArrayList<Node> catalogueOfNodes, ObjectOutputStream out, ObjectInputStream in, int flag2) {

        this.out = out;
        this.in = in;
        this.catalogueOfNodes = catalogueOfNodes;
        this.flag2 = flag2;

    }

    public void run() {
        try {

            if (flag2 == 0) { // finger table
                int a = (int) in.readInt();
                //System.out.println("skaei edw server");

                for (int i = 0; i < catalogueOfNodes.size(); i++) {
                    if (catalogueOfNodes.get(i).getId() == a) {
                        out.writeObject(catalogueOfNodes.get(i));
                        out.flush();
                    } else if (catalogueOfNodes.get(i).getId() > a) {
                        out.writeObject(catalogueOfNodes.get(i));
                        out.flush();
                    } else if (catalogueOfNodes.get(catalogueOfNodes.size() - 1).getId() < a) {
                        out.writeObject(catalogueOfNodes.get(0));
                        out.flush();
                    }

                }

            } else if (flag2 == 1) { // commit file

                File file = (File) in.readObject();
                String sha1Hash = HashGeneratorUtils.generateSHA1(file.getName());
                int fileKey = new BigInteger(sha1Hash, 16).intValue();
                fileKey = Math.abs(fileKey % 64);

                for (int i = 0; i < catalogueOfNodes.size(); i++) {

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

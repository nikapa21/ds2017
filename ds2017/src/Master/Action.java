package Master;

import Chord.Node;

import java.io.*;
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

            if (flag2 == 0) {
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

            } else if (flag2 == 1) {

                int file = in.readInt();

                for (int i = 0; i < catalogueOfNodes.size(); i++) {

                    if (file <= catalogueOfNodes.get(i).getId()) {
                        MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get(i).getPort(), file, flag2);
                        mrt.start();
                        break;
                    } else if (file > catalogueOfNodes.get(catalogueOfNodes.size() - 1).getId()) {
                        MasterRequestThread mrt = new MasterRequestThread(catalogueOfNodes.get(0).getPort(), file, flag2);
                        mrt.start();
                        break;
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

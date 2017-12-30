/*
package Chord;

public class Stabilizer extends Thread {
    Node n;

    public Stabilizer(Node node)
    {
        n = node;
    }


    @Override
    public void run()
    {
        while(true) {

            try {
                n.Stabilize();
                n.FixFingers();

                this.sleep(5000);
                //join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
*/

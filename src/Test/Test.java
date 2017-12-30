package Test;


import Master.HashGenerationException;
import Master.HashGeneratorUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String args[]) throws HashGenerationException, IOException {

        String test = "jac.txt";
        String test1 = "second.txt";
        System.out.println("SHA-1 Hash: " + test);
        System.out.println("SHA-1 Hash: " + test1);
        //AeSimpleSHA1.SHA1(test);

       // int hashtest = test.hashCode();
        String sha1Hash = HashGeneratorUtils.generateSHA1(test);
        String sha1Hash1 = HashGeneratorUtils.generateSHA1(test1);

        int i = (int) (Math.random()*((10 - 5) + 1) + 5);
        System.out.println(i);
        int t = new BigInteger(sha1Hash, 16).intValue();
        int t1 = new BigInteger(sha1Hash1, 16).intValue();

        System.out.println("SHA-1 Hash: " + sha1Hash);
        System.out.println("SHA-1 Hash: " + t);
        System.out.println("SHA-1 Hash: " + t % 64);
        System.out.println("SHA-1 Hash: " + sha1Hash1);
        System.out.println("SHA-1 Hash: " + t1);
        System.out.println("SHA-1 Hash: " + t1 % 64);
        //System.out.println("hash code: " + (hashtest));
       // System.out.println("hash code: " + (hashtest % 64));

       /* File f = new File("jac.txt");
        if (f.exists()) {
            System.out.println("yo eimai edw");
        }

       /* PrintWriter writer = new PrintWriter("jac13.txt", "UTF-8");
        writer.println("The first line");
        writer.println("The second line");
        writer.close();*/

       // Files.write(Paths.get(f.getName()), "the fdsskngfdkgn kdhrn kdnhffk ndfkhn kdnf h".getBytes(), StandardOpenOption.APPEND);
        //System.out.println(f.toPath().toAbsolutePath().toString());

    }


}

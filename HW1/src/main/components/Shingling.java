import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.HashMap;

/**
 * Created by gaoxiaoxu on 10/11/16.
 */
public class Shingling {

    public void Shingling(String InputDocument, Integer k_shingles) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(InputDocument));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        Set<String> set = new HashSet<String>();
        set.add("ddd");
        set.add("d");
        set.add("ddd");


//        String a = "Hello";
//        String b = "world";
//        String c = "Hello";
//        CRC32 crc = new CRC32();
//        CRC32 crc2 = new CRC32();
//        CRC32 crc3 = new CRC32();
//        crc.update(a.getBytes());
//        crc2.update(b.getBytes());
//        crc3.update(c.getBytes());
    }
}

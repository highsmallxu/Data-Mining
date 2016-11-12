import org.apache.commons.lang3.StringUtils;

import java.util.*;


/**
 * Created by gaoxiaoxu on 10/11/16.
 */
public class Shingling {

    public void Shingling(String InputDocument, Integer k_shingles){
        List<String> Tokens = new ArrayList();
        List sets = new ArrayList();
        StringTokenizer st = new StringTokenizer(InputDocument);
        while (st.hasMoreTokens()){
            Tokens.add(st.nextToken());
        }

        for(int i=0;i<Tokens.size();i++){
            List<String> set = new ArrayList();
            String currentToken = Tokens.get(i);
            set.add(currentToken);
            for(int j=1;j<k_shingles;j++){
                if(i+j>=Tokens.size()){
                    break;
                }
                else{
                    set.add(Tokens.get(i+j));
                }
            }
            if(set.size()==k_shingles){
                String SetString = StringUtils.join(set," ");
                sets.add(SetString);
            }
            else{
                break;
            }

        }


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

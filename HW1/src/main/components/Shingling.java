import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by gaoxiaoxu on 10/11/16.
 */
public class Shingling {

    public List Shingling(String InputDocument, Integer k_shingles) throws UnsupportedEncodingException{
        Set<Integer> sets = new HashSet<Integer>();
        Set<String> tmp = new HashSet<String>();
        for(int i=0;i<InputDocument.length()-k_shingles+1;i++){
            String single = InputDocument.substring(i,i+k_shingles);
            Integer singleHashcode = single.hashCode();
            sets.add(singleHashcode);
            tmp.add(single);
        }
        List<Integer> setsList = new ArrayList<Integer>(sets);
        List<String> tmps = new ArrayList<String>(tmp);
        return setsList;

    }
}

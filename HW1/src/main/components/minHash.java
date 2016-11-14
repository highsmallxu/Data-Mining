import java.util.*;

/**
 * Created by gaoxiaoxu on 14/11/16.
 */
public class minHash {

    public List<Integer> k_shingle(String document, Integer k){
        List<Integer> k_shingles;
        Set<Integer> sets = new HashSet<Integer>();
        for(int i=0;i<document.length()-k+1;i++){
            String shingle = document.substring(i,i+k);
            Integer shingleHashcode = shingle.hashCode();
            sets.add(shingleHashcode);
        }
        k_shingles = new ArrayList<Integer>(sets);
        return k_shingles;
    }

    public Map<Integer,boolean[]> HashSingleMatrix(List<Integer> documentshingles){
        Map<Integer,boolean[]> HashMatrix = new HashMap<Integer, boolean[]>();
        for(int i=0;i<documentshingles.size();i++){
            HashMatrix.put(documentshingles.get(i), new boolean[]{true,false});
        }
        return HashMatrix;
    }
}


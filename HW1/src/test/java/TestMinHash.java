import java.io.IOException;
import java.util.*;

/**
 * Created by gaoxiaoxu on 15/11/16.
 */
public class TestMinHash {


    public static void main(String arg[]) throws IOException{

        /*
        Get dataset path
        Get full documents path
         */
        Preprocessing preprocessingClass = new Preprocessing();
        List<String> alldocPath = preprocessingClass.ReadingFilesMain("test");

        /*
        Choose initial document;
        Remove unnecessary space;
         */
        Integer inidocInd  = new Random().nextInt(alldocPath.size());
        String  inidocPath = alldocPath.get(inidocInd);
        String  inidoc     = preprocessingClass.RemoveSpaceAndJoint(inidocPath);

        System.out.print("===Selected document is:" + inidocPath +"==="+"\n");

        /*
        Get initial document shingle sets
         */
        Integer k=10;
        List<Integer> inidocShi = k_shingle(inidoc,k);
        Set<Integer>  inidocShiSet = new HashSet<Integer>(inidocShi);

        for(int i=0;i<alldocPath.size();i++){
            String comdocPath = alldocPath.get(i);
            String comdoc = preprocessingClass.RemoveSpaceAndJoint(comdocPath);
            List<Integer> comdocShi = k_shingle(comdoc,k);
            Set<Integer> comdocShiSet = new HashSet<Integer>(comdocShi);

            Set<Integer> combineSet = new HashSet<Integer>();
            combineSet.addAll(inidocShi);
            combineSet.addAll(comdocShi);

            TestMinHash minhashClass = new TestMinHash(100,combineSet.size());
            int[] sig1 = minhashClass.hash(inidocShiSet,combineSet);
            int[] sig2 = minhashClass.hash(comdocShiSet,combineSet);

            System.out.print("===Compated document is:"+comdocPath+"==="+"\n");
            System.out.println("Similarity with minhash is:"+minhashClass.sim(sig1,sig2));
            System.out.println("Similarity without minhash is:"+JaccardSim(inidocShiSet,comdocShiSet)+"\n");
        }
    }

    public static List<Integer> k_shingle(String document, Integer k){
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

    public static double JaccardSim(Set<Integer>s1, Set<Integer>s2){
        Set<Integer> intersection = new HashSet<Integer>(s1);
        intersection.retainAll(s2);
        Set<Integer> union = new HashSet<Integer>(s1);
        union.addAll(s2);
        return (double) intersection.size()/union.size();
    }

    private int n;
    private int[][]hashfun;
    private int combinesize;

    public TestMinHash (int size, int combinesize){
        init(size,combinesize);
    }

    public void init(int size, int combinesize){
        this.combinesize = combinesize;
        n = size;
        Random r = new Random();
        hashfun = new int[n][2];
        for(int i=0;i<n;i++){
            hashfun[i][0] = r.nextInt(Integer.MAX_VALUE);
            hashfun[i][1] = r.nextInt(Integer.MAX_VALUE);
        }
    }

    private int h(int i, int x){
        Integer prime = combinesize;
        return (int) ((((long)hashfun[i][0]) * x +
                ((long)hashfun[i][1])) % (Integer.MAX_VALUE));
    }

    public int[] hash(Set<Integer> set, Set<Integer> combine){
        int[] sig = new int[n];
        for(int i=0;i<n;i++){
            sig[i] = Integer.MAX_VALUE;
        }
        List<Integer> a = new ArrayList<Integer>(combine);
        for(int r=0;r<combinesize;r++){
            if(set.contains(a.get(r))){
                for(int i=0;i<n;i++){
                    sig[i] = Math.min(sig[i],h(i,r));
                }
            }
        }
        return sig;
    }

    public double sim(int[] sig1, int[] sig2){
        if(sig1.length!=sig2.length){
            throw new IllegalArgumentException();
        }
        double sim = 0;
        for(int i=0;i<sig1.length;i++){
            if(sig1[i]==sig2[i]){
                sim += 1;
            }
        }
        return sim/sig1.length;
    }

}

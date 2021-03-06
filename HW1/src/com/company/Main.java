package com.company;

import java.io.IOException;
import java.util.*;

/**
 * Created by gaoxiaoxu on 15/11/16.
 */
public class Main {


    public static void main(String arg[]) throws IOException{

        Scanner user_input = new Scanner(System.in);
        System.out.print("Enter threshold for Jaccard Similarity:"+"\n");
        System.out.print("-The range is from 0 to 1"+"\n");
        System.out.print("Number: ");
        String input = user_input.next();
        Double jacth = Double.parseDouble(input);

        System.out.print("Enter threshold for LSH:"+"\n");
        System.out.print("-The range is from 0 to 1"+"\n");
        System.out.print("Number: ");
        String input2 = user_input.next();
        Double lshth = Double.parseDouble(input2);

        /*
        Get dataset path
        Get full documents path
         */
        Preprocessing preprocessingClass = new Preprocessing();
        List<String> alldocPath = preprocessingClass.ReadingFilesMain("com/company/data");

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

            Main minhashClass = new Main(100,combineSet.size());
            int[] sig1 = minhashClass.hash(inidocShiSet,combineSet);
            int[] sig2 = minhashClass.hash(comdocShiSet,combineSet);
            String ans = minhashClass.lsh(sig1,sig2,lshth);

            System.out.print("===Compated document is:"+comdocPath+"==="+"\n");

            //Output
            if(JaccardSim(inidocShiSet,comdocShiSet)>jacth){
                System.out.print("Similar document found!"+"\n");
                System.out.println("Jaccard Similarity is:"+JaccardSim(inidocShiSet,comdocShiSet));
                System.out.println("MinHash Similarity is:"+minhashClass.sim(sig1,sig2));
            }
            else{
                System.out.print("Not Similar"+"\n");
                System.out.println("Jaccard Similarity is:"+JaccardSim(inidocShiSet,comdocShiSet));
                System.out.println("MinHash Similarity is:"+minhashClass.sim(sig1,sig2));
            }

            System.out.println(ans+"\n");
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

    public Main(int size, int combinesize){
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

    public int[] complshhash(int[] sig, int stages, double buckets){
        int[] hash = new int[stages];
        int rows = sig.length/stages;

        for(int i=0;i<sig.length;i++){
            int stage = Math.min(i/rows,stages-1);
            hash[stage] = (int)((hash[stage] + (long)sig[i]*Integer.MAX_VALUE)%buckets);
        }

        return hash;

    }
    public String lsh(int[] sig1,int[] sig2,Double thresh){
        String ans = "not match";
        int stages = 20; //stage=band
        double buckets = 10;


        int[] hash1 = complshhash(sig1,stages,buckets);
        int[] hash2 = complshhash(sig2,stages,buckets);

        double count = 0;
        for(int i=0;i<stages;i++){
            if(hash1[i]==hash2[i]){
                count += 1;
            }
        }
        double fraction = count/stages;

        if(fraction > thresh){
            ans = "candidate pair";
        }
        else{
            ans = "not candidate pair";
        }
        return ans;
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

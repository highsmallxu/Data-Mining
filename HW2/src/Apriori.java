import java.io.*;
import java.lang.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;


public class Apriori {

    public static void main(String[] args) throws Exception {

        int min_sup = 2;
        double min_conf = 0.7;
        String data = "sdata.dat";
        if (args.length > 0) {
            data = args[0];
            min_sup = Integer.parseInt(args[1]);
            min_conf = Double.parseDouble(args[2]);
        }


        String output = "result.txt";

        // read data file and init transactions
        ReadData readDat = new ReadData();
        List<Set<String>> trans = readDat.read(data);

        //find frequent item sets
        Map<Integer, Set<ItemSet>> rst = findFrequentItemSets(trans, min_sup,output);

        // generate association rule
        Rule genRule = new Rule();
        Rule.genRule(rst, min_conf,output);

    }

    //find frequent item set
    static Map<Integer, Set<ItemSet>> findFrequentItemSets(Iterable<Set<String>> transIterable, int min_sup, String output) {
        Map<Integer, Set<ItemSet>> ret = new TreeMap<Integer, Set<ItemSet>>();

        //find 1 item frequent set
        Iterator<Set<String>> it = transIterable.iterator();
        Set<ItemSet> oneItemSets = findFrequentOneItemSets(it, min_sup);
        ret.put(1, oneItemSets);

        int preItemSetSize = 1;
        Set<ItemSet> preItemSets = oneItemSets;

        //find L frequent item set until L-1 frequent item set does not exist
        while (!preItemSets.isEmpty()) {
            int curItemSetSize = preItemSetSize + 1;

            //list all candidate L frequent item set
            List<ItemSet> candidates = aprioriGenCandidates(preItemSets);

            //list all frequency of L frequent item set
            it = transIterable.iterator();
            while (it.hasNext()) {
                Set<String> tran = it.next();
                for (ItemSet candidate : candidates)
                    if (tran.containsAll(candidate))
                        candidate.frequence++;
            }
            //candidate item set which has support larger than min_sup could be frequent item set
            Set<ItemSet> curItemSets = new HashSet<ItemSet>();
            for (ItemSet candidate : candidates)
                if (candidate.frequence >= min_sup)
                    curItemSets.add(candidate);
            if (!curItemSets.isEmpty())
                ret.put(curItemSetSize, curItemSets);

            preItemSetSize = curItemSetSize;
            preItemSets = curItemSets;
        }
        //print result
        try {
            PrintWriter writer =  new PrintWriter(output);
            writer.println("Frequent Item Sets:");//System.out.println("Frequent Item Sets:");
            for (Entry<Integer, Set<ItemSet>> entry : ret.entrySet()) {
                Integer itemSetSize = entry.getKey();
                writer.printf("Frequent %d Item Sets:\n", itemSetSize);//System.out.printf("Frequent %d Item Sets:\n", itemSetSize);

                for (ItemSet set : entry.getValue())
                    writer.printf("%s, %d\n", set, set.frequence);//System.out.printf("%s, %d\n", set, set.frequence);
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return ret;
    }

    //find frequent one item set
    static Set<ItemSet> findFrequentOneItemSets(Iterator<Set<String>> trans, int min_sup) {

        //find frequency of items in set
        Map<String, Integer> frequences = new HashMap<String, Integer>();
        while (trans.hasNext()) {
            Set<String> tran = trans.next();
            for (String item : tran) {
                Integer frequence = frequences.get(item);
                frequence = frequence == null ? 1 : frequence + 1;
                frequences.put(item, frequence);
            }
        }

        //1 frequent item set is frequency larger than min_sup
        Set<ItemSet> ret = new HashSet<ItemSet>();
        for (Entry<String, Integer> entry : frequences.entrySet()) {
            String item = entry.getKey();
            Integer frequence = entry.getValue();
            if (frequence >= min_sup) {
                ItemSet set = new ItemSet(new String[] { item });
                set.frequence = frequence;
                ret.add(set);
            }
        }
        return ret;
    }

    //generate L candidate set based on L-1 frequent set
    static List<ItemSet> aprioriGenCandidates(Set<ItemSet> preItemSets) {
        List<ItemSet> ret = new LinkedList<ItemSet>();

        // combine any two of L-1 frequent set to get L candidate set
        for (ItemSet set1 : preItemSets) {
            for (ItemSet set2 : preItemSets) {
                if (set1 != set2 && set1.canMakeJoin(set2)) {

                    // join
                    ItemSet union = new ItemSet();
                    union.addAll(set1);
                    union.add(set2.last());

                    // prune
                    boolean missSubSet = false;
                    List<ItemSet> subItemSets = union.listDirectSubItemSets();
                    for (ItemSet itemSet : subItemSets) {
                        if (!preItemSets.contains(itemSet)) {
                            missSubSet = true;
                            break;
                        }
                    }
                    if (!missSubSet)
                        ret.add(union);
                }
            }
        }
        return ret;
    }

}
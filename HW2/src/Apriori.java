import java.io.*;
import java.util.*;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Apriori {

    public static void main(String[] args) throws Exception {
        int MSF = 2;
        double MCONF = 0.6;
        String data = "src/sdata.dat";
        String output = "result.txt";

        // read data file and init transactions
        List<Set<String>> trans = new LinkedList<Set<String>>();
        List<String> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(data))) {
            list = stream
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0; i<list.size(); i++){
            Set<String> items = new HashSet<String>(Arrays.asList(list.get(i).split(" ")));
            trans.add(items);
        }

        //find frequent item sets
        Map<Integer, Set<ItemSet>> rst = findFrequentItemSets(trans, MSF,output);


        Map<ItemSet, ItemSet> directMap = new HashMap<ItemSet, ItemSet>();
        for (Entry<Integer, Set<ItemSet>> entry : rst.entrySet()) {
            for (ItemSet set : entry.getValue())
                directMap.put(set, set);
        }
        //generate association rule
        //genRule(rst,directMap,MCONF,output);
    }

    //find frequent item set
    static Map<Integer, Set<ItemSet>> findFrequentItemSets(
            Iterable<Set<String>> transIterable, int MSF, String output) {
        Map<Integer, Set<ItemSet>> ret = new TreeMap<Integer, Set<ItemSet>>();

        //find 1 item frequent set
        Iterator<Set<String>> it = transIterable.iterator();
        Set<ItemSet> oneItemSets = findFrequentOneItemSets(it, MSF);
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
                if (candidate.frequence >= MSF)
                    curItemSets.add(candidate);
            if (!curItemSets.isEmpty())
                ret.put(curItemSetSize, curItemSets);

            preItemSetSize = curItemSetSize;
            preItemSets = curItemSets;
        }
        try {
            PrintWriter writer =  new PrintWriter(output);
            writer.write("Frequent Item Sets:");
            //System.out.println("Frequent Item Sets:");
            for (Entry<Integer, Set<ItemSet>> entry : ret.entrySet()) {
                Integer itemSetSize = entry.getKey();
                writer.printf("Frequent %d Item Sets:\n", itemSetSize);
                //System.out.printf("Frequent %d Item Sets:\n", itemSetSize);

                for (ItemSet set : entry.getValue())
                    writer.printf("%s, %d\n", set, set.frequence);
                    //System.out.printf("%s, %d\n", set, set.frequence);
            }
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return ret;
    }

    //find frequent one item set
    static Set<ItemSet> findFrequentOneItemSets(Iterator<Set<String>> trans,
                                                int MSF) {

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
            if (frequence >= MSF) {
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

        // 尝试将所有频繁L-1项集两两连接然后作剪枝处理以获得候选L项集
        for (ItemSet set1 : preItemSets) {
            for (ItemSet set2 : preItemSets) {
                if (set1 != set2 && set1.canMakeJoin(set2)) {

                    // 连接
                    ItemSet union = new ItemSet();
                    union.addAll(set1);
                    union.add(set2.last());

                    // 剪枝
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
    //generate rule
    static void genRule(Map<Integer, Set<ItemSet>> rst,Map<ItemSet, ItemSet> directMap,double MCONF,String output){


    }
}

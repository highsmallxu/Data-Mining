import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangyu on 2016/11/17.
 */
public class Rule {
    //generate rule
    static void genRule(Map<Integer, Set<ItemSet>> rst, double min_conf, String output){
        Map<ItemSet, ItemSet> directMap = new HashMap<ItemSet, ItemSet>();
        for (Map.Entry<Integer, Set<ItemSet>> entry : rst.entrySet()) {
            for (ItemSet set : entry.getValue())
                directMap.put(set, set);
        }
        try {
            PrintWriter writer =  new PrintWriter(new FileWriter(output, true));
            writer.println("Association Rules:");
            //System.out.println("Association Rules:");
            for (Map.Entry<Integer, Set<ItemSet>> entry : rst.entrySet()) {
                for (ItemSet set : entry.getValue()) {
                    double cnt1 = directMap.get(set).frequence;
                    List<ItemSet> subSets = set.listNotEmptySubItemSets();
                    for (ItemSet subSet : subSets) {
                        int cnt2 = directMap.get(subSet).frequence;
                        double conf = cnt1 / cnt2;
                        if (conf >= min_conf) {
                            ItemSet remainSet = new ItemSet();
                            remainSet.addAll(set);
                            remainSet.removeAll(subSet);
                            writer.printf("%s -> %s, %.2f\n", subSet,remainSet, conf);//System.out.printf("%s => %s, %.2f\n", subSet, remainSet, conf);
                        }
                    }
                }
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

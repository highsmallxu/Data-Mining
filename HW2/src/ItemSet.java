import java.util.*;
/**
 * Created by wangyu on 2016/11/14.
 */


class ItemSet extends TreeSet<String> {

    int frequence;

    public ItemSet() {
        this(new String[0]);
    }

    public ItemSet(String[] items) {
        for (String item : items)
            add(item);
    }

    //test if L-1 set can form L set by joining other set
    public boolean canMakeJoin(ItemSet other) {
        //if size is not equal, cannot join
        if (other.size() != this.size())
            return false;

        //if this L-1 set1 has L-2 same items with set2 && L-1 th item in set1 is smaller than which in set2, it can make join
        Iterator<String> it1 = this.iterator();
        Iterator<String> it2 = other.iterator();
        while (it1.hasNext()) {
            String item1 = it1.next();
            String item2 = it2.next();
            int result = item1.compareTo(item2);
            if (result != 0) {
                if (it1.hasNext())
                    return false;
                return result < 0 ? true : false;
            }
        }
        return false;
    }

    //assume this is L set, list all L-1 subset
    public List<ItemSet> listDirectSubItemSets() {
        List<ItemSet> ret = new LinkedList<ItemSet>();
        //for set has subset, it size has to be larger than 1
        if (size() > 1) {
            for (String rmItem : this) {
                ItemSet subSet = new ItemSet();
                subSet.addAll(this);
                subSet.remove(rmItem);
                ret.add(subSet);
            }
        }
        return ret;
    }

    //list all not null subset
    public List<ItemSet> listNotEmptySubItemSets() {
        List<ItemSet> ret = new LinkedList<ItemSet>();
        int size = size();
        if (size > 0) {
            char[] mapping = new char[size()];
            Arrays.fill(mapping,'0');//init mapping
            while (nextMapping(mapping)) {
                ItemSet set = new ItemSet();
                Iterator<String> it = this.iterator();
                for (int i = 0; i < size; i++) {
                    String item = it.next();
                    if (mapping[i] == '1')
                        set.add(item);
                }
                if (set.size() < size)
                    ret.add(set);
            }
        }
        return ret;
    }

    private boolean nextMapping(char[] mapping) {
        int pos = 0;
        while (pos < mapping.length && mapping[pos] == '1') {
            mapping[pos] = '0';
            pos++;
        }
        if (pos < mapping.length) {
            mapping[pos] = '1';
            return true;
        }
        return false;
    }
}
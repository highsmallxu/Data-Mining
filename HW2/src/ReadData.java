import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wangyu on 2016/11/14.
 */
public class ReadData {
    public List<Set<String>> read(String data){
        // read data file and init transactions
        List<Set<String>> trans = new LinkedList<Set<String>>();
        List<String> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(data))) {
            list = stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0; i<list.size(); i++){
            Set<String> items = new HashSet<String>(Arrays.asList(list.get(i).split(" ")));
            trans.add(items);
        }
        return trans;
    }

}

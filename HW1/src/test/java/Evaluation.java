import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;

/**
 * Created by gaoxiaoxu on 10/11/16.
 */

public class Evaluation {

    public List<String> AllDocuments = new ArrayList<String>();
    public static void main(String args[]) {
        Evaluation evaluation = new Evaluation();

        //Get dataset path
        String DatasetPath = evaluation.getDatasetPath("mini_newsgroups");
        //Get all documents path
        List AllDocuments = evaluation.AllDocumentsPath(DatasetPath);


    }

    public String getDatasetPath(String dataset){
        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource(dataset).getPath();
        return path;
    }

    private List AllDocumentsPath(String datasetpath){
        File folder = new File(datasetpath);
        File[] path = folder.listFiles();
        for(int i=0; i<path.length; i++){
            if(path[i].isFile()){
                AllDocuments.add(path[i].getPath());
            }
            else if(path[i].isDirectory()){
                AllDocumentsPath(path[i].getPath());
            }
        }
        return AllDocuments;
    }


}

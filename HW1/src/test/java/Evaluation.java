import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.util.SystemClock;
import scala.reflect.internal.Trees;

/**
 * Created by gaoxiaoxu on 10/11/16.
 */

public class Evaluation {



    public List<String> AllDocuments = new ArrayList<String>();
    public static void main(String args[]) throws IOException{

        Evaluation evaluation = new Evaluation();

        //Get dataset path
        String DatasetPath = evaluation.GetDatasetPath("mini_newsgroups");
        //Get all documents path
        List<String> AllDocuments = evaluation.AllDocumentsPath(DatasetPath);

        //Tokenization, remove punctuation
        List<String> NewDocument = evaluation.Tokenization(AllDocuments.get(0));
        String NewDocumentString = StringUtils.join(NewDocument, " ");

        //k-shinglings
        Shingling k_shingling = new Shingling();
        k_shingling.Shingling(NewDocumentString,10);


        //Build universe HashMap
        Set<String> UniverseHashMap = new HashSet<String>();

        Set<String> seta = new HashSet<String>();
        Set<String> setb = new HashSet<String>();





    }

    public List Tokenization(String documentpath) throws FileNotFoundException, IOException{
        List NewDocument = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(documentpath));
        while(true){
            String line = br.readLine();
            if (line==null){
                break;
            }
            String newline = line.replaceAll("\\W+"," ");
            if(newline.length()==0){
                continue;
            }
            else{
                List temp = new ArrayList();
                NewDocument.add(newline);
            }
        }

        return NewDocument;
    }

    public String GetDatasetPath(String dataset){
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

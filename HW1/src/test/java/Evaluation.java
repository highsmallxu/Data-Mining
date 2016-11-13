import java.io.*;
import java.util.*;

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
        Shingling k_shingling = new Shingling();
        Integer k_shingle = 10;
        Integer numHash = 100;


        //Get dataset path
        String DatasetPath = evaluation.GetDatasetPath("mini_newsgroups");
        //Get all documents path
        List<String> AllDocuments = evaluation.AllDocumentsPath(DatasetPath);
        //Initial document
        String InitialDocumentPath = AllDocuments.get(0);
        String InitialDocument = evaluation.RemoveSpaceAndJoint(InitialDocumentPath);
        List<Integer> InitialDocumentShingle = k_shingling.Shingling(InitialDocument,k_shingle);

        //Build Matrix
        Map<Integer, boolean[]> InitialInputMatrix = new HashMap<Integer,boolean[]>();
        for(int i=0;i<InitialDocumentShingle.size();i++){
            InitialInputMatrix.put(InitialDocumentShingle.get(i),new boolean[]{true,false});
        }
        //Compated Documents
        Map<Integer, boolean[]> ComparedInputMatrix;
        Integer intersection;
        Integer NumberofShingles;
        Random r = new Random(100);
        for (int i=0;i<AllDocuments.size();i++) {
            ComparedInputMatrix = InitialInputMatrix;
            intersection = 0;
            String ComparedDocumentPath = AllDocuments.get(i);
            String ComparedDocument = evaluation.RemoveSpaceAndJoint(ComparedDocumentPath);
            List<Integer> ComparedDocumentShingle = k_shingling.Shingling(ComparedDocument, k_shingle);
            for (int j = 0; j < ComparedDocumentShingle.size(); j++) {
                if (ComparedInputMatrix.containsKey(ComparedDocumentShingle.get(j))) {
                    ComparedInputMatrix.put(ComparedDocumentShingle.get(j), new boolean[]{true, true});
                    intersection += 1;
                } else if (!ComparedInputMatrix.containsKey(ComparedDocumentShingle.get(j))) {
                    ComparedInputMatrix.put(ComparedDocumentShingle.get(j), new boolean[]{false, true});
                }
            }
            System.out.print(intersection+"\n");
            NumberofShingles = ComparedInputMatrix.size();

            List<Integer> coeffA = evaluation.pickRandoCoeffs(numHash);
            List<Integer> coeffB = evaluation.pickRandoCoeffs(numHash);

            Integer maxShingleID = (int)Math.pow(2,25) -1;
            Integer nextPrime = maxShingleID + 16;
            List<Integer> signature = new ArrayList<Integer>();
            for (int z=0;z<numHash;z++){
                Integer minHash = nextPrime + 1;
                for (int m=0;m<ComparedDocumentShingle.size();m++){
                    Integer hashCode = (coeffA.get(z)*ComparedDocumentShingle.get(m) + coeffB.get(z)) % nextPrime;
                    if(hashCode<minHash){
                        minHash = hashCode;
                    }
                }
                signature.add(minHash);
            }

            System.out.print("dd");

        }


    }



    public List<Integer> pickRandoCoeffs(Integer NumberofHash){

        Integer maxShingleID = (int)Math.pow(2,32) -1;
        Integer nextPrime = maxShingleID + 16;
        List<Integer> randList = new ArrayList<Integer>();
        while(NumberofHash>0) {
            Random generator = new Random();
            Integer randIndex = generator.nextInt(maxShingleID);
            while (randList.contains(randIndex)) {
               // Random generator2 = new Random();
                randIndex = generator.nextInt(maxShingleID);
            }
            randList.add(randIndex);
            NumberofHash -= 1;
        }
        return randList;
    }

    public String RemoveSpaceAndJoint(String documentpath) throws FileNotFoundException, IOException{
        List NewDocument = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(documentpath));
        while(true){
            String line = br.readLine();
            if (line==null){
                break;
            }
            String newlines = line.replaceAll("\\p{Punct}","");
            String newline = (newlines.replaceAll("\\s+","_"))+"_";
            //System.out.println(newlines.charAt(0));
            if(newline.length()==0){
                continue;
            }
            else{
                List temp = new ArrayList();
                NewDocument.add(newline);
            }
        }
        String NewDocumentString = StringUtils.join(NewDocument, " ");
        return NewDocumentString;
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

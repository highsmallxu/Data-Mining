import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by gaoxiaoxu on 14/11/16.
 */
public class TestWithoutSignature {

    public static void main(String arg[]) throws IOException{

        //Get Dataset Path
        //Get AllDocuments Paths
        Preprocessing PreprocessingClass = new Preprocessing();
        List<String> AllDocumentsPath = PreprocessingClass.ReadingFilesMain("test");

        //Get InitialDocument
        //--Get random document index
        Random r = new Random();
        Integer InitialDocumentIndex = r.nextInt(AllDocumentsPath.size());
        //--Get current document path
        String InitialDocumentPath = AllDocumentsPath.get(0);
        //--Remove space and join sentences
        String InitialDocument = PreprocessingClass.RemoveSpaceAndJoint(InitialDocumentPath);
        //--Create k-shingles
        minHash minHashClass = new minHash();
        Integer k = 10;
        List<Integer> InitialDocumentShingles = minHashClass.k_shingle(InitialDocument,k);
        //--Create HashMap
        Map<Integer,boolean[]> InitialDocumentMatrix;

        //Get ComparedDocuments
        Map<Integer,boolean[]> ComparedDocumentMatrix;
        Integer InterSection;
        Double threshold = 0.6;
        for(int i=0;i<AllDocumentsPath.size();i++){
            InterSection = 0;
            InitialDocumentMatrix = minHashClass.HashSingleMatrix(InitialDocumentShingles);
            ComparedDocumentMatrix = InitialDocumentMatrix;
            String ComparedDocumentPath = AllDocumentsPath.get(i);
            String ComparedDocument = PreprocessingClass.RemoveSpaceAndJoint(ComparedDocumentPath);
            List<Integer> ComparedDocumentShingles = minHashClass.k_shingle(ComparedDocument,k);
            List e = minHashClass.HashComparedMatrix(ComparedDocumentShingles,ComparedDocumentMatrix,InterSection);
            ComparedDocumentMatrix = new ObjectMapper().convertValue(e.get(0),Map.class);
            InterSection = Integer.valueOf((Integer) e.get(1));

            Double fraction = (double)InterSection/ComparedDocumentMatrix.size();
            //System.out.println(String.format("%.2f",fraction));
            if(fraction>threshold){
                File SimilarDocument = new File(AllDocumentsPath.get(i));
                System.out.print(SimilarDocument.getName()+"\n");
            }
        }
    }
}

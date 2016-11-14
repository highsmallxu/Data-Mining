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
        List<String> AllDocumentsPath = PreprocessingClass.ReadingFilesMain("mini_newsgroups");

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
        private static Map<Integer,boolean[]> InitialDocumentMatrix;
        InitialDocumentMatrix = minHashClass.HashSingleMatrix(InitialDocumentShingles);


        //Get ComparedDocuments
        Map<Integer,boolean[]> ComparedDocumentMatrix;
        Integer InterSection;
        for(int i=0;i<AllDocumentsPath.size();i++){
            InterSection = 0;
            ComparedDocumentMatrix = InitialDocumentMatrix;
            String ComparedDocumentPath = AllDocumentsPath.get(i);
            String ComparedDocument = PreprocessingClass.RemoveSpaceAndJoint(ComparedDocumentPath);
            List<Integer> ComparedDocumentShingles = minHashClass.k_shingle(ComparedDocument,k);
            for(int j=0;j<ComparedDocumentShingles.size();j++){
                if(ComparedDocumentMatrix.containsKey(ComparedDocumentShingles.get(j))){
                    ComparedDocumentMatrix.put(ComparedDocumentShingles.get(j), new boolean[]{true, true});
                    InterSection += 1;
                }
                else if(!ComparedDocumentMatrix.containsKey(ComparedDocumentShingles.get(j))){
                    ComparedDocumentMatrix.put(ComparedDocumentShingles.get(j), new boolean[]{false,true});
                }
            }
            if(InterSection>1119){
                System.out.print("warning");
            }
            System.out.print(InterSection+"\n");
//            Double fraction = (double)InterSection/ComparedDocumentMatrix.size();
//            System.out.println(String.format("%.2f",fraction));
        }


    }
}

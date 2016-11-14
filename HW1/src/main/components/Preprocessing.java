import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoxiaoxu on 14/11/16.
 */
public class Preprocessing {

    public static List<String> AllDocumentsPath = new ArrayList<String>();
    public List<String> ReadingFilesMain(String dataset){
        String datasetpath = ReadingDatasetPath(dataset);
        List<String> AllDocumentsPath = ReadingFilesPath(datasetpath);
        return AllDocumentsPath;
    }

    public String ReadingDatasetPath(String dataset){
        //initialization
        ClassLoader classLoader = getClass().getClassLoader();
        //Get dataset path
        String datasetpath = classLoader.getResource(dataset).getPath();
        return datasetpath;
    }

    public List<String> ReadingFilesPath(String datasetpath){
        //initialization
        File folder = new File(datasetpath);
        //Get files path
        File[] paths = folder.listFiles();
        for(int i=0;i<paths.length;i++){
            if(paths[i].isFile()){
                AllDocumentsPath.add(paths[i].getPath());
            }
            else if(paths[i].isDirectory()){
                ReadingFilesPath(paths[i].getPath());
            }
        }
        return AllDocumentsPath;
    }

    public String RemoveSpaceAndJoint(String documentpath) throws FileNotFoundException, IOException{
        //initialization
        BufferedReader br = new BufferedReader(new FileReader(documentpath));
        List NewDocument = new ArrayList();
        //RemoveSpace
        while(true){
            String line = br.readLine();
            if(line==null){
                break;
            }
            String LineRemovePunct = line.replaceAll("\\p{Punct}","");
            String NewLine = (LineRemovePunct.replaceAll("\\s+","_"))+"_";
            if(NewLine.length()==0){
                continue;
            }
            else{
                NewDocument.add(NewLine);
            }
        }
        //JoinSentence
        String NewDocumentString = StringUtils.join(NewDocument, " ");
        return NewDocumentString;
    }
}

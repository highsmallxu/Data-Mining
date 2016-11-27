package com.company;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoxiaoxu on 14/11/16.
 */
public class Preprocessing {

    public static List<String> AllDocumentsPath = new ArrayList<String>();
    public List<String> ReadingFilesMain(String dataset){
        String paths = Paths.get(".").toAbsolutePath().normalize().toString();
        String path  = paths + "/src/" + dataset;
        File folder = new File(path);
        File[] files = folder.listFiles();

        List<String> AllDocumentsPath = new ArrayList<String>();
        for(int i=0;i<files.length;i++){
            if(files[i].isFile()){
                AllDocumentsPath.add(files[i].getPath());
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
        //NewDocument.toString();
        String NewDocumetString = NewDocument.get(0).toString();
        return NewDocumetString;
    }
}


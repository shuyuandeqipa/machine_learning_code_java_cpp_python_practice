package c45_pruning;

import java.util.ArrayList;

public class Main {


    public static void main(String []args){

        ReadDateFromFile readDateFromFile = new ReadDateFromFile(Config.trainDataPath, null, Config.testDataPath);
        ArrayList<String> allFeatures = readDateFromFile.readAllFeatures();
        ArrayList<DataItem> trainDataItems = readDateFromFile.readTrainData();
        ArrayList<DataItem> testDataItems = readDateFromFile.readTestData();
        Dataset dataset=new Dataset(trainDataItems,null,testDataItems,allFeatures);
        dataset.setClassFeature(allFeatures.get(allFeatures.size()-1));
        System.out.println(dataset);


    }
}

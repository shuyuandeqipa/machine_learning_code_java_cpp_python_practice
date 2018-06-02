package c45_pruning;

import c45_pruning.ConfigsAndEnums.Config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReadDateFromFile {
    String trainDatasetPath;
    String validateDatasetPath;
    String testDatasetPath;

    public ReadDateFromFile() {

    }

    public ReadDateFromFile(String trainDatasetPath, String validateDatasetPath, String testDatasetPath) {
        this.trainDatasetPath = trainDatasetPath;
        this.validateDatasetPath = validateDatasetPath;
        this.testDatasetPath = testDatasetPath;
    }

    public ArrayList<String> readAllFeatures() {
        ArrayList<String> allFeatures = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(trainDatasetPath)));
            String features[] = bufferedReader.readLine().split(" ");
            for (int i = 0; i < features.length - 1; i++) {
                allFeatures.add(features[i]);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return allFeatures;
    }

    public ArrayList<DataItem> readData(String path) {
        ArrayList<DataItem> dataItems = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String data = new String();
            //色泽  根蒂  敲声  纹理  脐部  触感  好瓜
            String features[] = bufferedReader.readLine().split(" ");//read the features
            while ((data = bufferedReader.readLine()).trim().length() != 0) {
                String datas[] = data.trim().split(" ");//01  青绿  蜷缩  浊响  清晰  凹陷  硬滑  是
                DataItem dataItem = new DataItem();
                dataItem.setId(Integer.parseInt(datas[0]));
                Map<String, String> dataItemFeatures = new HashMap<>();
                for (int i = 1; i < datas.length - 1; i++) {
                    dataItemFeatures.put(features[i - 1], datas[i]);
                }
                dataItem.setDataItemFeatures(dataItemFeatures);
                dataItem.setLabel(datas[datas.length - 1]);
                dataItems.add(dataItem);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataItems;
    }

    public ArrayList<DataItem> readTrainData() {
        return readData(Config.trainDataPath);
    }

    public ArrayList<DataItem> readValidateData() {
        return readData(Config.validatingDataPath);
    }

    public ArrayList<DataItem> readTestData() {
        return readData(Config.testDataPath);
    }
}

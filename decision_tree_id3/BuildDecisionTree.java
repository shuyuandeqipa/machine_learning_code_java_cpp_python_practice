import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BuildDecisionTree {

    public static void buildDecisionTree() {
        //1.read the dataset
        String fileName = "E:\\machineLearningJavaCpp\\DecisionTree\\data\\xigua.txt";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            int numberOfDataItems = 17;//数据项的数量
            DataSet dataSet;
            ArrayList<DataItem> dataItems = new ArrayList<>();
            for (int i = 0; i < numberOfDataItems; ++i) {
                String features[] = bufferedReader.readLine().split("  ");
                int id = Integer.parseInt(features[0]);
                ArrayList<String> featuresOfDataItem = new ArrayList<>();
                for (int j = 1; j <= features.length - 2; ++j) {
                    featuresOfDataItem.add(features[j]);
                }
                String label = features[features.length - 1];
                DataItem dataItem = new DataItem(id, featuresOfDataItem, label);
                dataItems.add(dataItem);
            }
            String featureInfos[] = bufferedReader.readLine().split("  ");
            ArrayList<String> allFeatures = new ArrayList<>();
            for (int i = 0; i <= featureInfos.length - 2; i++) {
                allFeatures.add(featureInfos[i]);
            }
            dataSet = new DataSet(dataItems, allFeatures, featureInfos[featureInfos.length - 1]);
            System.out.println("output the dataset information :");
            System.out.println(dataSet);//输出所有数据的信息
            System.out.println("output the information gain for the first layer :");
            for(int i=0;i<dataSet.allFeatures.size();i++){
                String s="feature="+dataSet.allFeatures.get(i)+" "+",information gain = ";
                s+=(new InformationGain(dataSet.dataSet,i)).getGain()+"";
                System.out.println(s);
            }
            //2.build the decision tree
            ID3Solver solver=new ID3Solver(dataSet);
            solver.id3();
        } catch (IOException exception) {
            exception.printStackTrace();
        }


    }

    public static void main(String[] args) {
        BuildDecisionTree.buildDecisionTree();
    }
}

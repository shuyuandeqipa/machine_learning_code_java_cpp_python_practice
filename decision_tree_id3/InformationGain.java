import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class InformationGain {

    ArrayList<DataItem>dataItems;
   //根据这个feature将数据分开
    int featureIndex;
    double gain;//信息增益

    public double getGain() {
        return gain;
    }

    public InformationGain(ArrayList<DataItem> dataItems,int featureIndex) {
        this.dataItems = dataItems;

        this.featureIndex=featureIndex;
        this.gain = computeInformationGain(this.dataItems,this.featureIndex);
    }

    private double computeInformationGain(ArrayList<DataItem> dataItems, int featureIndex) {
        double gain = 0.0;

        double dataItemsEntropy=new InformationEntropy(dataItems).getEntropy();
        gain=dataItemsEntropy;
        // 获取featureIndex下面所有的类别
        Set<String> allDataItemFeatureOfThisFeature = new HashSet<>();
        for (DataItem dataItem : dataItems) {
            allDataItemFeatureOfThisFeature.add(dataItem.getDataItemFeatureWithIndex(featureIndex));
        }

        for(String feature :allDataItemFeatureOfThisFeature){
            ArrayList<DataItem>tempDataItems=DataSet.getDataItemsWithDataItemFeature(dataItems,featureIndex,feature);
            gain=gain-(double) tempDataItems.size()/(double)dataItems.size()*(new InformationEntropy(tempDataItems).getEntropy());
        }

        return gain;
    }
}

package c45_pruning;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class DataItem {
    int id;
    Map<String, String> dataItemFeatures;
    String label;

    //**********************************************************
    public String getDataItemFeatureWithFeature(String featureKey){
        if(dataItemFeatures.containsKey(featureKey)){
            return dataItemFeatures.get(featureKey);
        }else{
            return null;
        }
    }

    //**********************************************************
    @Override
    public String toString() {
        String s = "" + id + " ";
        Set<String>keyset=dataItemFeatures.keySet();
        //output all the features of  a dataItem
        for(String key :keyset){
            s+=" key= "+key+" data= "+dataItemFeatures.get(key)+",";
        }
        s += label + "\n";

        return s;
    }
    public String outputFeatureKeys(){
        String s = "";
        Set<String>keyset=dataItemFeatures.keySet();
        for(String key:keyset){
            s=s+key+" ";
        }
        return  s;
    }

    public DataItem(int id, Map<String, String> dataItemFeatures, String label) {
        this.id = id;
        this.dataItemFeatures = dataItemFeatures;
        this.label = label;
    }

    public DataItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, String> getDataItemFeatures() {
        return dataItemFeatures;
    }

    public void setDataItemFeatures(Map<String, String> dataItemFeatures) {
        this.dataItemFeatures = dataItemFeatures;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

package c45_pruning;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Dataset {

    ArrayList<DataItem> trainDataset;
    ArrayList<DataItem> validateDataset;
    ArrayList<DataItem> testDataset;
    public DataItem findTrainDataItemWithId(Integer id){
        for(DataItem item:trainDataset){
            if(item.getId()==id){
                return item;
            }
        }
        return null;
    }
    public DataItem findValidatingDataItemWithId(Integer id){
        for(DataItem item:validateDataset){
            if(item.getId()==id){
                return item;
            }
        }
        return null;
    }
    public DataItem findTestDataItemWithId(Integer id){
        for(DataItem item:testDataset){
            if(item.getId()==id){
                return item;
            }
        }
        return null;
    }


    ArrayList<String> features;// all the features in data used to build the decision tree
    String classFeature;// the feature that used to classify the data

    public Dataset(ArrayList<DataItem> trainDataset, ArrayList<DataItem> validateDataset, ArrayList<DataItem> testDataset, ArrayList<String> features) {
        this.trainDataset = trainDataset;
        this.validateDataset = validateDataset;
        this.testDataset = testDataset;
        this.features = features;
        this.numberOfTestDataItems = testDataset == null ? 0 : testDataset.size();
        this.numberOfTrainDataItems = trainDataset == null ? 0 : trainDataset.size();
        this.numberOfValidateDataItems = validateDataset == null ? 0 : validateDataset.size();
    }

    Integer numberOfTrainDataItems;
    Integer numberOfValidateDataItems;
    Integer numberOfTestDataItems;


    //***************************************************************
    //select all the dataItems from dataItems who's feature at featureKey is dataItemFeature
    public static ArrayList<DataItem> getDataItemsWithDataItemFeature(ArrayList<DataItem>dataItems,String  featureKey,String dataItemFeature) {
        ArrayList<DataItem> arrayList=new ArrayList<>();
        for (DataItem item : dataItems) {
            if (item.dataItemFeatures.get(featureKey).equals(dataItemFeature)) {
                arrayList.add(item);
            }
        }
        return arrayList;
    }

    // select all the values  for a feature in the dataItems
    public static Set<String>getAttributesForThisFeature(ArrayList<DataItem>dataItems,String feature){
        Set<String> attributes=new HashSet<>();
        for(DataItem item:dataItems){
            attributes.add(item.dataItemFeatures.get(feature));
        }
        return  attributes;
    }



    //************************************************************
    public ArrayList<DataItem> getTrainDataset() {
        return trainDataset;
    }

    public void setTrainDataset(ArrayList<DataItem> trainDataset) {
        this.trainDataset = trainDataset;
    }

    public ArrayList<DataItem> getValidateDataset() {
        return validateDataset;
    }

    public void setValidateDataset(ArrayList<DataItem> validateDataset) {
        this.validateDataset = validateDataset;
    }

    public ArrayList<DataItem> getTestDataset() {
        return testDataset;
    }

    public void setTestDataset(ArrayList<DataItem> testDataset) {
        this.testDataset = testDataset;
    }

    public ArrayList<String> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<String> features) {
        this.features = features;
    }

    public String getClassFeature() {
        return classFeature;
    }

    public void setClassFeature(String classFeature) {
        this.classFeature = classFeature;
    }

    public Integer getNumberOfTrainDataItems() {
        return numberOfTrainDataItems;
    }

    public void setNumberOfTrainDataItems(Integer numberOfTrainDataItems) {
        this.numberOfTrainDataItems = numberOfTrainDataItems;
    }

    public Integer getNumberOfValidateDataItems() {
        return numberOfValidateDataItems;
    }

    public void setNumberOfValidateDataItems(Integer numberOfValidateDataItems) {
        this.numberOfValidateDataItems = numberOfValidateDataItems;
    }

    public Integer getNumberOfTestDataItems() {
        return numberOfTestDataItems;
    }

    public void setNumberOfTestDataItems(Integer numberOfTestDataItems) {
        this.numberOfTestDataItems = numberOfTestDataItems;
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "trainDataset=" + trainDataset +
                ", validateDataset=" + validateDataset +
                ", testDataset=" + testDataset +
                ", features=" + features +
                ", classFeature='" + classFeature + '\'' +
                ", numberOfTrainDataItems=" + numberOfTrainDataItems +
                ", numberOfValidateDataItems=" + numberOfValidateDataItems +
                ", numberOfTestDataItems=" + numberOfTestDataItems +
                '}';
    }
}

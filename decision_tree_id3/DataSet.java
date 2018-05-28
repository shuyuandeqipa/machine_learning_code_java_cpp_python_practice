import java.util.*;

public class DataSet {

    ArrayList<DataItem>dataSet;//整个数据集中的数据
    ArrayList<String>allFeatures;//每个属性的名字
    boolean allFeaturesIsUsed[];
    String className;//比如是否是好瓜
    public DataSet(ArrayList<DataItem> dataSet, ArrayList<String> allFeatures,String className) {
        this.dataSet = dataSet;
        this.allFeatures = allFeatures;
        this.className=className;
        allFeaturesIsUsed=new boolean[allFeatures.size()];
    }

    public void setAllFeaturesIsUsedWithIndex(int index,boolean isUsed) {
        this.allFeaturesIsUsed[index]=isUsed;
    }
    public boolean getAllFeaturesIsUsedWithIndex(int index){
        return this.allFeaturesIsUsed[index];
    }

    @Override
    public String toString() {
      String s="";
      for(int i=0;i<dataSet.size();i++){
          s+=dataSet.get(i);
      }

      for(int i=0;i<allFeatures.size();i++){
          s+=allFeatures.get(i)+" ";
      }
      s+=className+"\n";
      return s;
    }
    //*******************************************************************************
    // useful functions
    public ArrayList<String>getFeatureDetailsInfoWithIndex(int index){
        Set<String> set=new HashSet<>();
        for(DataItem item:dataSet){
            set.add(item.getDataItemFeatureWithIndex(index));
        }
        ArrayList<String >childPointerInfo=new ArrayList<>();
        for(String s:set){
            childPointerInfo.add(s);
        }
        return childPointerInfo;
    }
    public static  ArrayList<String>getFeatureDetailsInfoWithIndex(ArrayList<DataItem>dataItems,int index){
        Set<String>set=new HashSet<>();
        for(DataItem item:dataItems){
            set.add(item.getDataItemFeatureWithIndex(index));
        }
        ArrayList<String>childPointerInfo=new ArrayList<>();
        for(String info:set){
            childPointerInfo.add(info);
        }
        return childPointerInfo;
    }

    public void addDataItem2DataSet(DataItem item){
        this.dataSet.add(item);
    }

    public String getFeatureWithIndex(int index){
        return this.allFeatures.get(index);
    }
    //从数据集中选取index对应的属性等于需要的dataItemFeature的所有的数据
    public static ArrayList<DataItem> getDataItemsWithDataItemFeature(ArrayList<DataItem>dataItems,int featureIndex,String dataItemFeature) {
        ArrayList<DataItem> arrayList=new ArrayList<>();
        for (DataItem item : dataItems) {
            if (item.dataItemFeatures.get(featureIndex).equals(dataItemFeature)) {
                arrayList.add(item);
            }
        }
        return arrayList;
    }

    public static boolean allDataItemsFlagIsSame(ArrayList<DataItem>dataItems){
        boolean isSame=true;
        Set<String>set=new HashSet<>();
        for(int i=0;i<dataItems.size();i++){
            set.add(dataItems.get(i).getLabel());
        }
        if(set.size()==1){
            isSame=true;
        }else{
            isSame=false;
        }
        return isSame;
    }
    //返回一个数据集合中同一个属性的所有的类别取值
    public static Set<String>getAllFeatureDetailInfoWithIndex(ArrayList<DataItem>dataItems,int index){
        Set<String>set=new HashSet<>();
        for(DataItem item:dataItems){
            set.add(item.getDataItemFeatureWithIndex(index));
        }
        return set;
    }
    public static String getLabelWithVoting(ArrayList<DataItem>dataItems){
        String label="";
        Map<String ,Integer>m=new HashMap<>();
        for(DataItem dataItem:dataItems){
            if(m.containsKey(dataItem.getLabel())){
                m.put(dataItem.getLabel(),m.get(dataItem.getLabel())+1);
            }else{
                m.put(dataItem.getLabel(),1);
            }
        }
        int max=0;
        Set<String>keySet=m.keySet();
        for(String key:keySet){
            if(m.get(key)>max){
                max=m.get(key);
                label=key;
            }
        }
        return label;
    }
    //**********************************************************************
    public ArrayList<DataItem> getDataSet() {
        return dataSet;
    }

    public void setDataSet(ArrayList<DataItem> dataSet) {
        this.dataSet = dataSet;
    }

    public ArrayList<String> getAllFeatures() {
        return allFeatures;
    }

    public void setAllFeatures(ArrayList<String> allFeatures) {
        this.allFeatures = allFeatures;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}

import java.util.ArrayList;

public class DataItem {
    int id;//数据项的编号
    ArrayList<String> dataItemFeatures;//一个数据项的所有的属性,如果有数据缺失，使用""代替
    String label; //属于的类别

    public DataItem(int id, ArrayList<String> dataItemFeatures, String label) {
        this.id = id;
        this.dataItemFeatures = dataItemFeatures;
        this.label = label;
    }

    @Override
    public String toString() {
        String s = "" + id + " ";
        for (int i = 0; i < dataItemFeatures.size(); i++) {
            s += dataItemFeatures.get(i) + " ";
        }
        s += label + "\n";

        return s;
    }

    //***********************************************************************
    //useful functions
    public String getDataItemFeatureWithIndex(int index){
        String needFeature="";
       if(index<0 || index>=dataItemFeatures.size()){
        throw new ArrayIndexOutOfBoundsException();
       }else{
           needFeature=dataItemFeatures.get(index);
       }
       return needFeature;
    }

    //********************************************************************
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getDataItemFeatures() {
        return dataItemFeatures;
    }

    public void setDataItemFeatures(ArrayList<String> dataItemFeatures) {
        this.dataItemFeatures = dataItemFeatures;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}

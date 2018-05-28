import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InformationEntropy {
    ArrayList<DataItem>dataItems;
    double entropy;

    public double getEntropy() {
        return entropy;
    }


    public InformationEntropy(ArrayList<DataItem> dataItems) {
        this.dataItems = dataItems;
        this.entropy = computeInformationEntropy(this.dataItems);
    }


    //计算信息熵
    public static double computeInformationEntropy(ArrayList<DataItem> dataItems) {
        ArrayList<Double> pks = computePks(dataItems);
        double entropy = 0.0;
        for (double pk : pks) {
            entropy += -compute(pk);
        }
        return entropy;
    }

    public static ArrayList<Double> computePks(ArrayList<DataItem> dataItems) {
        int numberOfItems = dataItems.size();
        ArrayList<Double> pks = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (DataItem item : dataItems) {
            String label = item.label;
            if (map.containsKey(label)) {
                map.put(label, map.get(label) + 1);
            } else {
                map.put(label, 1);
            }
        }

        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            pks.add(map.get(key) / (double) numberOfItems);
        }
        return pks;
    }

    private static double compute(double pk) {
        if (pk == 0) {
            return 0.0;
        } else {
            return pk * Math.log10(pk) / Math.log10(2.0);
        }
    }

//  public static void main(String[]args){
//      ArrayList<DataItem>dataItems=new ArrayList<>();
//      dataItems.add(new DataItem(1,null,"是"));
//      dataItems.add(new DataItem(2,null,"是"));
//      dataItems.add(new DataItem(3,null,"是"));
//      dataItems.add(new DataItem(4,null,"是"));
//      dataItems.add(new DataItem(5,null,"是"));
//      dataItems.add(new DataItem(6,null,"是"));
//      dataItems.add(new DataItem(7,null,"是"));
//      dataItems.add(new DataItem(8,null,"是"));
//
//      dataItems.add(new DataItem(9,null,"否"));
//      dataItems.add(new DataItem(10,null,"否"));
//      dataItems.add(new DataItem(11,null,"否"));
//      dataItems.add(new DataItem(12,null,"否"));
//      dataItems.add(new DataItem(13,null,"否"));
//      dataItems.add(new DataItem(14,null,"否"));
//      dataItems.add(new DataItem(15,null,"否"));
//      dataItems.add(new DataItem(16,null,"否"));
//      dataItems.add(new DataItem(17,null,"否"));
//
//
//
//      System.out.println(new InformationEntropy(dataItems).getEntropy());
//  }

}

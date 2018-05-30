package c45_pruning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * all the code here is designed to pruning the tree.
 * So I will write kinds of approach to handle the pruning work.
 *
 */
public class PruningUtils {


    public static String selectClassLabelWithVoting(ArrayList<DataItem> dataItems) {
        if(dataItems.size()<=0){
            return "";
        }
        Map<String, Integer> map = new HashMap<>();
        for (DataItem item : dataItems) {
            if (map.containsKey(item.label)) {
                Integer num = map.get(item.label);
                map.put(item.label, num + 1);
            } else {
                map.put(item.label, 1);
            }
        }
        Set<String> keyset = map.keySet();
        Integer max = -1;
        String label = "";
        for (String key : keyset) {
            if (map.get(key) > max) {
                max = map.get(key);
                label = key;
            }
        }
        return label;
    }

    // judge whether the data needs to prune
    public static boolean needDoPrePruning(ArrayList<DataItem>dataItems,ArrayList<String>features){
        // 1.使用投票法选出这些dataItems中的数据数目多的那个类别作为如果进行剪枝之后的那个类别
        String label=selectClassLabelWithVoting(dataItems);
        if(label.trim().length()==0){//在外面，我是不会让dataItems中的数据为空这种情况出现的
            return true;
        }
        //2.判断如果进行了剪枝，那么正确率是多少
        int num=0;
        for(DataItem item:dataItems){
            if(item.getLabel().equals(label)){
                num++;
            }
        }
        double accuracy=(double) num/(double)dataItems.size();
        //3.如果不进行剪枝，接着往下做一步，看看正确率是多少



        return false;
    }


    public static boolean needDoPostPruning(ArrayList<Integer>ids,ArrayList<String>features){


        return false;
    }

}

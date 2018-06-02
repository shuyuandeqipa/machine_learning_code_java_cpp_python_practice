package c45_pruning;

import c45_pruning.ConfigsAndEnums.FeatureSelectCriterions;
import c45_pruning.ConfigsAndEnums.TreeNodeType;
import c45_pruning.math.DecisionTreeMathUtils;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import java.util.*;

/**
 * all the code here is designed to pruning the tree.
 * So I will write kinds of approach to handle the pruning work.
 */
public class PruningUtils {
    public static String selectClassLabelWithVoting(ArrayList<DataItem> dataItems) {
        if (dataItems.size() <= 0) {
            return null;// 注意这里
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

    // 需要使用为完成的树进行向下进行和预测
    // 这里的判断方式是错误的，必须要从上到下进行预测，然后判断预测是否和真实的label相等
    public static double getValidatingAccuracyWhenJudgePruning(Map<String, String> childrenClassLabels,
                                                               ArrayList<DataItem> validatingItems, String feature,
                                                               Set<String> attrs) {
        double validatingAccuracy = 0.0;
        int num = 0;
        for (String attr : attrs) {
            ArrayList<DataItem> dataItems = Dataset.getDataItemsWithDataItemFeature(validatingItems, feature, attr);
            String predictClassLabel = childrenClassLabels.get(attr);
            for (DataItem item : dataItems) {
                if (predictClassLabel.equals(item.getLabel())) {
                    num++;
                }
            }
        }
        validatingAccuracy = (double) num / (double) validatingItems.size();
        return validatingAccuracy;
    }

    // judge whether the data needs to prune
    public static boolean needDoPrePruning(ArrayList<DataItem> nodeDataItems, ArrayList<DataItem> validatingItems,
                                           ArrayList<String> features, FeatureSelectCriterions criterion) {
        // 1.使用投票法选出这些trainItems中的数据数目多的那个类别作为如果进行剪枝之后的那个类别
        String label = selectClassLabelWithVoting(nodeDataItems);
        if (label.trim().length() == 0) {//在外面，我是不会让dataItems中的数据为空这种情况出现的
            return true;
        }
        //2.判断如果进行了剪枝，那么在验证集上正确率是多少
        int num = 0;
        for (DataItem item : validatingItems) {
            if (item.getLabel().equals(label)) {
                num++;
            }
        }
        double accuracy = (double) num / (double) validatingItems.size();
        //3.如果不进行剪枝，接着往下做一步，看看正确率是多少
        /**
         * 1.根据划分准则选择一个划分属性
         *  根据这个属性将数据项进行划分，
         * 2.向下划分一步，然后计算正确率是多少
         */
        String bestFeature = DecisionTreeMathUtils.selectBestFeature(nodeDataItems, features, criterion);
        Set<String> attributes = Dataset.getAttributesForThisFeature(nodeDataItems, bestFeature);
        Map<String, String> childrenClassLabels = new HashMap<>();
        for (String attr : attributes) {
            //这里选择出来的child是不会为空的
            ArrayList<DataItem> child = Dataset.getDataItemsWithDataItemFeature(nodeDataItems, bestFeature, attr);
            childrenClassLabels.put(attr, selectClassLabelWithVoting(child));
        }
        /**
         * 3.在验证集上面统计正确率是多少
         */
        double validatingAccuracy = getValidatingAccuracyWhenJudgePruning(childrenClassLabels, validatingItems, bestFeature, attributes);
        // 如果把树继续向下进行的效果更好，那么就不剪枝
        if (validatingAccuracy > accuracy) {
            return false;
        } else {
            return true;
        }
    }
    public static String pruningPredict(DecisionTree tree,DataItem dataItem,Dataset dataset) {
        TreeNode root=tree.rootNode;
        while(root.nodeType!=TreeNodeType.LEAF_NODE&& root.needDoPruning==false){
            String feature=root.nodeDivisionFeature;
            String dataItemFeature=dataItem.dataItemFeatures.get(feature);
            for(TreeNode item:root.children) {
                if (dataItemFeature.equals(item.pointerInfo)) {
                    root = item;
                }
            }
        }
        if(root.nodeType==TreeNodeType.LEAF_NODE){
            return root.leafClassInfo;
        }else{
            ArrayList<DataItem>dataItems=new ArrayList<>();
            for(Integer id:root.ids){
                DataItem tmp=dataset.findTrainDataItemWithId(id);
                if(tmp!=null){
                    dataItems.add(tmp);
                }
            }
            return selectClassLabelWithVoting(dataItems);
        }

    }

    public static double pruningPredictAndComputeAccuracy(DecisionTree tree, ArrayList<DataItem> validatingData, Dataset dataset) {
        int num = 0;
        for (DataItem item : validatingData) {
            if (item.getLabel().equals(pruningPredict(tree, item, dataset))) {
                num++;
            }
        }
        return (double) num / (double) validatingData.size();
    }


    //后剪枝，完成
    //先把树建立好，然后，对这个树，进行剪枝
    public static void postPruning(DecisionTree tree,ArrayList<DataItem>validatingData,Dataset dataset){
      //需要将这颗树的非叶子节点逆序
        LinkedList<TreeNode>queue=new LinkedList<>();
        LinkedList<TreeNode>nodes=new LinkedList<>();
        TreeNode tempNode=tree.rootNode;
        queue.addLast(tempNode);
        while (queue.size()!=0){
            tempNode=queue.getFirst();queue.removeFirst();
            nodes.addFirst(tempNode);
            for(TreeNode treeNode:tempNode.children){
                if(treeNode.nodeType!=TreeNodeType.LEAF_NODE){
                    queue.addLast(treeNode);
                }
            }
        }
        while(nodes.size()!=0){
            TreeNode node=nodes.getFirst();nodes.removeFirst();
            //判断是否需要进行剪枝操作
            /**
             * 1.把当前节点变为叶结点，计算预测正确率
             * 2.判断是否需要剪枝操作
             */
            double originalAccuracy=pruningPredictAndComputeAccuracy(tree,validatingData,dataset);
            node.needDoPruning=true;
            double newAccuracy=pruningPredictAndComputeAccuracy(tree,validatingData,dataset);
            if(newAccuracy<=originalAccuracy){
             node.needDoPruning=false;
            }
            System.out.println("originalAccuracy:"+originalAccuracy+", newAccuracy:"+newAccuracy);

        }
    }

    // 需要检查
    public static int statisticsNumberOfPruningTreeNode(DecisionTree tree){
        int num=0;
        LinkedList<TreeNode>queue=new LinkedList<>();
        TreeNode tempNode=tree.rootNode;
        queue.addLast(tempNode);
        while (queue.size()!=0){
            tempNode=queue.getFirst();queue.removeFirst();
            if(tempNode.nodeType==TreeNodeType.LEAF_NODE || tempNode.needDoPruning==true){
                num++;
            }
            for(TreeNode treeNode:tempNode.children){
                    queue.addLast(treeNode);
            }
        }
        return num;
    }

}

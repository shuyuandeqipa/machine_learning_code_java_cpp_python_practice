import java.util.*;

public class ID3Solver {

    //这里还有一个bug ,由于Java的Map的key不予许重复的特性，所以每一个节点都要做到key不予许重复。
    // 如果使用嵌套的结构，就可以这个问题；或者把这个树的问题当作图来处理
    // 但是，要怎么样嵌套呢？

    DataSet dataSet;

    public ID3Solver(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    // 使用id3算法来建立决策树
    public void id3() {
        Map<String, TreeNode> tree = new HashMap<>();
        treeGenerate(dataSet.dataSet, tree, "", -1, null, 1);
        printRulesInDecisionTree(tree);
        System.out.println("print the tree according to the tree depth :");
        printRulesAccordingDepths(tree);
    }

    public void treeGenerate(ArrayList<DataItem> dataItems, Map<String, TreeNode> tree, String parentPointerInfo,
                             int parentFeatureIndex, TreeNode parent, int depth) {
//
//     if(dataItems.size()==0){//如果数据集为空，则直接退出
//         return;
//     }
        if (tree.size() == 0) {//根节点
            ArrayList<Integer> ids = new ArrayList<>();
            for (int i = 0; i < dataItems.size(); i++) {
                ids.add(dataItems.get(i).getId());
            }
            TreeNode treeNode = new TreeNode(false, "", ids, "", null);
            treeNode.setDepth(depth);
            double maxGain = 0.0;
            int maxGainFeatureIndex = -1;
            for (int i = 0; i < dataSet.allFeatures.size(); i++) {
                if ((new InformationGain(dataItems, i).getGain()) > maxGain) {
                    maxGain = new InformationGain(dataItems, i).getGain();
                    maxGainFeatureIndex = i;//选择出来了gain最大的属性
                }
            }
            dataSet.setAllFeaturesIsUsedWithIndex(maxGainFeatureIndex, true);
            treeNode.setNodeName(dataSet.getFeatureWithIndex(maxGainFeatureIndex));//设置节点的名字
            treeNode.setChildPointerInfo(dataSet.getFeatureDetailsInfoWithIndex(maxGainFeatureIndex));//设置指向子节点的指针的所有信息
            tree.put(treeNode.getParentPointerInfo() + ",depth=" + depth, treeNode);
            for (int i = 0; i < treeNode.childPointerInfo.size(); i++) {
                ArrayList<DataItem> subDataItems = DataSet.getDataItemsWithDataItemFeature(dataItems, maxGainFeatureIndex, treeNode.childPointerInfo.get(i));
//             //******************************
//             System.out.println(i+" ****************************");
//             for(int j=0;j<subDataItems.size();j++){
//                 System.out.println(subDataItems.get(j));
//             }
//             //***********************
                treeGenerate(subDataItems, tree, treeNode.childPointerInfo.get(i), maxGainFeatureIndex, treeNode, depth + 1);
            }

        } else {//不是根节点
            //数据子集为空，需要标记为他的父亲的节点中类别多的那种
            if (dataItems.size() == 0) {
                //统计父亲中样本最多的类
                ArrayList<Integer> ids = parent.nodeItems;
                ArrayList<DataItem> items = new ArrayList<>();
                String s = "";
                for (int id : ids) {
                    s += id + " ";
                    items.add(dataSet.dataSet.get(id - 1));

                }
                String label = DataSet.getLabelWithVoting(items);
                TreeNode treeNode = new TreeNode(true, parentPointerInfo, null, label, null);
                treeNode.setDepth(depth);
                tree.put(parent.getNodeName() + "->" + parentPointerInfo + ",depth=" + depth, treeNode);

                System.out.println(parentPointerInfo + " " + label + " ids=" + s);
                return;
            }

            //如果所有数据项的label都是相同的，那么这个是叶子节点，标记根节点的名字
            if (DataSet.allDataItemsFlagIsSame(dataItems)) {
                TreeNode treeNode = new TreeNode(true, parentPointerInfo, null, dataItems.get(0).getLabel(), null);
                treeNode.setDepth(depth);
                tree.put(parent.getNodeName() + "->" + parentPointerInfo + ",depth=" + depth, treeNode);
                return;
            }

            ArrayList<Integer> ids = new ArrayList<>();
            for (int i = 0; i < dataItems.size(); i++) {
                ids.add(dataItems.get(i).getId());
            }

            TreeNode treeNode = new TreeNode(false, parentPointerInfo, ids, "", null);
            treeNode.setDepth(depth);
            double maxGain = 0.0;
            int maxGainFeatureIndex = -1;
            for (int i = 0; i < dataSet.allFeatures.size(); i++) {
//             if(dataSet.getAllFeaturesIsUsedWithIndex(i)==false) {//如果这个属性没有被使用过
//                 if ((new InformationGain(dataItems, i).getGain()) > maxGain) {
//                     maxGain = new InformationGain(dataItems, i).getGain();
//                     maxGainFeatureIndex = i;//选择出来了gain最大的属性
//                 }
//             }
                //属性可以重复使用
                if ((new InformationGain(dataItems, i).getGain()) > maxGain) {
                    maxGain = new InformationGain(dataItems, i).getGain();
                    maxGainFeatureIndex = i;//选择出来了gain最大的属性
                }
            }
            if (maxGainFeatureIndex != -1) {
                dataSet.setAllFeaturesIsUsedWithIndex(maxGainFeatureIndex, true);//设置这个属性被使用过了
                //如果在这个属性上面所有数据项上的取值都相同，那么将这个节点标记为叶结点，类别用投票法标记
                Set<String> set = DataSet.getAllFeatureDetailInfoWithIndex(dataItems, maxGainFeatureIndex);
                if (set.size() == 1) {
                    treeNode.setLeafNode(true);
                    //投票法标记类别
                    treeNode.setNodeName(DataSet.getLabelWithVoting(dataItems));
                    tree.put(parent.getNodeName() + "->" + parentPointerInfo + ",depth=" + depth, treeNode);
                } else {
                    treeNode.setNodeName(dataSet.getFeatureWithIndex(maxGainFeatureIndex));//设置节点的名字
                    treeNode.setChildPointerInfo(DataSet.getFeatureDetailsInfoWithIndex(dataSet.dataSet, maxGainFeatureIndex));
                    tree.put(parent.getNodeName() + "->" + treeNode.getParentPointerInfo() + ",depth=" + depth, treeNode);
                    //处理后续的节点
                    for (int i = 0; i < treeNode.childPointerInfo.size(); i++) {
                        ArrayList<DataItem> subDataItems = DataSet.getDataItemsWithDataItemFeature(dataItems, maxGainFeatureIndex, treeNode.childPointerInfo.get(i));
                        //******************************
                        System.out.println(i + " ****************************");
                        for (int j = 0; j < subDataItems.size(); j++) {
                            System.out.println(subDataItems.get(j));
                        }
                        //***********************
                        treeGenerate(subDataItems, tree, treeNode.childPointerInfo.get(i), maxGainFeatureIndex, treeNode, depth + 1);
                    }

                }

            } else {//如果所有的属性都被使用完了
                return;
            }
        }

    }

    public void printRulesAccordingDepths(Map<String, TreeNode> tree) {
        System.out.println("Print the tree rules :");
        for (int depth = 1; depth <= 5; depth++) {
            Set<String> keyset = tree.keySet();
            for (String key : keyset) {
                TreeNode treeNode = tree.get(key);
                if (depth == treeNode.getDepth()) {
                    String s = "parentPointerInfo=" + key + ", ";
                    s += "isLeafNode=" + treeNode.isLeafNode + ", ";
                    s += "nodeName=" + treeNode.nodeName + ", { ";
                    if (treeNode.nodeItems != null) {
                        for (int id : treeNode.nodeItems) {
                            s += id + ",";
                        }
                    }
                    s += "}\n";
                    System.out.println(s);
                }
            }
        }
    }

    public void printRulesInDecisionTree(Map<String, TreeNode> tree) {
        System.out.println("printRulesInDecisionTree:");
        Set<String> keyset = tree.keySet();
        for (String key : keyset) {
            TreeNode treeNode = tree.get(key);
            String s = "parentPointerInfo=" + key + ", ";
            s += "isLeafNode=" + treeNode.isLeafNode + ", ";
            s += "nodeName=" + treeNode.nodeName + ", { ";
            if (treeNode.nodeItems != null) {
                for (int id : treeNode.nodeItems) {
                    s += id + ",";
                }
            }
            s += "}\n";
            System.out.println(s);
        }
    }

}

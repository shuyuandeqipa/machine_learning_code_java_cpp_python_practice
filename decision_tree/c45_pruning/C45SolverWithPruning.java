package c45_pruning;


import c45_pruning.ConfigsAndEnums.Config;
import c45_pruning.ConfigsAndEnums.DecisionAlgorithms;
import c45_pruning.ConfigsAndEnums.FeatureSelectCriterions;
import c45_pruning.ConfigsAndEnums.TreeNodeType;
import c45_pruning.math.DecisionTreeMathUtils;

import javax.xml.crypto.Data;
import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Now,we will build the decision tree using c4.5 algorithm,and use prepruning algorithm to optimise the tree.
 * Because the prepruning approach is a kind of greedy algorithm,so the decision tree may be under fitting.
 */
public class C45SolverWithPruning {
    Dataset dataset;// get the data for all the process

    public C45SolverWithPruning(Dataset dataset) {
        this.dataset = dataset;
    }

    // 需要好好考虑树的结构该如何设计
    public DecisionTree train(DecisionAlgorithms algorithm, boolean usePruning) {
        DecisionTree tree = new DecisionTree(algorithm, usePruning);
        TreeNode rootNode = new TreeNode();
        rootNode.nodeType = TreeNodeType.ROOT_NODE;
        ArrayList<Integer> ids = getIds(dataset.trainDataset);
        rootNode.setIds(ids);
        rootNode = generateTree(rootNode, DecisionAlgorithms.C45_WITH_PRUNING);
        tree.setRootNode(rootNode);
        return tree;
    }

    public TreeNode generateTree(TreeNode rootNode, DecisionAlgorithms algorithm) {

        if (algorithm == DecisionAlgorithms.C45_WITH_PRUNING) {
            rootNode = generateTreeC45Pruning(rootNode);
        } else if (algorithm == DecisionAlgorithms.CART) {
            rootNode = generateTreeCART(rootNode);
        }
        return rootNode;
    }

    public boolean allDataLabelIsSame(ArrayList<DataItem> dataItems) {
        if (dataItems.size() == 0) {
            throw new IllegalArgumentException("The dataItems is an empty array ! Please check you data !");
        }

        String firstLabel = dataItems.get(0).getLabel();
        if (dataItems.size() == 1) {
            return true;
        } else {
            for (int i = 1; i < dataItems.size(); i++) {
                if (firstLabel.equals(dataItems.get(i).getLabel()) == false) {
                    return false;
                }
            }
        }
        return true;
    }

    public TreeNode generateTreeC45Pruning(TreeNode rootNode) {
        System.out.println("start layer = "+rootNode.getLayer());
        if (rootNode.getIds()==null) {
            return null;
        }
        //1.select the best division feature according to the algorithm
        ArrayList<DataItem> dataItems = new ArrayList<>();
        for (Integer id : rootNode.ids) {
            dataItems.add(dataset.findTrainDataItemWithId(id));
        }
        /**
         * 这里还有一些递归退出的条件需要设置
         * 1.如果所有的数据属于同一个类别，那么将节点置为叶结点，设置类别标记
         * 2.如果所有的属性使用完了，就设置为空；但是，在目前这里的处理逻辑中，这一步可以不用做
         * 3.如果节点为空，那么就使用其父节点中的数据使用投票法来确定，这一步可以放到进入递归函数之前
         */
        boolean allDataItemsHaveSameClassLabel = allDataLabelIsSame(dataItems);
        if (allDataItemsHaveSameClassLabel == true) {//特殊处理
            rootNode.setNodeType(TreeNodeType.LEAF_NODE);
            rootNode.setNodeDivisionFeature(PruningUtils.selectClassLabelWithVoting(dataItems));
            rootNode.setLeafClassInfo(PruningUtils.selectClassLabelWithVoting(dataItems));
            return rootNode;
        }

        String bestFeature = selectTheBestFeatureForBuildTreeWithC45(dataItems, dataset.features);
        System.out.println("bestFeature= "+bestFeature);
        rootNode.setNodeDivisionFeature(bestFeature);
        // 判断是否需要进行剪枝的操作
        boolean whetherDoPruning = PruningUtils.needDoPrePruning(dataItems, dataset.validateDataset, dataset.features, FeatureSelectCriterions.GAIN_RATIO);
        whetherDoPruning=false;//先不使用剪枝技术
        if (whetherDoPruning == false) {//不需要剪枝，则继续按照树的逻辑向下进行
            //对每个子树进行递归操作
            /**
             * 1.根据当前的最佳属性找出所有的子树
             * 2.设置子树节点的所有需要的相关的信息
             * 3.递归操作
             */
            Set<String> attributes = Dataset.getAttributesForThisFeature(dataItems, bestFeature);
            System.out.println("attributes.size = "+attributes.size());
            for (String attr : attributes) {
                ArrayList<DataItem> child = Dataset.getDataItemsWithDataItemFeature(dataItems, bestFeature, attr);
                //如果子节点的数据为空
                if (child.size() == 0) {
                    String childClassLabel = PruningUtils.selectClassLabelWithVoting(dataItems);
                    TreeNode childNode = new TreeNode();
                    childNode.setPointerInfo(attr);
                    childNode.setNodeDivisionFeature(childClassLabel);
                    childNode.setLayer(rootNode.getLayer() + 1);
                    childNode.setLeafClassInfo(childClassLabel);
                    childNode.setNodeType(TreeNodeType.LEAF_NODE);
                    rootNode.addChild(childNode);//add a child to the rootNode
                } else {
                    ArrayList<Integer> childIds = new ArrayList<>();
                    for (DataItem item : child) {
                        childIds.add(item.getId());
                    }
                    TreeNode childNode = new TreeNode();
                    childNode.setPointerInfo(attr);
                    childNode.setLayer(rootNode.getLayer() + 1);
                    childNode.setIds(childIds);
                    System.out.println("child layer="+childNode.getLayer()+";childIds="+childIds);
                    childNode = generateTreeC45Pruning(childNode);
                    System.out.println("childNode="+childNode.toString());
                    rootNode.addChild(childNode);
                }
            }

        } else {
            // 把树标记为叶结点，并把classLabel写好
            rootNode.setNodeType(TreeNodeType.LEAF_NODE);
            rootNode.setNodeDivisionFeature(PruningUtils.selectClassLabelWithVoting(dataItems));
            rootNode.setLeafClassInfo(PruningUtils.selectClassLabelWithVoting(dataItems));
        }
        return rootNode;
    }
    public String selectTheBestFeatureForBuildTreeWithC45(ArrayList<DataItem> dataItems, ArrayList<String> features) {
        return DecisionTreeMathUtils.selectBestFeature(dataItems, features, FeatureSelectCriterions.GAIN_RATIO);
    }
    public TreeNode generateTreeCART(TreeNode rootNode) {

        return rootNode;
    }

    public ArrayList<Integer> getIds(ArrayList<DataItem> dataItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (DataItem dataItem : dataItems) {
            ids.add(dataItem.getId());
        }
        return ids;
    }
    //应该是正确的
    public String predict(DecisionTree tree,DataItem dataItem) {
        TreeNode root=tree.rootNode;
        while(root.nodeType!=TreeNodeType.LEAF_NODE){
            String feature=root.nodeDivisionFeature;
            String dataItemFeature=dataItem.dataItemFeatures.get(feature);
            for(TreeNode item:root.children) {
                if (dataItemFeature.equals(item.pointerInfo)) {
                    root = item;
                }
            }
        }
        return root.leafClassInfo;
    }

    public double predictAndComputeAccuracy(DecisionTree tree,ArrayList<DataItem>dataItems){
        int num=0;
        for(int i=0;i<dataItems.size();i++){
            if(predict(tree,dataItems.get(i)).equals(dataItems.get(i).getLabel())){
                num++;
            }
        }
        return (double)num/(double)dataItems.size();
    }

    public static void main(String[] args) {
        ReadDateFromFile readDateFromFile = new ReadDateFromFile(Config.trainDataPath,Config.validatingDataPath,Config.testDataPath);
        ArrayList<DataItem> trainDataItems = readDateFromFile.readTrainData();
        ArrayList<DataItem>validatingDataItems=readDateFromFile.readValidateData();
        ArrayList<String>features=readDateFromFile.readAllFeatures();
        System.out.println(trainDataItems);
        System.out.println(validatingDataItems);
        Dataset dataset=new Dataset(trainDataItems,validatingDataItems,validatingDataItems,features);
        C45SolverWithPruning c45=new C45SolverWithPruning(dataset);
        DecisionTree tree=c45.train(DecisionAlgorithms.C45_WITH_PRUNING,true);
        System.out.println(tree);
        TreeShow treeShow=new TreeShow(tree);
        treeShow.show();
        for(int i=0;i<dataset.validateDataset.size();i++){
            System.out.println(c45.predict(tree,dataset.validateDataset.get(i)));
        }
        System.out.println(c45.predictAndComputeAccuracy(tree,dataset.validateDataset));
        PruningUtils.postPruning(tree,validatingDataItems,dataset);
        System.out.println(PruningUtils.statisticsNumberOfPruningTreeNode(tree));
    }

}

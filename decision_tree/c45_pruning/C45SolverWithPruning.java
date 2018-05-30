package c45_pruning;


import c45_pruning.ConfigsAndEnums.DecisionAlgorithms;
import c45_pruning.ConfigsAndEnums.FeatureSelectCriterions;
import c45_pruning.ConfigsAndEnums.TreeNodeType;
import c45_pruning.math.DecisionTreeMathUtils;

import java.util.ArrayList;

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
    public DecisionTree train(DecisionAlgorithms algorithm,boolean usePruning){
        DecisionTree tree=new DecisionTree(algorithm,usePruning);
        TreeNode rootNode=new TreeNode();rootNode.nodeType= TreeNodeType.ROOT_NODE;
        ArrayList<Integer>ids=getIds(dataset.trainDataset);rootNode.setIds(ids);




        tree.setRootNode(rootNode);
        return  tree;
    }
    public TreeNode generateTree(TreeNode rootNode,DecisionAlgorithms algorithm){

        if(algorithm==DecisionAlgorithms.C45_WITH_PRUNING){
            rootNode=generateTreeC45Pruning(rootNode);
        }else if(algorithm==DecisionAlgorithms.CART){
            rootNode=generateTreeCART(rootNode);
        }
        return rootNode;
    }

    public TreeNode generateTreeC45Pruning(TreeNode rootNode) {
        //1.select the best division feature according to the algorithm
        ArrayList<DataItem>dataItems=new ArrayList<>();
        for(Integer id:rootNode.ids){
            dataItems.add(dataset.trainDataset.get(id));
        }
        String bestFeature=selectTheBestFeatureForBuildTreeWithC45(dataItems,dataset.features);
        // 判断是否需要进行剪枝的操作



        return rootNode;
    }
    public String selectTheBestFeatureForBuildTreeWithC45(ArrayList<DataItem>dataItems,ArrayList<String>features){
        return DecisionTreeMathUtils.selectBestFeature(dataItems,features, FeatureSelectCriterions.GAIN_RATIO);
    }

    public TreeNode generateTreeCART(TreeNode rootNode) {

        return rootNode;
    }

    public ArrayList<Integer> getIds(ArrayList<DataItem> dataItems) {
        ArrayList<Integer>ids=new ArrayList<>();
        for(DataItem dataItem:dataItems){
           ids.add(dataItem.getId());
        }
        return ids;
    }


    public String predict(DataItem dataItem){
        String classLabel="";

        return classLabel;
    }


}

package c45_pruning;

import c45_pruning.ConfigsAndEnums.TreeNodeType;

import java.util.ArrayList;

public class TreeNode {
    String pointerInfo;// the info on the pointer
    int layer;
    TreeNodeType nodeType;
    ArrayList<Integer>ids;// all the id of dataItems in this node
    String nodeDivisionFeature;// the feature to divide the dataItems
    ArrayList<TreeNode>children;// all the child of the node
    boolean needDoPruning;// whether the node is pruned,default is false
    String leafClassInfo;// if the node is a leaf node ,we will mark the class for the dataItem

    public TreeNode() {
        ids=null;
        nodeDivisionFeature="";
        this.pointerInfo = null;
        this.layer=1;
        this.children=new ArrayList<>();
        this.needDoPruning=false;
        this.nodeType=TreeNodeType.INNER_NODE;
        this.leafClassInfo=null;
    }
    public void addChild(TreeNode child){
        this.children.add(child);
    }

    //*********************************************************************
    public String getPointerInfo() {
        return pointerInfo;
    }

    public void setPointerInfo(String pointerInfo) {
        this.pointerInfo = pointerInfo;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public TreeNodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(TreeNodeType nodeType) {
        this.nodeType = nodeType;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public String getNodeDivisionFeature() {
        return nodeDivisionFeature;
    }

    public void setNodeDivisionFeature(String nodeDivisionFeature) {
        this.nodeDivisionFeature = nodeDivisionFeature;
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TreeNode> children) {
        this.children = children;
    }

    public boolean isNeedDoPruning() {
        return needDoPruning;
    }

    public void setNeedDoPruning(boolean needDoPruning) {
        this.needDoPruning = needDoPruning;
    }

    public String getLeafClassInfo() {
        return leafClassInfo;
    }

    public void setLeafClassInfo(String leafClassInfo) {
        this.leafClassInfo = leafClassInfo;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "pointerInfo='" + pointerInfo + '\'' +
                ", layer=" + layer +
                ", nodeType=" + nodeType +
                ", ids=" + ids +
                ", nodeDivisionFeature='" + nodeDivisionFeature + '\'' +
                ", children=" + children +
                ", needDoPruning=" + needDoPruning +
                ", leafClassInfo='" + leafClassInfo + '\'' +
                '}';
    }
}

import java.util.ArrayList;

public class TreeNode {
    boolean isLeafNode;//if the node is a leaf node
    String parentPointerInfo;//指向这个节点的指针的附加信息
    ArrayList<Integer> nodeItems;//just store the ids for needed dataItems
    String nodeName;// name of a node
    ArrayList<String>childPointerInfo;//指向孩子的指针的附加信息的集合
    int depth;//这个节点在树中的深度
    public TreeNode(boolean isLeafNode, String parentPointerInfo, ArrayList<Integer> nodeItems, String nodeName, ArrayList<String> childPointerInfo) {
        this.isLeafNode = isLeafNode;
        this.parentPointerInfo = parentPointerInfo;
        this.nodeItems = nodeItems;
        this.nodeName = nodeName;
        this.childPointerInfo = childPointerInfo;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isLeafNode() {
        return isLeafNode;
    }

    public void setLeafNode(boolean leafNode) {
        isLeafNode = leafNode;
    }

    public String getParentPointerInfo() {
        return parentPointerInfo;
    }

    public void setParentPointerInfo(String parentPointerInfo) {
        this.parentPointerInfo = parentPointerInfo;
    }

    public ArrayList<Integer> getNodeItems() {
        return nodeItems;
    }

    public void setNodeItems(ArrayList<Integer> nodeItems) {
        this.nodeItems = nodeItems;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public ArrayList<String> getChildPointerInfo() {
        return childPointerInfo;
    }

    public void setChildPointerInfo(ArrayList<String> childPointerInfo) {
        this.childPointerInfo = childPointerInfo;
    }
}

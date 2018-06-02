package c45_pruning;

import c45_pruning.ConfigsAndEnums.TreeNodeType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

//Output the decision tree in a needed format!
public class TreeShow {
    DecisionTree decisionTree;

    public TreeShow(DecisionTree decisionTree) {
        this.decisionTree = decisionTree;
    }

    public void show() {
        String s = "";
        LinkedList<TreeNode> queue = new LinkedList<>();
        TreeNode rootNode = decisionTree.rootNode;
        s += rootNode.nodeDivisionFeature + "\n";
        queue.addLast(rootNode);
        while (queue.size() != 0) {
            TreeNode node = queue.getFirst();
            queue.removeFirst();
            ArrayList<TreeNode> childs = node.children;

            if (childs != null && childs.size() != 0) {
                for (TreeNode child : childs) {
                    if (child.nodeType == TreeNodeType.LEAF_NODE) {
                        s += "(LEAF_NODE:ids=" + child.ids + " pointerInfo: " + child.pointerInfo + ",feature" + child.nodeDivisionFeature + ")    ";
                    } else {
                        s += "ids:" + child.ids + "pointerInfo: " + child.pointerInfo + ",feature" + child.nodeDivisionFeature + "   ";
                    }

                    queue.addLast(child);
                }
                s += "\n";
            }
        }
        System.out.println(s);

    }


}

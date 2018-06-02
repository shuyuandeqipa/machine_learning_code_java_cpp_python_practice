package c45_pruning;

import c45_pruning.ConfigsAndEnums.DecisionAlgorithms;

public class DecisionTree {
    TreeNode rootNode;
    DecisionAlgorithms algorithm;
    boolean hasUsedPruning;
    double trainingAccuracy;
    double validatingAccuracy;
    double testingAccuracy;

    public DecisionTree( DecisionAlgorithms algorithm,boolean hasUsedPruning) {
        this.algorithm = algorithm;
        this.hasUsedPruning = hasUsedPruning;
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public DecisionAlgorithms getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(DecisionAlgorithms algorithm) {
        this.algorithm = algorithm;
    }

    public boolean isHasUsedPruning() {
        return hasUsedPruning;
    }

    public void setHasUsedPruning(boolean hasUsedPruning) {
        this.hasUsedPruning = hasUsedPruning;
    }

    public double getTrainingAccuracy() {
        return trainingAccuracy;
    }

    public void setTrainingAccuracy(double trainingAccuracy) {
        this.trainingAccuracy = trainingAccuracy;
    }

    public double getValidatingAccuracy() {
        return validatingAccuracy;
    }

    public void setValidatingAccuracy(double validatingAccuracy) {
        this.validatingAccuracy = validatingAccuracy;
    }

    public double getTestingAccuracy() {
        return testingAccuracy;
    }

    public void setTestingAccuracy(double testingAccuracy) {
        this.testingAccuracy = testingAccuracy;
    }

    @Override
    public String toString() {
        return "DecisionTree{" +
                "rootNode=" + rootNode +
                ", algorithm=" + algorithm +
                ", hasUsedPruning=" + hasUsedPruning +
                ", trainingAccuracy=" + trainingAccuracy +
                ", validatingAccuracy=" + validatingAccuracy +
                ", testingAccuracy=" + testingAccuracy +
                '}';
    }
}

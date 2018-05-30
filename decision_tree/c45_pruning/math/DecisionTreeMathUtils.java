package c45_pruning.math;

import c45_pruning.ConfigsAndEnums.FeatureSelectCriterions;
import c45_pruning.DataItem;
import c45_pruning.Dataset;
import c45_pruning.ReadDateFromFile;
import com.sun.org.apache.bcel.internal.generic.FieldGen;


import java.util.*;

//All the code in this class in for build the decision tree.
//We can use the math function there to build id3, c4.5 and CART tree.
public class DecisionTreeMathUtils {

    public static String selectBestFeature(ArrayList<DataItem> dataItems, ArrayList<String> features, FeatureSelectCriterions criterion) {
        if(dataItems.size()<=0 || features.size()<=0)return "";

        if (criterion == FeatureSelectCriterions.GAIN_RATIO) {
            // 使用启发式的方法，先从划分属性中选择新星增益高于平均水平的属性，再从中选择增益率最高的
            ArrayList<Double> informationGains = new ArrayList<>();
            for (String feature : features) {
                InformationGain gain = new InformationGain(dataItems, feature);
                informationGains.add(gain.getGain());
            }
            // compute the mean of all the gains
            double meanGains = MathUtils.mean(informationGains);
            ArrayList<String> selectedFeatures = new ArrayList<>();
            for (int i = 0; i < informationGains.size(); i++) {
                if (informationGains.get(i) >= meanGains) {
                    selectedFeatures.add(features.get(i));
                }
            }
            // select the feature with the highest gain ratio
            ArrayList<Double> gainRatios = new ArrayList<>();
            for (String feature : selectedFeatures) {
                GainRatio gainRatio = new GainRatio(dataItems, feature);
                gainRatios.add(gainRatio.getGainRatio());
            }

            double maxRatio = -Double.MAX_VALUE;
            String maxFeature = "";
            for (int i = 0; i < gainRatios.size(); i++) {
                if (gainRatios.get(i) > maxRatio) {
                    maxRatio = gainRatios.get(i);
                    maxFeature = selectedFeatures.get(i);
                }
            }
            return maxFeature;

        }
        return "";
    }

    static class InformationEntropy {
        ArrayList<DataItem> dataItems;
        double entropy;

        public double getEntropy() {
            return entropy;
        }


        public InformationEntropy(ArrayList<DataItem> dataItems) {
            this.dataItems = dataItems;
            this.entropy = computeInformationEntropy(this.dataItems);
        }


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
                String label = item.getLabel();
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

        // compute the entropy
        private static double compute(double pk) {
            if (pk == 0) {
                return 0.0;
            } else {
                return pk * Math.log10(pk) / Math.log10(2.0);
            }
        }
    }


    static class InformationGain {

        ArrayList<DataItem> dataItems;
        //split the data according to the feature
        String feature;
        double gain;//the information gain

        public double getGain() {
            return gain;
        }

        public InformationGain(ArrayList<DataItem> dataItems, String feature) {
            this.dataItems = dataItems;

            this.feature = feature;
            this.gain = computeInformationGain(this.dataItems, this.feature);
        }

        private double computeInformationGain(ArrayList<DataItem> dataItems, String featureKey) {
            double gain = 0.0;

            double dataItemsEntropy = new InformationEntropy(dataItems).getEntropy();
            gain = dataItemsEntropy;
            // get all the classes in this feature
            Set<String> allDataItemFeatureOfThisFeature = new HashSet<>();
            for (DataItem dataItem : dataItems) {
                allDataItemFeatureOfThisFeature.add(dataItem.getDataItemFeatureWithFeature(featureKey));
            }

            for (String featureClasses : allDataItemFeatureOfThisFeature) {
                ArrayList<DataItem> tempDataItems = Dataset.getDataItemsWithDataItemFeature(dataItems, featureKey, featureClasses);
                gain = gain - (double) tempDataItems.size() / (double) dataItems.size() * (new InformationEntropy(tempDataItems).getEntropy());
            }

            return gain;
        }
    }

    // the code here is used to test weather the InformationGain is right;
    public static void main(String[] args) {
        String xigua = "E:\\machineLearningJavaCpp\\machine_learning_code_java_cpp_python_practice\\machine_learning_java\\src\\id4_pruning\\data\\xigua.txt";
        ReadDateFromFile readDateFromFile = new ReadDateFromFile();
        ArrayList<DataItem> dataItems = readDateFromFile.readData(xigua);
        System.out.println(dataItems.get(0).outputFeatureKeys());
        System.out.println(dataItems);
        InformationGain informationGain = new InformationGain(dataItems, "触感");
        System.out.println(informationGain.getGain());


    }
    // the gain ratio is used to build the decision tree according to the c4.5 algorithm

    /**
     * Gain_ratio(D,a)=Gain(D,a)/IV(a)
     * IV(a)=-sum(|D^v|/|D| log2(|D^v|/|D|))
     * <p>
     * page (78) at <<Machine learning >> author:Zhou ZhiHua, China
     */
    static class GainRatio {
        ArrayList<DataItem> dataItems;
        String featureKey;// compute the gain ratio according to the feature
        double gainRatio;

        public GainRatio(ArrayList<DataItem> dataItems, String featureKey) {
            this.dataItems = dataItems;
            this.featureKey = featureKey;
            gainRatio = computeGainRatio(this.dataItems, featureKey);
        }

        public double getGainRatio() {
            return gainRatio;
        }

        static public double computeGainRatio(ArrayList<DataItem> dataItems, String featureKey) {
            double gainRatio = 0.0;
            InformationGain informationGain = new InformationGain(dataItems, featureKey);
            double gain = informationGain.getGain();
            //split the dataItems according to the featureKeys
            //get the featurekeys
            Set<String> featureKeys = new HashSet<>();
            for (DataItem item : dataItems) {
                featureKeys.add(item.getDataItemFeatureWithFeature(featureKey));
            }
            //split the dataItems
            double IV = 0.0;
            for (String feature : featureKeys) {
                ArrayList<DataItem> tempDataItems = Dataset.getDataItemsWithDataItemFeature(dataItems, featureKey, feature);
                double ratio = (double) tempDataItems.size() / (double) dataItems.size();
                IV -= ratio * Math.log10(ratio) / Math.log10(2);
            }
//            System.out.println("Gain="+gain);
//            System.out.println("IV="+IV);
            gainRatio = gain / IV;
            return gainRatio;
        }

        // just test the GainRatio algorithm
        public static void main(String[] args) {
            String xigua = "E:\\machineLearningJavaCpp\\machine_learning_code_java_cpp_python_practice\\machine_learning_java\\src\\c45_pruning\\data\\xigua.txt";
            ReadDateFromFile readDateFromFile = new ReadDateFromFile();
            ArrayList<DataItem> dataItems = readDateFromFile.readData(xigua);
            System.out.println(dataItems);
            GainRatio gainRatio = new GainRatio(dataItems, "色泽");
            System.out.println(gainRatio.getGainRatio());
        }
    }


    // The giniIndex is used in CART algorithm
    // CART : classification and regression tree
    // The code here is just to do the math work
    static class GiniIndex {

        ArrayList<DataItem> dataItems;
        String featureKey;
        double giniIndex = 0.0;

        public GiniIndex(ArrayList<DataItem> dataItems, String featureKey) {
            this.dataItems = dataItems;
            this.featureKey = featureKey;
            giniIndex = computeGiniIndex(dataItems, featureKey);
        }


        /**
         * Gini(D)=1-sum((p_k)^2) and (k=1:|y|)
         * Gini_index(D,a)=sum(|D^v|/|D|)Gini(D^v)
         * <p>
         * page (79) at <<Machine learning >> author:Zhou ZhiHua, China
         */
        public double computeGini(ArrayList<DataItem> dataItems) {
            ArrayList<Double> p_ks = InformationEntropy.computePks(dataItems);
            double gini = 0.0;
            double sum = 0.0;
            for (int i = 0; i < p_ks.size(); i++) {
                sum += p_ks.get(i) * p_ks.get(i);
            }
            gini = 1.0 - sum;
            return gini;
        }

        public double computeGiniIndex(ArrayList<DataItem> dataItems, String featureKey) {
            //First : we need to compute the gini from the original data (dataItems).
            ArrayList<Double> p_ks = InformationEntropy.computePks(dataItems);
            double gini = computeGini(dataItems);

            //Second :we can compute the giniIndex
            double giniIndex = 0.0;
            Set<String> featureKeys = new HashSet<>();
            for (DataItem item : dataItems) {
                featureKeys.add(item.getDataItemFeatureWithFeature(featureKey));
            }
            for (String feature : featureKeys) {
                ArrayList<DataItem> tempDataItems = Dataset.getDataItemsWithDataItemFeature(dataItems, featureKey, feature);
                double ratio = (double) tempDataItems.size() / (double) dataItems.size();
                giniIndex += ratio * computeGini(tempDataItems);
            }
            return giniIndex;
        }
    }


}

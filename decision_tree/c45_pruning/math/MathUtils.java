package c45_pruning.math;

import java.util.ArrayList;

public class MathUtils {
    public static double mean(ArrayList<Double>array){
        double sum=0.0;
        for(double data:array){
            sum+=data;
        }
        return sum/(double) array.size();
    }
}

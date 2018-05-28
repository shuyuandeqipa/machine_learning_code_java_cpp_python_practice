package nd4j;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
public class LinearRegression {
    private double learningrate = 0.002d;//至关重要
    private double k;
    private double b;

    public LinearRegression(double k, double b, double learningrate){
        this.k = k;
        this.b = b;
        this.learningrate = learningrate;
    }

    public LinearRegression(double k, double b){
        this.k = k;
        this.b = b;
    }

    public double fitBGD(INDArray trainData, INDArray labelData,int mat_iters){
        INDArray diff=null;
        for(int iter=0;iter<mat_iters;iter++){
            diff = labelData.sub(trainData.mul(k).add(b));
            //每次使用的都是整个数据集上面的误差和
            k = diff.dup().muli(trainData).sumNumber().doubleValue() / trainData.length() * 2.0 * learningrate + k;
            b = diff.sumNumber().doubleValue() / trainData.length() * 2.0 * learningrate + b;
        }
        return Nd4j.sum(diff.muli(diff)).div(trainData.length()).getDouble(0);//返回平均误差
    }

    public double fitSGD(INDArray trainData, INDArray labelData,int max_iters){
        double diff = 0.0;
        for(int i=0;i<max_iters;i++){
            for( int index = 0; index < trainData.length(); ++index ){
                double label = labelData.getDouble(index);
                double data = trainData.getDouble(index);
                diff = label - (k * data + b);// (y-y_hat)^2=(y-kx-b)^2;
                k = k + 2 * diff * data * learningrate;// k=k-(learning_rate*(-2)*x*(y-(kx+b)));
                b = b + 2 * diff * learningrate;//b=b-(learning_rate*(-2)*(y-kx-b))
//                System.out.println("data="+data+",label="+label+",diff="+diff+",k="+k+",b="+b);
            }
        }

        return diff * diff;//最终返回的是误差的平方
    }

    public double getK(){
        return k;
    }

    public double getB(){
        return b;
    }
    public static void main(String[] args){
        LinearRegression model = new LinearRegression(-3.0 , 1.0);
//        double k_label = 125.6;
//        double b_label = 10.3;
//        INDArray data = Nd4j.getRandom().nextDouble(new int[]{1,1000});
//        INDArray label = data.mul(k_label).add(b_label);
        INDArray data=Nd4j.create(new double[]{ 6.1101, 5.5277, 8.5186, 7.0032, 5.8598, 8.3829, 7.4764, 8.5781, 6.4862, 5.0546, 5.7107, 14.1640, 5.7340, 8.4084, 5.6407, 5.3794, 6.3654, 5.1301, 6.4296, 7.0708, 6.1891, 20.2700, 5.4901, 6.3261, 5.5649, 18.9450, 12.8280, 10.9570, 13.1760, 22.2030, 5.2524, 6.5894, 9.2482, 5.8918, 8.2111, 7.9334, 8.0959, 5.6063, 12.8360, 6.3534, 5.4069, 6.8825, 11.7080, 5.7737, 7.8247, 7.0931, 5.0702, 5.8014, 11.7000, 5.5416, 7.5402, 5.3077, 7.4239, 7.6031, 6.3328, 6.3589, 6.2742, 5.6397, 9.3102, 9.4536, 8.8254, 5.1793, 21.2790, 14.9080, 18.9590, 7.2182, 8.2951, 10.2360, 5.4994, 20.3410, 10.1360, 7.3345, 6.0062, 7.2259, 5.0269, 6.5479, 7.5386, 5.0365, 10.2740, 5.1077, 5.7292, 5.1884, 6.3557, 9.7687, 6.5159, 8.5172, 9.1802, 6.0020, 5.5204, 5.0594, 5.7077, 7.6366, 5.8707, 5.3054, 8.2934, 13.3940, 5.4369});
        INDArray label = Nd4j.create(new double[]{17.5920, 9.1302, 13.6620, 11.8540, 6.8233, 11.8860, 4.3483, 12.0000, 6.5987, 3.8166, 3.2522, 15.5050, 3.1551, 7.2258, 0.7162, 3.5129, 5.3048, 0.5608, 3.6518, 5.3893, 3.1386, 21.7670, 4.2630, 5.1875, 3.0825, 22.6380, 13.5010, 7.0467, 14.6920, 24.1470, -1.2200, 5.9966, 12.1340, 1.8495, 6.5426, 4.5623, 4.1164, 3.3928, 10.1170, 5.4974, 0.5566, 3.9115, 5.3854, 2.4406, 6.7318, 1.0463, 5.1337, 1.8440, 8.0043, 1.0179, 6.7504, 1.8396, 4.2885, 4.9981, 1.4233, -1.4211, 2.4756, 4.6042, 3.9624, 5.4141, 5.1694, -0.7428, 17.9290, 12.0540, 17.0540, 4.8852, 5.7442, 7.7754, 1.0173, 20.9920, 6.6799, 4.0259, 1.2784, 3.3411, -2.6807, 0.2968, 3.8845, 5.7014, 6.7526, 2.0576, 0.4795, 0.2042, 0.6786, 7.5435, 5.3436, 4.2415, 6.7981, 0.9270, 0.1520, 2.8214, 1.8451, 4.2959, 7.2029, 1.9869, 0.1445, 9.0551, 0.6170}) ; // sum(1) 是每一行加起来

//        System.out.println(label);
        final int iterations = 20000;
        double loss = model.fitBGD(data, label, iterations);
        System.out.println(loss);

        System.out.println("k: " + model.getK());
        System.out.println("b: " + model.getB());

//        for(int i=0;i<10;i++){
//            System.out.println(Nd4j.getRandom().nextGaussian());//标准正态分布
//        }
    }

}
/**
 反思：1.BGD和SGD的结果在这里有很大的差距
 2.合适的学习率设置很重要
 3.迭代的次数很重要
 */
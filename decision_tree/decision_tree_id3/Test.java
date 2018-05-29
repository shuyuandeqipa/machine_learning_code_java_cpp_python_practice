import java.util.ArrayList;

public class Test {
    boolean isused[];

    public Test() {
        this.isused = new boolean[10];
    }
    public static void main(String[]args){
        Test test=new Test();
        for(int i=0;i<test.isused.length;i++){
            System.out.println(test.isused[i]);
        }
    }

//    public static void test(ArrayList<Integer> arrayList){
//        arrayList.add(100);
//    }
//    public static void main(String[]args){
//        ArrayList<Integer>arrayList=new ArrayList<>();
//        arrayList.add(1);
//        arrayList.add(3);
//        test(arrayList);
//        for(int i=0;i<arrayList.size();i++){
//            System.out.println(arrayList.get(i));
//        }
//    }
}

package testsForAuditions;

/**
 * Created by navot.dako on 5/21/2017.
 */
public class test3 {
    public static void main(String[] args) {
        System.out.println(fun(100, 4));
    }
    public static String fun(int a, int b){
        int c = 0;
        int d = 0;
        while (a > 0){
            c += (a % b) * ((int)Math.pow(10, d));
            a = a / b;
            d ++;
        }
        return String.valueOf(c);
    }
}

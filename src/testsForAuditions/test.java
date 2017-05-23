package testsForAuditions;

/**
 * Created by navot.dako on 4/30/2017.
 */
public class test {
    public static void main(String[] args) {
        System.out.println("Here You Go - " + doIt(15, 6, 6));
    }

    private static double doIt(double x, double n, int i) {
        double a = 1;
        double b = x;
        double c = -1;
        double d = getDouble(i);
        while ((b - a) > d) {
            c = (a + b) / 2;
            if (Math.pow(c, n) == x) {
                return c;
            }
            if (Math.pow(c, n) > x) {
                b = c;
            } else if (Math.pow(c, n) < x) {
                a = c;
            }
        }
        double e = Math.round(1 / d);
        double f = (double) ((int) (c * e)) / (e);
        return f;
    }

    private static double getDouble(int i) {
        double r = 1;
        while (i > 0) {
            r /= 10;
            i--;
        }
        return r;
    }
}

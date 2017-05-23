package testsForAuditions;

import java.util.Arrays;

public class test2 {
    public static void main(String[] args) {
        int[] arr = new int[]{10,30,20,30,900,600,30,40,50,55,45};
        System.out.println("Here You Go - " + Arrays.toString(doIt(arr)));
    }

    static int[] doIt(int[] arr) {
        int a = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > a)
                a = arr[i];
        }
        int[] arr2 = new int[a + 1];
        for (int i = 0; i < arr.length; i++) {
            arr2[arr[i]]++;
        }
        int b = 0;
        for (int i = a; i >= 0; i--) {
            for (int j = 0; j < arr2[i]; j++) {
                arr[b] = i;
                b++;
            }
        }
        return arr;
    }

}

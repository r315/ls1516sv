package pt.isel.ls;

public class Ints {

    public static int max(int a, int b) {
        return a >= b ? a : b;
    }

    public static void insertionSort(int[] v, int l, int r) {
        if (v.length == 0 || l >= r) {
            return;
        }
        if (l<0) {
            throw new IllegalArgumentException("start index cannot be negative");
        }
        if (r>=v.length) {
            throw new IllegalArgumentException("finish index cannot be greater than the length of the array");
        }
        for (int i = l+1; i < r; ++i) {
            int key = v[i];
            int j = i - 1;
            for (; j > 0 && v[j] > key; --j)
                v[j + 1] = v[j];
            v[j + 1] = key;
        }
    }
}

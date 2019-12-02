import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class MergeSort {

    private static final Random RNG    = new Random(10982755L);
    private static final int    LENGTH = 8192000;

    public static void main(String... args) {
        int[] arr = randomIntArray();
        long start = System.currentTimeMillis();
        concurrentMergeSort(arr);
        long end = System.currentTimeMillis();
        if (!sorted(arr)) {
            System.err.println("The final array is not sorted");
            System.exit(0);
        }
        System.out.printf("%10d numbers: %6d ms%n", LENGTH, end - start);
    }

    public static void concurrentMergeSort(int[] arr) {
        // int threads = Runtime.getRuntime().availableProcessors();
        int threads = 4;
        concurrentMergeSort(arr, threads);
    }

    public static void concurrentMergeSort(int[] arr, int threadCount) {
        if (threadCount <= 1) {
            mergeSort(arr);
        } else if (arr.length >= 2) {
            // split array in half
            int[] left  = Arrays.copyOfRange(arr, 0, arr.length / 2);
            int[] right = Arrays.copyOfRange(arr, arr.length / 2, arr.length);

            // sort the halves
            // mergeSort(left);
            // mergeSort(right);
            Thread firstThread = new Thread(new Sorting(left,  threadCount / 2));
            Thread secondThread = new Thread(new Sorting(right, threadCount / 2));
            firstThread.start();
            secondThread.start();

            //
            try {
                firstThread.join();
                secondThread.join();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ie);
            }

            // merge them back together
            merge(left, right, arr);
        }
    }

    public static void mergeSort(int[] arr) {
        if (arr.length >= 2) {
            // split array in half
            int[] left  = Arrays.copyOfRange(arr, 0, arr.length / 2);
            int[] right = Arrays.copyOfRange(arr, arr.length / 2, arr.length);

            // sort the halves
            mergeSort(left);
            mergeSort(right);

            // merge them back together
            merge(left, right, arr);
        }
    }

    public static void merge(int[] left, int[] right, int[] arr) {
        int i1 = 0;
        int i2 = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i2 >= right.length || (i1 < left.length && left[i1] < right[i2])) {
                arr[i] = left[i1];
                i1++;
            } else {
                arr[i] = right[i2];
                i2++;
            }
        }
    }

    private static int[] randomIntArray() {
        int[] arr = new int[LENGTH];
        for (int i = 0; i < arr.length; i++)
            arr[i] = RNG.nextInt(LENGTH * 10);
        return arr;
    }

    public static boolean sorted(int[] arr) {
        if (arr == null){
            return false;
        }
        return !IntStream.range(1, arr.length)
                .mapToObj(i -> arr[i - 1] > arr[i])
                .findFirst().orElse(false);
    }
}
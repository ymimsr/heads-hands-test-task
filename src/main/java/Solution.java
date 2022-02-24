import java.util.*;

public class Solution {

    /**
     * Defines random generated arrays' sizes
     */
    private final int MIN_SIZE;
    private final int MAX_SIZE;

    /**
     * Defines range of random generated double values
     */
    private final double MIN_VALUE;
    private final double MAX_VALUE;

    /**
     * <p>
     *     Initializes configuration for generating random values
     * </p>
     * <p>
     *     Sets provided MIN_SIZE, MAX_SIZE, MIN_VALUE, MAX_VALUE to the class fields.
     *     If values for those fields are not provided, assigns default values.
     * </p>
     * <p>
     *     If provided MIN_SIZE is negative, IllegalArgumentException will be thrown.
     * </p>
     * @param MIN_SIZE default value: 1
     * @param MAX_SIZE default value: 100
     * @param MIN_VALUE default value: Double.MIN_VALUE
     * @param MAX_VALUE default value: Double.MAX_VALUE
     */
    public Solution(int MIN_SIZE, int MAX_SIZE, double MIN_VALUE, double MAX_VALUE) {
        if (MIN_SIZE < 0)
            throw new IllegalArgumentException("MIN_SIZE cannot be negative");

        this.MIN_SIZE = MIN_SIZE;
        this.MAX_SIZE = MAX_SIZE;
        this.MIN_VALUE = MIN_VALUE;
        this.MAX_VALUE = MAX_VALUE;
    }

    public Solution(int MIN_SIZE, int MAX_SIZE) {
        this(MIN_SIZE, MAX_SIZE, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public Solution(double MIN_VALUE, double MAX_VALUE) {
        this(1, 100, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public Solution() {
        this(1, 100, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    private static final Random random = new Random();

    /**
     * <p>
     *     Generates random double that is in the range.
     * </p>
     * @return random generated double value
     */
    private double generateDouble() {
        double randomDouble = random.nextDouble();
        // half min and half max used in order to prevent overflow
        double halfMin = MIN_VALUE / 2.0;
        double halfMax = MAX_VALUE / 2.0;

        randomDouble = randomDouble * (halfMax - halfMin) + halfMin;
        randomDouble *= 2;

        return randomDouble;
    }

    /**
     * <p>
     *     Generates array of random sized array filled with random generated double values.
     * </p>
     * <p>
     *     If array.length > MAX_SIZE - MIN_SIZE + 1, i.e. it's impossible to generate not recurring
     *     random sizes, IllegalArgumentException will be thrown.
     * </p>
     * @param array array of random generated arrays
     */
    private void init(double[][] array) {
        if (array.length > MAX_SIZE - MIN_SIZE + 1)
            throw new IllegalArgumentException(
                    "MAX_SIZE - MIN_SIZE + 1 < n\n" +
                            "no size duplicates requirement cannot be maintained."
            );

        Set<Integer> sizeHashSet = new HashSet<>(MAX_SIZE - MIN_SIZE + 1);

        for (int i = 0; i < array.length; i++) {
            int randSize = random.nextInt(MAX_SIZE - MIN_SIZE + 1) + MIN_SIZE;
            if (sizeHashSet.contains(randSize)) {
                i--;
                continue;
            }
            sizeHashSet.add(randSize);

            array[i] = new double[randSize];
            for (int k = 0; k < array[i].length; k++) {
                array[i][k] = generateDouble();
            }
        }
    }

    /**
     * <p>
     *     Swaps two elements of array.
     * </p>
     * @param array array to swap elements in
     * @param a index of the first element
     * @param b index of the second element
     */
    private void swap(double[] array, int a, int b) {
        double swapTemp = array[a];
        array[a] = array[b];
        array[b] = swapTemp;
    }

    /**
     * <p>
     *     Partition for quicksort algorithm. Finds the partition point for provided array.
     * </p>
     * @param array array to find partition point for
     * @param begin inclusive start index
     * @param end inclusive end index
     * @param doubleComparator custom comparator
     * @return index of partition point
     */
    private int partition(double[] array, int begin, int end, Comparator<Double> doubleComparator) {
        double pivot = array[end];

        int l = begin, r = end - 1;
        while (l <= r) {
            if (doubleComparator.compare(array[l], pivot) > 0
                    && doubleComparator.compare(array[r], pivot) < 0) {
                swap(array, l, r);
                l++;
                r--;
            } else if (doubleComparator.compare(array[l], pivot) > 0) {
                r--;
            } else if (doubleComparator.compare(array[r], pivot) < 0) {
                l++;
            } else {
                r--;
                l++;
            }
        }

        swap(array, l, end);
        return l;
    }

    /**
     * <p>
     *     Sorts an array. Implements quicksort algorithm.
     * </p>
     * <p>
     *     Uses custom comparator in order to sort in both descending and ascending order.
     * </p>
     * @param array array to sort
     * @param begin inclusive index to start sort from
     * @param end inclusive index to stop sort from
     * @param doubleComparator custom comparator
     */
    private void quickSort(double[] array, int begin, int end, Comparator<Double> doubleComparator) {
        if (begin < end) {
            int partitionIndex = partition(array, begin, end, doubleComparator);

            quickSort(array, begin, partitionIndex - 1, doubleComparator);
            quickSort(array,partitionIndex + 1, end, doubleComparator);
        }
    }

    /**
     * <p>
     *     Generates an array of n random sized arrays of random generated double values.
     *     Sizes of array cannot be the same. Arrays with even index are sorted in ascending order, while
     *     arrays with odd index are sorted in descending order.
     * </p>
     * @param n amount of random generated arrays
     * @return array of sorted arrays
     */
    public double[][] sortArrays(int n) {
        double[][] result = new double[n][];

        init(result);

        for (int i = 0; i < n; i++) {
            if (i % 2 == 0)
                // ascending order
                quickSort(result[i], 0, result[i].length - 1,
                        (a, b) -> Math.abs(a - b) <= Math.min(Math.ulp(a), Math.ulp(b)) ? 0 : (a > b ? 1 : -1));
            else
                // descending order
                quickSort(result[i], 0, result[i].length - 1,
                        (a, b) -> Math.abs(a - b) <= Math.min(Math.ulp(a), Math.ulp(b)) ? 0 : (a < b ? 1 : -1));
        }

        return result;
    }
}

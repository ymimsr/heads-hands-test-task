import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SolutionTest {

    private static Stream<Arguments> provideTestsForDefault() {
        return Stream.of(
                Arguments.of(
                        1
                ),
                Arguments.of(
                        10
                ),
                Arguments.of(
                        40
                )
        );
    }

    private static Stream<Arguments> provideTestsForCustom() {
        return Stream.of(
                Arguments.of(
                        1,
                        100,
                        200,
                        12.3,
                        123.1
                ),
                Arguments.of(
                        10,
                        10,
                        20,
                        0,
                        0.1
                ),
                Arguments.of(
                        40,
                        2,
                        50,
                        -7.2,
                        -1
                ),
                Arguments.of(
                        5,
                        10,
                        50,
                        0,
                        0
                )
        );
    }

    /**
     * <p>
     *     Tests if expected result matches actual result.
     * </p>
     * <p>
     *     Clones actual result and sorts it using standard library quicksort algorithm.
     * </p>
     * @param result actual result
     */
    private void solutionTest(double[][] result) {
        double[][] expResult = Arrays
                .stream(result)
                .map(double[]::clone)
                .peek(Arrays::sort)
                .toArray(double[][]::new);
        IntStream.range(0, expResult.length)
                .filter(i -> i % 2 != 0)
                .forEach(i -> IntStream
                        .range(0, expResult[i].length / 2 + (expResult.length % 2))
                        .forEach(k -> {
                            double swapTemp = expResult[i][k];
                            expResult[i][k] = expResult[i][expResult[i].length - k - 1];
                            expResult[i][expResult[i].length - k - 1] = swapTemp;
                        }));

        assertArrayEquals(expResult, result);
    }

    /**
     * <p>
     *     Tests sortArrays with default configuration.
     * </p>
     * @param n amount of random generated arrays
     */
    @ParameterizedTest
    @MethodSource("provideTestsForDefault")
    public void testDefault(int n) {
        Solution solution = new Solution();
        double[][] result = solution.sortArrays(n);

        solutionTest(result);
    }

    /**
     * <p>
     *     Tests sortArrays with custom configuration.
     * </p>
     * <p>
     *     Also checks if generated arrays' sizes and elements of these arrays are in the given range.
     * </p>
     * @param n amount of random generated arrays
     * @param MIN_SIZE minimum size of generated arrays
     * @param MAX_SIZE maximum size of generated arrays
     * @param MIN_VALUE minimum value of elements of generated arrays
     * @param MAX_VALUE maximum value of elements of generate arrays
     */
    @ParameterizedTest
    @MethodSource("provideTestsForCustom")
    public void testCustomSize(int n, int MIN_SIZE, int MAX_SIZE, double MIN_VALUE, double MAX_VALUE) {
        Solution solution = new Solution(MIN_SIZE, MAX_SIZE, MIN_VALUE, MAX_VALUE);
        double[][] result = solution.sortArrays(n);

        assertTrue(Arrays
                .stream(result)
                .allMatch(arr -> arr.length <= MAX_SIZE && arr.length >= MIN_SIZE));
        assertTrue(Arrays
                .stream(result)
                .allMatch(arr -> Arrays.stream(arr).allMatch(elem -> elem >= MIN_VALUE && elem <= MAX_VALUE)));

        solutionTest(result);
    }

    /**
     * <p>
     *     Checks if IllegalArgumentException is thrown due to invalid value of MIN_SIZE.
     * </p>
     */
    @Test
    public void illegalMinSize() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Solution(-1, 100));

        String expMessage = "cannot be negative";
        String message = exception.getMessage();

        assertTrue(message.contains(expMessage));
    }

    /**
     * <p>
     *     Checks if IllegalArgumentException is thrown due to invalid size range.
     * </p>
     */
    @Test
    public void illegalSizeRange() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Solution(1, 100).sortArrays(1000));

        String expMessage = "no size duplicates requirement cannot be maintained";
        String message = exception.getMessage();

        assertTrue(message.contains(expMessage));
    }

}

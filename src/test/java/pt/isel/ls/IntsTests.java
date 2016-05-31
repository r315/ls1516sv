package pt.isel.ls;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class IntsTests {

    private static boolean isNonDecreasingOrder(int[] a, int l, int r) {
        for (int i = l; i < r; i++)
            if (a[i] > a[i+1])
                return false;
        return true;
    }

    @Test
    public void max_returns_greatest(){
        assertEquals(1, Ints.max(1, -2));
        assertEquals(1, Ints.max(-2, 1));
        assertEquals(-1, Ints.max(-1,-2));
        assertEquals(-1, Ints.max(-2,-1));
    }

    @Test
    public void empty_array_is_always_sorted() {
        // Arrange
        int[] values = new int[0];
        // Act
        Ints.insertionSort(values, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalid_left_argument_throws_exception() {
        // Arrange
        int[] values = {-20,4,-1,2,1,0,10};
        // Act
        Ints.insertionSort(values, -1, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalid_right_argument_throws_exception() {
        // Arrange
        int[] values = {-20,4,-1,2,1,0,10};
        // Act
        Ints.insertionSort(values, 2, values.length);
    }

    @Test
    public void given_an_empty_interval_insertionSort_does_not_change_the_array() {
        // Arrange
        int[] values = {-20,4,-1,2,1,0,10};
        int[] copy = Arrays.copyOf(values, values.length);
        // Act
        Ints.insertionSort(values, 5, 1);
        // Assert
        assertArrayEquals(values, copy);
    }

    @Test
    public void sorts_all_elements_of_a_small_array() {
        // Arrange
        int[] values = {-20,4,-1,2,1,0,10};
        // Act
        Ints.insertionSort(values, 0, values.length-1);
        // Assert
        assertTrue(isNonDecreasingOrder(values, 0, values.length-1));
    }

    @Test
    public void sorts_a_sub_range_of_a_small_array() {
        // Arrange
        int[] values = {-20,4,-1,2,1,0,10};
        // Act
        Ints.insertionSort(values, 2, 5);
        // Assert
        assertTrue(isNonDecreasingOrder(values, 2, 5));
    }
}

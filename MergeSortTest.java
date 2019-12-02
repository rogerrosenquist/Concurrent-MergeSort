import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MergeSortTest {

    @Test
    public void sortedTest(){
        int[] arr1 = {60, 40, 50}; // should return false
        int[] arr2 = {40, 50, 60}; // should return true
        assertFalse(MergeSort.sorted(arr1));
        assertTrue(MergeSort.sorted(arr2));

        int[] arr3 = {}; // should return true since it's technically sorted
        assertTrue(MergeSort.sorted(arr3));

        int[] arr4 = null; // should return false since it can't be sorted
        assertFalse(MergeSort.sorted(arr4));
    }
}
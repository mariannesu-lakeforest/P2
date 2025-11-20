import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Sorting class provides in-place implementations of the quicksort and heapsort algorithms. 
 * Both algorithms count and return the number of element comparisons made during the sorting process.
 * 
 * <p>All sorting is done in-place; no auxiliary data structures are created.</p>
 * 
 * @author Marianne Su
 */

public class Sorting {
    /**
     * Sorts the given list in-place using the quicksort algorithm.
     * Uses the first element of each partition as the pivot.
     *
     * @param <T>  the type of elements in the list, must be Comparable
     * @param list the list to sort
     * @return the number of element comparisons made
     */
    
    public static <T extends Comparable<? super T>> int quicksort(List<T> list) {
        return quicksortHelper(list, 0, list.size() - 1);
    }

    // Recursive helper for quicksort
    private static <T extends Comparable<? super T>> int quicksortHelper(List<T> list, int low, int high) {
        if (low >= high) {
            return 0;
        }

        int comparisons = 0;
        int pivotIndex = low;
        T pivotValue = list.get(pivotIndex);

        int left = low + 1;
        int right = high;

        while (left <= right) {
            // Move left index to the right as long as elements are <= pivot
            while (left <= right) {
                comparisons++;
                if (list.get(left).compareTo(pivotValue) > 0) {
                    break;
                }
                left++;
            }

            // Move right index to the left as long as elements are >= pivot
            while (left <= right) {
                comparisons++;
                if (list.get(right).compareTo(pivotValue) < 0) {
                    break;
                }
                right--;
            }

            if (left < right) {
                swap(list, left, right);
                left++;
                right--;
            }
        }

        // Place pivot in its correct position
        swap(list, pivotIndex, right);

        // Recursively sort the partitions
        comparisons += quicksortHelper(list, low, right - 1);
        comparisons += quicksortHelper(list, right + 1, high);

        return comparisons;
    }

    /**
     * Sorts the given list in-place using the heapsort algorithm.
     * Builds a max heap and repeatedly extracts the maximum element.
     *
     * @param <T>  the type of elements in the list, must be Comparable
     * @param list the list to sort
     * @return the number of element comparisons made
     */
    public static <T extends Comparable<? super T>> int heapsort(List<T> list) {
        int n = list.size();
        int comparisons = 0;

        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            comparisons += heapify(list, n, i);
        }

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            swap(list, 0, i);
            comparisons += heapify(list, i, 0);
        }

        return comparisons;
    }

    // Maintains the max-heap property for a subtree rooted at index i
    private static <T extends Comparable<? super T>> int heapify(List<T> list, int heapSize, int i) {
        int comparisons = 0;
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // Compare left child
        if (left < heapSize) {
            comparisons++;
            if (list.get(left).compareTo(list.get(largest)) > 0) {
                largest = left;
            }
        }

        // Compare right child
        if (right < heapSize) {
            comparisons++;
            if (list.get(right).compareTo(list.get(largest)) > 0) {
                largest = right;
            }
        }

        if (largest != i) {
            swap(list, i, largest);
            comparisons += heapify(list, heapSize, largest);
        }

        return comparisons;
    }

    // Swaps two elements in a list
    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // Verifies that a list is sorted in non-decreasing order
    private static <T extends Comparable<? super T>> boolean isSorted(List<T> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1).compareTo(list.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Main method for testing and comparing quicksort and heapsort.
     * Generates two identical lists of random integers, sorts them, verifies correctness, and reports comparison counts.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        Random rand = new Random();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        for (int i = 0; i < 20000; i++) {
            int value = rand.nextInt(1_000_000);
            list1.add(value);
            list2.add(value);
        }

        int quickComparisons = quicksort(list1);
        int heapComparisons = heapsort(list2);

        System.out.println("Quicksort comparisons: " + quickComparisons);
        System.out.println("Heapsort comparisons:  " + heapComparisons);

        System.out.println("Quicksort sorted: " + isSorted(list1));
        System.out.println("Heapsort sorted:  " + isSorted(list2));
    }
}

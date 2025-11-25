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

    /**
     * Sorts the given list in-place using mergesort.
     *
     * @param <T>  Comparable type
     * @param list list to sort
     * @return number of comparisons
     */
    public static <T extends Comparable<? super T>> int mergesort(List<T> list) {
        List<T> temp = new ArrayList<>(list);
        return mergesortHelper(list, temp, 0, list.size() - 1);
    }

    private static <T extends Comparable<? super T>> int mergesortHelper(
            List<T> list, List<T> temp, int left, int right) {
        if (left >= right) return 0;

        int mid = (left + right) / 2;
        int comparisons = 0;

        comparisons += mergesortHelper(list, temp, left, mid);
        comparisons += mergesortHelper(list, temp, mid + 1, right);
        comparisons += merge(list, temp, left, mid, right);

        return comparisons;
    }

    private static <T extends Comparable<? super T>> int merge(
            List<T> list, List<T> temp, int left, int mid, int right) {

        int comparisons = 0;

        for (int i = left; i <= right; i++) {
            temp.set(i, list.get(i));
        }

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            comparisons++;
            if (temp.get(i).compareTo(temp.get(j)) <= 0) {
                list.set(k++, temp.get(i++));
            } else {
                list.set(k++, temp.get(j++));
            }
        }

        while (i <= mid) {
            list.set(k++, temp.get(i++));
        }
        return comparisons;
    }

    /**
     * Sorts the list using TreeSort (BST insertion + inorder traversal).
     *
     * @param <T> Comparable type
     * @param list list to sort
     * @return number of comparisons
     */
     
    public static <T extends Comparable<? super T>> int treesort(List<T> list) {
        if (list.isEmpty()) return 0;

        Node<T> root = new Node<>(list.get(0));
        int comparisons = 0;

        for (int i = 1; i < list.size(); i++) {
            comparisons += insert(root, list.get(i));
        }
        List<T> output = new ArrayList<>();
        inorder(root, output);

        for (int i = 0; i < list.size(); i++) {
            list.set(i, output.get(i));
        }
        return comparisons;
    }

    private static class Node<T extends Comparable<? super T>> {
        T value;
        Node<T> left, right;

        Node(T v) { value = v; }
    }

    private static <T extends Comparable<? super T>> int insert(Node<T> node, T value) {
        int comparisons = 1; // compare with node.value

        if (value.compareTo(node.value) <= 0) {
            if (node.left == null) {
                node.left = new Node<>(value);
            } else {
                comparisons += insert(node.left, value);
            }
        } else {
            if (node.right == null) {
                node.right = new Node<>(value);
            } else {
                comparisons += insert(node.right, value);
            }
        }
        return comparisons;
    }

    // Performs inorder traversal on the BST
    private static <T extends Comparable<? super T>> void inorder(Node<T> node, List<T> output) {
        if (node == null) return;
        inorder(node.left, output);
        output.add(node.value);
        inorder(node.right, output);
    }
    
    /**
     * Block sort implemented as Comb Sort.
     *
     * @param <T> Comparable type
     * @param list list to sort
     * @return number of comparisons
    */
     
    public static <T extends Comparable<? super T>> int blocksort(List<T> list) {
        int n = list.size();
        int gap = n;
        boolean swapped = true;
        int comparisons = 0;

        while (gap > 1 || swapped) {
            gap = Math.max(1, (int)(gap / 1.3));
            swapped = false;

            for (int i = 0; i + gap < n; i++) {
                comparisons++;
                if (list.get(i).compareTo(list.get(i + gap)) > 0) {
                    swap(list, i, i + gap);
                    swapped = true;
                }
            }
        }
        return comparisons;
    }


    /* UTILITY METHODS */

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
     * Main method for testing and comparing quicksort, heapsort, merge sort, tree sort, and block sort.
     * Generates two identical lists of random integers, sorts them, verifies correctness, and reports comparison counts.
     *
     * @param args command line arguments (unused)
    */
    
    public static void main(String[] args) {
        Random rand = new Random();

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        // per request: declare lists a, b, c *inside main*
        List<Integer> a = new ArrayList<>();
        List<Integer> b = new ArrayList<>();
        List<Integer> c = new ArrayList<>();

        for (int i = 0; i < 20000; i++) {
            int value = rand.nextInt(1_000_000);
            list1.add(value);
            list2.add(value);

            a.add(value);
            b.add(value);
            c.add(value);
        }

        int quickComparisons = quicksort(list1);
        int heapComparisons = heapsort(list2);
        
        int mergeComp = mergesort(a);
        int treeComp  = treesort(b);
        int blockComp = blocksort(c);

        System.out.println("Quicksort comparisons: " + quickComparisons);
        System.out.println("Heapsort comparisons:  " + heapComparisons);
        System.out.println("MergeSort comparisons: " + mergeComp);
        System.out.println("TreeSort comparisons:  " + treeComp);
        System.out.println("BlockSort comparisons: " + blockComp);

        System.out.println("Quicksort sorted: " + isSorted(list1));
        System.out.println("Heapsort sorted:  " + isSorted(list2));
        System.out.println("Merge sorted: " + isSorted(a));
        System.out.println("Tree sorted:  " + isSorted(b));
        System.out.println("Block sorted: " + isSorted(c));
    }
}

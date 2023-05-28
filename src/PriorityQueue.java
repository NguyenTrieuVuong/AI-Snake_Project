import java.util.ArrayList;
import java.util.List;

public class PriorityQueue<E extends Comparable<E>> {
    private List<E> queue;  // List to store the elements of the priority queue

    public PriorityQueue() {
        queue = new ArrayList<>();  // Initialize the queue as an empty ArrayList
    }

    public void add(E element) {
        queue.add(element);  // Add the element to the end of the queue
        int i = queue.size() - 1;  // Index of the newly added element
        // Perform "up-heap" operation to maintain the min-heap property
        while (i > 0 && queue.get(i).compareTo(queue.get(parent(i))) < 0) {
            swap(i, parent(i));  // Swap the element with its parent if it is smaller
            i = parent(i);  // Move up to the parent index
        }
    }

    public E poll() {
        if (queue.isEmpty()) {
            return null;  // If the queue is empty, return null
        }
        E element = queue.get(0);  // Get the root element (minimum element)
        queue.set(0, queue.get(queue.size() - 1));  // Replace the root with the last element
        queue.remove(queue.size() - 1);  // Remove the last element from the queue
        minHeapify(0);  // Perform "down-heap" operation to maintain the min-heap property
        return element;  // Return the removed element (minimum element)
    }

    public boolean contains(E element) {
        return queue.contains(element);  // Check if the queue contains the specified element
    }

    public boolean isEmpty() {
        return queue.isEmpty();  // Check if the queue is empty
    }

    private void minHeapify(int i) {
        int left = left(i);  // Index of the left child
        int right = right(i);  // Index of the right child
        int smallest = i;  // Assume the current node is the smallest
        // Compare the left child with the current node and update the smallest index if necessary
        if (left < queue.size() && queue.get(left).compareTo(queue.get(smallest)) < 0) {
            smallest = left;
        }
        // Compare the right child with the current node and update the smallest index if necessary
        if (right < queue.size() && queue.get(right).compareTo(queue.get(smallest)) < 0) {
            smallest = right;
        }
        if (smallest != i) {
            swap(i, smallest);  // Swap the current node with the smallest child
            minHeapify(smallest);  // Recursively perform "down-heap" operation on the affected child
        }
    }

    private void swap(int i, int j) {
        E temp = queue.get(i);  // Temporary variable to hold the element at index i
        queue.set(i, queue.get(j));  // Replace element at index i with element at index j
        queue.set(j, temp);  // Replace element at index j with the temporary variable
    }

    private int parent(int i) {
        return (i - 1) / 2;  // Calculate the index of the parent node
    }

    private int left(int i) {
        return 2 * i + 1;  // Calculate the index of the left child node
    }

    private int right(int i) {
        return 2 * i + 2;  // Calculate the index of the right child node
    }
}

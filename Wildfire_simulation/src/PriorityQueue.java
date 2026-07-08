/**
 * PriorityQueue - A simple priority queue implementation used by Dijkstra's
 * algorithm for the wildfire spread simulation.
 * 
 * This priority queue always extracts the node with the smallest distance
 * (lowest fire spread cost). It is implemented using the custom ArrayList
 * 
 * @param <T> the generic type of the nodes stored in this queue
 * @author Clean_Code
 * @version 1.0
 */
public class PriorityQueue<T> {

	private ArrayList<Node<T>> list = new ArrayList<>();

	/**
	 * Adds a node to the priority queue. Since we always extract the minimum, the
	 * insertion order does not matter.
	 * 
	 * @param node the Node to be added to the queue
	 */
	public void add(Node<T> node) {
		list.add(list.size(), node); // add at the end
	}

	/**
	 * Checks whether the priority queue is empty.
	 * 
	 * @return true if the queue contains no nodes, false otherwise
	 */
	public boolean isEmpty() {
		return list.size() == 0;
	}

	/**
	 * Removes and returns the node with the smallest distance (lowest
	 * cost). This is the core operation used by Dijkstra's algorithm to select the
	 * next node to process.
	 * 
	 * @return the Node with the smallest distance, or null if the queue is empty
	 */
	public Node<T> pull() {

		if (isEmpty())
			return null;

		int smallestIndex = 0;
		Node<T> smallest = list.get(0);

		// Linear scan to find the node with minimum distance
		for (int i = 1; i < list.size(); i++) {

			Node<T> current = list.get(i);

			if (current.getDistance() < smallest.getDistance()) {
				smallest = current;
				smallestIndex = i;
			}
		}

		// Remove and return the smallest node
		return list.remove(smallestIndex);
	}

	/**
	 * Returns the node with the smallest distance without removing it
	 * 
	 * @return the Node with the smallest distance, or null if the queue is empty
	 */
	public Node<T> peek() {

		if (isEmpty())
			return null;

		Node<T> smallest = list.get(0);

		for (int i = 1; i < list.size(); i++) {

			Node<T> current = list.get(i);

			if (current.getDistance() < smallest.getDistance()) {
				smallest = current;
			}
		}

		return smallest;
	}

	/**
	 * Returns the current number of nodes in the priority queue.
	 * 
	 * @return the number of elements in the queue
	 */
	public int size() {
		return list.size();
	}
}
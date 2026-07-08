/**
 * Dijkstra - Implements Dijkstra's shortest path algorithm tailored for
 * wildfire spread simulation.
 * 
 * In this context, "shortest path" means the path with the lowest total fire
 * spread cost. Lower cost = fire spreads faster (e.g., DRY GRASS has cost 1,
 * WATER has infinite cost).
 * 
 * The algorithm finds the most likely propagation path a wildfire would take
 * across the terrain from the starting point to the end point.
 * 
 * @author Clean_Code
 * @version 1.0
 */
public class Dijkstra {

	/**
	 * Finds the lowest-cost path from the start node to the end node using Dijkstra's algorithm
	 * 
	 * @param start the starting node (usually top-left corner)
	 * @param end   the ending node (usually bottom-right corner)
	 * @return an ArrayList containing the nodes in the fire propagation path from
	 *         start to end (in correct order)
	 */
	public static <T> ArrayList<Node<T>> findPath(Node<T> start, Node<T> end) {

		PriorityQueue<T> pq = new PriorityQueue<>();

		// Initialize start node
		start.setDistance(0);
		pq.add(start);

		int safetyCounter = 0;

		while (!pq.isEmpty()) {

			//Prevent infinity loop
			if (++safetyCounter > 100000) {
				System.out.println("Stopped: safety limit reached");
				break;
			}

			Node<T> current = pq.pull();

			if (current == null)
				break;

			if (current.isVisited())
				continue;

			current.setVisited(true);

			// Relax all neighboring edges
			for (int i = 0; i < current.getEdges().size(); i++) {

				Edge<T> edge = current.getEdges().get(i);
				Node<T> neighbour = edge.getTarget();

				//Skip impossible paths (water barriers)
				if (edge.getWeight() == Double.MAX_VALUE)
					continue;

				double newDist = current.getDistance() + edge.getWeight();

				// If a better (lower cost) path is found, update distance and previous pointer
				if (newDist < neighbour.getDistance()) {

					neighbour.setDistance(newDist);
					neighbour.setPrevious(current);

					pq.add(neighbour);
				}
			}
		}

		//Build the path from start to end
		ArrayList<Node<T>> path = new ArrayList<>();
		Node<T> curr = end;

		//Prevent infinite backtracking in case of a disconnected graph
		int pathSafety = 0;

		while (curr != null && pathSafety++ < 10000) {
			path.add(path.size(), curr); // insert at front (will be reversed later)
			curr = curr.getPrevious();
		}

		reverse(path);
		return path;
	}

	/**
	 * Reverses the order of elements in the given ArrayList to
	 * convert the backward-built path into the correct forward order.
	 * 
	 * @param list the ArrayList to be reversed
	 */
	private static <T> void reverse(ArrayList<T> list) {

		int left = 0;
		int right = list.size() - 1;

		while (left < right) {

			T temp = list.get(left);
			list.set(left, list.get(right));
			list.set(right, temp);

			left++;
			right--;
		}
	}
}
/**
 * Represents a directed connection between two terrain nodes in the
 * wildfire graph.
 * 
 * Each edge carries a weight that corresponds to the fire spread cost of moving
 * from the current node into the target node.
 * 
 * Lower weight = fire spreads faster (e.g., DRY GRASS has low cost). Water
 * edges have weight = Double.MAX_VALUE (fire cannot cross).
 * 
 * @param <T> the generic type of the Node this edge points to
 * @author Clean_Code
 * @version 1.0
 */
public class Edge<T> {

	private Node<T> target;
	private double weight;

	/**
	 * Constructs a new edge pointing to the target node with the given fire spread
	 * cost.
	 * 
	 * @param target the destination node this edge leads to
	 * @param weight the fire spread cost of moving into this target node
	 */
	public Edge(Node<T> target, double weight) {
		this.setTarget(target);
		this.setWeight(weight);
	}

	/**
	 * Returns the target node that this edge connects to.
	 * 
	 * @return the destination Node
	 */
	public Node<T> getTarget() {
		return target;
	}

	/**
	 * Sets the target node for this edge.
	 * 
	 * @param target the new destination node
	 */
	public void setTarget(Node<T> target) {
		this.target = target;
	}

	/**
	 * Returns the weight (fire spread cost) of this edge.
	 * 
	 * @return the cost of traversing this edge
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Sets the weight (fire spread cost) of this edge.
	 * 
	 * @param weight the new fire spread cost for this edge
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
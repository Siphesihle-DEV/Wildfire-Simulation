/**
 * Node - Represents a single terrain block (cell) in the wildfire propagation
 * graph.
 * 
 * Each node corresponds to a block of pixels in the input image and stores: 
 * Its grid position (row, col) - Terrain type (DRY GRASS, GRASS, FOREST, WATER)
 * - Fire spread cost (lower = faster spread) - Dijkstra algorithm state
 * (distance, visited, previous pointer) - List of adjacent edges to neighboring
 * terrain blocks
 * 
 * This class is the fundamental building block of the Graph ADT used in the
 * wildfire spread simulation system.
 * 
 * @param <T> the generic type parameter (used for type safety with edges)
 * @author Clean_Code
 * @version 1.0
 */
public class Node<T> {

	private int row;
	private int col;
	private double cost; // fire spread cost based on terrain type
	private String terrain; // terrain classification
	private double distance; // used by Dijkstra (current best known cost to reach this node)
	private boolean visited; // whether this node has been processed by Dijkstra
	private ArrayList<Edge<T>> edges; // list of adjacent terrain blocks
	private Node<T> previous; // used to reconstruct the path after Dijkstra finishes

	/**
	 * Constructs a new Node at the specified grid position. Initializes
	 * Dijkstra-related fields and an empty list of edges.
	 * 
	 * @param row the row index in the terrain grid
	 * @param col the column index in the terrain grid
	 */
	public Node(int row, int col) {
		this.row = row;
		this.col = col;
		this.edges = new ArrayList<>();

		// Initialize for Dijkstra's algorithm
		this.distance = Double.MAX_VALUE; // infinity = unknown distance
		this.visited = false;
		this.previous = null;
	}

	/**
	 * Adds a directed edge to a neighboring node. The weight of the edge is the
	 * fire spread cost of the target node.
	 * 
	 * @param target the neighboring node to connect to
	 */
	public void addEdge(Node<T> target) {
		// Use the target's terrain cost as the weight (cost of spreading fire INTO this
		// cell)
		double weight = target.cost;
		edges.add(0, new Edge<T>(target, weight));
	}

	//Getters and setters

	/**
	 * Returns the row index of this node in the grid.
	 * 
	 * @return row index
	 */
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Returns the column index of this node in the grid.
	 * 
	 * @return column index
	 */
	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	/**
	 * Returns the fire spread cost of this terrain block.
	 * 
	 * @return cost value (1.0 = DRY GRASS, higher = slower spread)
	 */
	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * Returns the terrain type of this block.
	 * 
	 * @return "DRY GRASS", "GRASS", "FOREST", or "WATER"
	 */
	public String getTerrain() {
		return terrain;
	}

	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}

	/**
	 * Returns the current known distance (total fire spread cost) to reach this
	 * node. Used internally by Dijkstra's algorithm.
	 * 
	 * @return current best known distance
	 */
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Returns whether this node has already been visited by Dijkstra's algorithm.
	 * 
	 * @return true if visited, false otherwise
	 */
	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	/**
	 * Returns the list of edges to adjacent nodes.
	 * 
	 * @return list of outgoing edges
	 */
	public ArrayList<Edge<T>> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge<T>> edges) {
		this.edges = edges;
	}

	/**
	 * Returns the previous node in the current best path (used for path
	 * reconstruction).
	 * 
	 * @return previous node in the path
	 */
	public Node<T> getPrevious() {
		return previous;
	}

	public void setPrevious(Node<T> previous) {
		this.previous = previous;
	}
}
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Graph - Core class responsible for converting a terrain/satellite image into
 * a 2D grid-based graph for wildfire spread simulation.
 * 
 * Each block of pixels becomes a Node in the graph. Terrain classification
 * determines the fire spread cost for each node. Adjacency edges connect
 * neighboring blocks to model fire propagation.
 *  
 * @author Clean_Code
 * @version 1.0
 */
public class Graph {

	/**
	 * Builds a 2D grid graph from the input terrain image.
	 * 
	 * @param image     the BufferedImage containing terrain data (satellite or mask)
	 * @param blockSize the size of each terrain block in pixels
	 * @return a 2D array of {@link Node} objects representing the terrain graph
	 */
	public static <T> Node<T>[][] buildGraph(BufferedImage image, int blockSize) {

		int rows = image.getHeight() / blockSize;
		int cols = image.getWidth() / blockSize;

		@SuppressWarnings("unchecked")
		Node<T>[][] grid = new Node[rows][cols];

		//Create nodes and classify terrain for each block
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {

				Node<T> node = new Node<>(r, c);

				String terrain = classifyBlock(image, r, c, blockSize);
				node.setTerrain(terrain);

				node.setCost(getCost(terrain));

				grid[r][c] = node;
			}
		}

		//Connect adjacent nodes with edges (4-directional adjacency)
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {

				Node<T> current = grid[r][c];

				if (r > 0)
					current.addEdge(grid[r - 1][c]); // North
				if (r < rows - 1)
					current.addEdge(grid[r + 1][c]); // South
				if (c > 0)
					current.addEdge(grid[r][c - 1]); // West
				if (c < cols - 1)
					current.addEdge(grid[r][c + 1]); // East
			}
		}

		return grid;
	}

	/**
	 * Classifies a square block of pixels into one of four terrain types based on
	 * average RGB values. This classification determines how fast fire can spread
	 * through that area.
	 * 
	 * @param image the source terrain image
	 * @param row   the row index of the current block
	 * @param col   the column index of the current block
	 * @param size  the block size in pixels
	 * @return terrain type: "DRY GRASS", "GRASS", "FOREST", or "WATER"
	 */
	private static String classifyBlock(BufferedImage image, int row, int col, int size) {

		int startX = col * size;
		int startY = row * size;

		long r = 0, g = 0, b = 0;
		int count = 0;

		for (int y = startY; y < startY + size; y++) {
			for (int x = startX; x < startX + size; x++) {

				// Safety check to avoid index out of bounds
				if (x >= image.getWidth() || y >= image.getHeight())
					continue;

				Color color = new Color(image.getRGB(x, y));

				r += color.getRed();
				g += color.getGreen();
				b += color.getBlue();
				count++;
			}
		}

		if (count == 0)
			return "FOREST";

		int avgR = (int) (r / count);
		int avgG = (int) (g / count);
		int avgB = (int) (b / count);

		//Water detection - strong blue dominance
		if (avgB > avgG * 1.2 && avgB > avgR * 1.2) {
			return "WATER";
		}

		//Grass detection - green dominant
		if (avgG > avgR * 1.1 && avgG > avgB * 1.1) {
			return "GRASS";
		}

		//Dry grass detection - high red, low green (highest fire risk)
		if (avgR > avgG * 1.2 && avgR > 120) {
			return "DRY GRASS";
		}

		//Default vegetation / mixed forest
		return "FOREST";
	}

	/**
	 * Returns the fire spread cost for a given terrain type. Lower cost means fire
	 * spreads faster through that terrain.
	 * 
	 * @param terrain the terrain classification string
	 * @return fire spread cost (1.0 = fastest, Double.MAX_VALUE = impassable)
	 */
	private static double getCost(String terrain) {
		switch (terrain) {
		case "GRASS":
			return 2;
		case "FOREST":
			return 3;
		case "DRY GRASS":
			return 1; // Highest potential for a wildfire - fastest spread
		case "WATER":
			return Double.MAX_VALUE; // Fire cannot spread through water
		default:
			return 3;
		}
	}
}
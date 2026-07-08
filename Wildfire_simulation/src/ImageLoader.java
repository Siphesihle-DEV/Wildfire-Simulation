import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Utility class for loading terrain or satellite images from disk.
 * 
 * This class provides a static method to load images using Java's
 * ImageIO. It is used by the MainUI to load the input image before converting
 * it into a graph for wildfire spread simulation.
 * 
 * @author Clean_Code
 * @version 1.0
 */
public class ImageLoader {

	/**
	 * Loads an image from the specified file path.
	 * 
	 * @param path the absolute or relative file path to the image file 
	 * @return the loaded BufferedImage, or null if the file could not be loaded or is invalid
	 */
	public static BufferedImage load(String path) {

		try {
			// Create a File object from the given path
			File file = new File(path);

			// Read the image from the file using ImageIO
			BufferedImage image = ImageIO.read(file);

			// Check if image was successfully loaded
			if (image == null) {
				System.out.println("Error: Unsupported or corrupted image file.");
				return null;
			}

			// Return the loaded image
			return image;

		} catch (IOException e) {
			// Handle file not found, permission issues, or read errors
			System.out.println("Error loading image: " + e.getMessage());
			return null;
		}
	}
}
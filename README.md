
 # Graph-Based Wildfire Spread Simulation System

## Project Description
A Graph-based desktop Java application that converts terrain or satellite images into a graph structure, where each block of pixels becomes a node. The system automatically classifies different terrain types (Dry Grass, Grass, Forest, and Water) using average RGB values, then builds a weighted graph based on fire spread costs.

It simulates wildfire propagation using **Dijkstra’s algorithm** to determine the most likely (lowest-cost) path fire would take. The application generates a **color-coded risk mask** overlaid on the original image:
- **Red** = High Risk (mainly Dry Grass) – Fire spreads very quickly
- **Yellow** = Medium Risk (Grass and Forest)
- **Blue** = Low Risk / Blocked (Water bodies) – Fire cannot spread

The system also displays useful statistics such as the total number of nodes and a breakdown of nodes by terrain type. This project demonstrates the practical application of Graph ADT, image processing, and pathfinding algorithms to solve a real-world environmental problem.

## How to Run the Application

**Note:** JavaFX is required to run the application.

### Option A: Primitive (Manual)
Create all the classes in your project and copy-paste the respective code files.

### Option B: Minimum (Using Eclipse - Recommended)
1. Right-click on your project name in **Package Explorer**.
2. Select **Export...**
3. In the Export window:
   - Expand **Java**
   - Select **Runnable JAR file**
   - Click **Next**
4. In the "Runnable JAR File Specification" window:
   - Under **Launch configuration**, select the configuration that uses **Main** class
   - Under **Export destination**, click **Browse**
   - Go to your project folder → open the `dist` folder
   - Name the JAR file and Click **Save**
   - Under **Library handling**, select:  
     **"Package required libraries into generated JAR"**
   - Click **Finish**

5. Test images are available in the `images` folder. You can also use images from the DeepGlobe Land Cover Classification dataset for testing.

## YouTube Video Link
Unlisted YouTube presentation video: https://youtu.be/QMfMOcMytmA

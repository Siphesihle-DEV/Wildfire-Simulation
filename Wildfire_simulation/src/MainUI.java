import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.DropShadow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MainUI extends Application {

    private ImageView imageView = new ImageView();

    private BufferedImage loadedImage;
    private BufferedImage originalImage;

    private Label nodeCountLabel = new Label("--");
    private Label terrainStatsLabel = new Label("--");
    private Label statusLabel = new Label("Ready");

    private TextArea pathArea = new TextArea();

    private Node[][] currentGraph;

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ece9e0;");

        root.setTop(createTopBar());
        root.setLeft(createLeftPanel());
        root.setCenter(createImageContainer());

        imageView.setFitWidth(750);
        imageView.setFitHeight(560);
        imageView.setPreserveRatio(true);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        Button loadBtn = (Button) root.lookup("#loadBtn");
        Button runBtn = (Button) root.lookup("#runBtn");
        Button pathBtn = (Button) root.lookup("#pathBtn");
        Button clearBtn = (Button) root.lookup("#clearBtn");

        //Load image 
        loadBtn.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    loadedImage = ImageIO.read(file);
                    originalImage = ImageIO.read(file);

                    imageView.setImage(new Image(file.toURI().toString()));

                    int block = 10;
                    int rows = loadedImage.getHeight() / block;
                    int cols = loadedImage.getWidth() / block;

                    nodeCountLabel.setText(String.valueOf(rows * cols));
                    terrainStatsLabel.setText("--");

                    pathArea.clear();
                    currentGraph = null;

                    statusLabel.setText("Loaded");

                } catch (Exception ex) {
                    statusLabel.setText("Failed to load image");
                }
            }
        });

        //Generate graph
        runBtn.setOnAction(e -> {

            if (loadedImage == null) {
                statusLabel.setText("Load image first");
                return;
            }

            try {

                currentGraph = Graph.buildGraph(loadedImage, 10);

                int dry = 0, grass = 0, forest = 0, water = 0;

                for (Node[] row : currentGraph) {
                    for (Node n : row) {
                        switch (n.getTerrain()) {
                            case "DRY GRASS" -> dry++;
                            case "GRASS" -> grass++;
                            case "FOREST" -> forest++;
                            case "WATER" -> water++;
                        }
                    }
                }

                terrainStatsLabel.setText(
                        "Dry: " + dry +
                        "\n" + "Grass: " + grass +
                        "\n" + "Forest: " + forest +
                        "\n" + "Water: " + water
                );

                imageView.setImage(createRiskMask(currentGraph, originalImage));
                statusLabel.setText("Mask Generated");

            } catch (Exception ex) {
                statusLabel.setText("Error");
            }
        });

        //Determine the path
        pathBtn.setOnAction(e -> {

            if (currentGraph == null) {
                statusLabel.setText("Generate mask first");
                return;
            }

            Node start = currentGraph[0][0];
            Node end = currentGraph[currentGraph.length - 1][currentGraph[0].length - 1];

            ArrayList<Node> path = Dijkstra.findPath(start, end);

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < path.size(); i++) {
                Node n = path.get(i);

                sb.append("(")
                        .append(n.getRow())
                        .append(",")
                        .append(n.getCol())
                        .append(")");

                if (i < path.size() - 1) sb.append(" -> ");
            }

            pathArea.setText(sb.toString());
            statusLabel.setText("Path computed");
        });

        //clear the screen
        clearBtn.setOnAction(e -> {

            imageView.setImage(null);
            loadedImage = null;
            originalImage = null;
            currentGraph = null;

            nodeCountLabel.setText("--");
            terrainStatsLabel.setText("--");
            pathArea.clear();

            statusLabel.setText("Cleared");
        });

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Wildfire Spread Simulation");
        stage.setScene(scene);
        stage.show();
    }

    //Add containers to canvas 
    private HBox createTopBar() {

        HBox bar = new HBox(10);
        bar.setPadding(new Insets(10));
        bar.setStyle("-fx-background-color: #2c3e2f;");
        bar.setAlignment(Pos.CENTER_LEFT);

        Rectangle icon = new Rectangle(20, 20, Color.web("#d97706"));

        Label title = new Label("WILDFIRE SIMULATION");
        title.setStyle("-fx-text-fill: #ece9e0; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        statusLabel.setStyle("-fx-text-fill: #d4dcc4;");

        bar.getChildren().addAll(icon, title, spacer, statusLabel);
        return bar;
    }

    //Left panel
    private VBox createLeftPanel() {

        VBox panel = new VBox(12);
        panel.setPadding(new Insets(15));
        panel.setPrefWidth(280);
        panel.setStyle("-fx-background-color: #f5f3ef;");

        VBox controls = createCard("CONTROLS");
        Button loadBtn = createButton("Load Image", "#2c5f2d");
        Button runBtn = createButton("Generate Mask", "#d97706");
        Button pathBtn = createButton("Run Dijkstra", "#1e6f9f");
        Button clearBtn = createButton("Clear", "#8e9eae");

        loadBtn.setId("loadBtn");
        runBtn.setId("runBtn");
        pathBtn.setId("pathBtn");
        clearBtn.setId("clearBtn");

        controls.getChildren().addAll(loadBtn, runBtn, pathBtn, clearBtn);

        VBox legend = createCard("LEGEND");
        legend.getChildren().addAll(
                createLegendItem(Color.RED, "Dry Grass"),
                createLegendItem(Color.GOLD, "Grass / Forest"),
                createLegendItem(Color.DODGERBLUE, "Water")
        );

        VBox info = createCard("GRAPH INFO");
        info.getChildren().addAll(nodeCountLabel, terrainStatsLabel);

        VBox pathCard = createCard("SHORTEST PATH");
        pathArea.setPrefHeight(100);
        pathArea.setWrapText(true);
        pathArea.setEditable(false);
        pathArea.setStyle("-fx-font-family: Consolas;");
        pathCard.getChildren().add(pathArea);

        VBox.setVgrow(legend, Priority.ALWAYS);
        VBox.setVgrow(info, Priority.ALWAYS);
        VBox.setVgrow(pathCard, Priority.ALWAYS);

        panel.getChildren().addAll(controls, legend, info, pathCard);

        return panel;
    }

    //Card
    private VBox createCard(String titleText) {

        VBox card = new VBox(8);
        card.setPadding(new Insets(10));
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #ddd;" +
                "-fx-border-radius: 8;"
        );

        Label title = new Label(titleText);
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e2f;");

        card.getChildren().add(title);
        return card;
    }

    //Legend
    private HBox createLegendItem(Color color, String text) {

        HBox box = new HBox(8);
        box.setAlignment(Pos.CENTER_LEFT);

        Rectangle swatch = new Rectangle(14, 14);
        swatch.setFill(color);

        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #333;");

        box.getChildren().addAll(swatch, label);
        return box;
    }

    //
    private Button createButton(String text, String color) {

        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color:" + color + ";" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:5;"
        );
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    //Image container
    private VBox createImageContainer() {

        VBox box = new VBox();
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);

        VBox card = new VBox();
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        card.setEffect(new DropShadow());

        card.getChildren().add(imageView);
        box.getChildren().add(card);

        return box;
    }

    //Create mask
    private WritableImage createRiskMask(Node[][] graph, BufferedImage original) {

        WritableImage img = new WritableImage(original.getWidth(), original.getHeight());
        PixelWriter pw = img.getPixelWriter();

        int size = 10;

        for (int r = 0; r < graph.length; r++) {
            for (int c = 0; c < graph[r].length; c++) {

                String t = graph[r][c].getTerrain();

                int color = switch (t) {
                    case "DRY GRASS" -> 0xE0FF0000;
                    case "GRASS", "FOREST" -> 0xE0FFCC00;
                    case "WATER" -> 0xE00088FF;
                    default -> 0x40000000;
                };

                int x0 = c * size;
                int y0 = r * size;

                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {

                        if (x0 + x < original.getWidth() &&
                                y0 + y < original.getHeight()) {

                            pw.setArgb(x0 + x, y0 + y, color);
                        }
                    }
                }
            }
        }

        return img;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
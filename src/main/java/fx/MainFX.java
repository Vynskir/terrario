package fx;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tiles.Climate;
import tiles.Terrain;
import worlds.World;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class MainFX extends Application {
    private boolean colored;

    private Controller ctrl;
    private World world;

    private Color[] colors;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/terrario.fxml"));
        Parent root = loader.load();

        ctrl = loader.getController();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/terrario.css").toString());

        GridPane tilePane = ctrl.getTilePane();


        this.world = new World(ctrl.getMaxX(), ctrl.getMaxY(), ctrl.getOrigins(), ctrl.getTemp());
        for (int x = 0; x < world.getMatrix().length; x++) {
            for (int y = 0; y < world.getMatrix()[x].length; y++) {
                tilePane.add(new HBox(world.getMatrix()[x][y].getImageView()), x, y);
            }
        }
        generateColors(ctrl.getOrigins());

        ChoiceBox<String> choice = ctrl.getChoice();
        ctrl.getChoice().getSelectionModel().selectedIndexProperty().addListener((observableValue, n1, n2) -> {
            int val = Integer.valueOf(choice.getItems().get((int) n2).replaceAll("[a-z]", ""));
            Terrain.reloadAll(val);
            tilePane.getChildren().clear();

            if (!colored) {
                for (int x = 0; x < world.getMatrix().length; x++) {
                    for (int y = 0; y < world.getMatrix()[x].length; y++) {
                        tilePane.add(new HBox(world.getMatrix()[x][y].getImageView()), x, y);
                    }
                }
            } else {
                switchToColors(tilePane, val);
            }
        });

        ctrl.getResetButton().setOnAction(event -> {
            this.world = world.newInstance(ctrl.getMaxX(), ctrl.getMaxY(), ctrl.getOrigins(), ctrl.getTemp());
            generateColors(ctrl.getOrigins());

            tilePane.getChildren().clear();
            for (int x = 0; x < world.getMatrix().length; x++) {
                for (int y = 0; y < world.getMatrix()[x].length; y++) {
                    tilePane.add(new HBox(world.getMatrix()[x][y].getImageView()), x, y);
                }
            }
        });

        ctrl.getColorButton().setOnAction(event -> {
            tilePane.getChildren().clear();

            if (colored) {
                colored = false;

                for (int x = 0; x < world.getMatrix().length; x++) {
                    for (int y = 0; y < world.getMatrix()[x].length; y++) {
                        tilePane.add(new HBox(world.getMatrix()[x][y].getImageView()), x, y);
                    }
                }
            } else {
                switchToColors(tilePane, Integer.valueOf(ctrl.getChoice().getValue().replaceAll("[a-z]", "")));
            }
        });

        ctrl.getSaveButton().setOnAction(event -> {
            WritableImage image = tilePane.snapshot(new SnapshotParameters(), null);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose save file");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.setInitialFileName("world.png");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));

            File file = fileChooser.showSaveDialog(stage);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stage.setTitle("Terrario");
        stage.getIcons().add(new Image(getClass().getResource("/icons/icon64.png").toString()));
        stage.getIcons().add(new Image(getClass().getResource("/icons/icon32.png").toString()));
        stage.getIcons().add(new Image(getClass().getResource("/icons/icon16.png").toString()));
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setScene(scene);
        stage.show();
    }

    private void generateColors(int max) {
        colors = new Color[max + 1];

        for (int i = 0; i < colors.length; i++) {
            int r = ThreadLocalRandom.current().nextInt(0, 256);
            int g = ThreadLocalRandom.current().nextInt(0, 256);
            int b = ThreadLocalRandom.current().nextInt(0, 256);
            colors[i] = Color.rgb(r, g, b);
        }
    }

    private void switchToColors(GridPane gridPane, int size) {
        colored = true;

        for (int x = 0; x < world.getMatrix().length; x++) {
            for (int y = 0; y < world.getMatrix()[x].length; y++) {
                if (world.getMatrix()[x][y].getOrigin() == null) {
                    gridPane.add(new HBox(new ImageView(Terrain.OCEAN.getImage(Climate.MILD))), x, y);
                } else {
                    gridPane.add(new Rectangle(size, size, colors[world.getMatrix()[x][y].getOrigin().getId()]), x, y);
                }
            }
        }
    }
}

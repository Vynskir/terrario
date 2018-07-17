package fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private GridPane tilePane;

    @FXML
    private Slider maxX;

    @FXML
    private Slider maxY;

    @FXML
    private Slider origins;

    @FXML
    private Slider temp;

    @FXML
    private ChoiceBox<String> choice;

    @FXML
    private Button resetButton;

    @FXML
    private Button colorButton;

    @FXML
    private MenuItem saveButton;

    @FXML
    private ImageView title;

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choice.getItems().add("2px");
        choice.getItems().add("4px");
        choice.getItems().add("8px");
        choice.getItems().add("16px");
        choice.getItems().add("32px");
        choice.getItems().add("64px");
        choice.setValue("8px");

        title.setImage(new Image(getClass().getResource("/images/Title.png").toString()));
    }

    public GridPane getTilePane() {
        return tilePane;
    }

    public int getMaxX() {
        return (int) maxX.getValue();
    }

    public int getMaxY() {
        return (int) maxY.getValue();
    }

    public int getOrigins() {
        return (int) origins.getValue();
    }

    public double getTemp() {
        return temp.getValue();
    }

    public ChoiceBox<String> getChoice() {
        return choice;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getColorButton() {
        return colorButton;
    }

    public MenuItem getSaveButton() {
        return saveButton;
    }
}


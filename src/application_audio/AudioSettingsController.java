package application_audio;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;

public class AudioSettingsController {

    // FXML elements - NO MASTER VOLUME
    @FXML
    private Slider musicSlider;
    @FXML
    private Slider sfxSlider;
    @FXML
    private CheckBox musicToggle;
    @FXML
    private Label musicValue;
    @FXML
    private Label sfxValue;
    @FXML
    private Button resetButton;
    @FXML
    private Button backButton;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView backgroundImage;
    
    private Parent returnRoot;  

    public void setReturnRoot(Parent returnRoot) {
        this.returnRoot = returnRoot;
    }
    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        backgroundImage.setPreserveRatio(false); // fill background

        // Set initial slider values from AudioManager defaults
        musicSlider.setValue(AudioManager.getMusicVolume() * 100);
        sfxSlider.setValue(AudioManager.getSfxVolume() * 100);
        musicToggle.setSelected(AudioManager.isMusicEnabled());
        
        // Set initial label values
        musicValue.setText((int)(AudioManager.getMusicVolume() * 100) + "%");
        sfxValue.setText((int)(AudioManager.getSfxVolume() * 100) + "%");

        sfxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int value = newVal.intValue();
            sfxValue.setText(value + "%");
            AudioManager.setSfxVolume(value / 100.0);
        });

        // Set up listener for music toggle
        musicToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            AudioManager.setMusicEnabled(newVal);
        });
    }

    @FXML
    private void handleReset(ActionEvent event) {
        // Reset audio manager first
        AudioManager.playSfx("chip_bet.wav");
        AudioManager.resetToDefaults();
        
        // Then update UI to match
        musicSlider.setValue(50);
        sfxSlider.setValue(50);
        musicToggle.setSelected(true);

        // Update labels
        musicValue.setText("50%");
        sfxValue.setText("50%");

        System.out.println("Settings reset to defaults");
    }

    @FXML
    private void handleBack(javafx.event.ActionEvent event) {
        AudioManager.playSfx("chip_bet.wav");
        // return to game if it exists
        if (returnRoot != null) {
            rootPane.getScene().setRoot(returnRoot);
            return;
        }

        // fallback (optional)
        if (application.GameController.gameRoot != null) {
            rootPane.getScene().setRoot(application.GameController.gameRoot);
        }
    }

}

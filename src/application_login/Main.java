package application_login;
	
import javafx.application.Application;
import javafx.stage.Stage;
import application_audio.AudioManager;

public class Main extends Application {
	 @Override
	    public void start(Stage stage) {
	        AudioManager.initializeAudio();
	        SceneManager.setStage(stage);
	        stage.setResizable(true);
	        stage.setMinWidth(900);
	        stage.setMinHeight(600);

	        SceneManager.switchScene("homepage.fxml"); // first screen
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
}

package application_login;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;

public class LoginController {
    @FXML private VBox Homebox;
    @FXML private VBox Loginbox;
    @FXML private VBox Registerbox;

    @FXML private TextField usernameField;
    @FXML private PasswordField PasswordField;

    @FXML private TextField reusernameField;
    @FXML private PasswordField repasswordField;
    @FXML private PasswordField reconfirmField;
    
    @FXML private Button Startbutton;
 
    @FXML private Button loginbutton;
    @FXML private Button registerbutton;

    @FXML private Hyperlink hypertoregister;
    @FXML private Hyperlink hypertologin;
    @FXML private Hyperlink hyperbackhome;
    @FXML private Hyperlink rehyperbackhome;

    @FXML private ImageView backgroundImage;
    @FXML private AnchorPane rootPane;
    
    @FXML private Label passwordmessage;
    @FXML private Label usernamemessage;
    @FXML private Label repasswordmessage;
    @FXML private Label reconfirmmessage;
    @FXML private Label reusernamemessage;

    public static Parent loginRoot;
    
    @FXML
    public void initialize() {
        javafx.application.Platform.runLater(() -> loginRoot = rootPane.getScene().getRoot());

    	backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        backgroundImage.setPreserveRatio(false);
        
        lockToPrefSize(Loginbox);
        lockToPrefSize(Registerbox);
        lockToPrefSize(Homebox);

    	showHome();
    	
    }

    private void lockToPrefSize(Region node) {
        node.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }
    
    private void showHome() {
        
        Homebox.setVisible(true);
        Loginbox.setVisible(false);
        Registerbox.setVisible(false);

    }
    
    private void showLogin() {
    	Homebox.setVisible(false);
        Loginbox.setVisible(true);
        Registerbox.setVisible(false);

        clearMessages();    
    }

    private void showRegister() {
    	Homebox.setVisible(false);
        Loginbox.setVisible(false);
        Registerbox.setVisible(true);

        clearRegisterMessages();
    }
   
    @FXML
    private void onStart() {
    	application_audio.AudioManager.playSfx("chip_bet.wav");
        showLogin();
        System.out.println("PLAY clicked");
    }
    
    @FXML
    private void onGoRegister() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        showRegister();
    }
    
    @FXML
    private void onGoLogin() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        showLogin();
        
    }
 
    @FXML
    private void onBackHome() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        showHome();
    }

    @FXML
    private void onLogin() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
    	clearMessages();
    	
    	String username = usernameField.getText().trim();
        String password = PasswordField.getText();
        
        if (username.isEmpty()) {
            showUsernameError("Username is required");
            return;
        }

        if (password.isEmpty()) {
            showPasswordError("Password is required");
            return;
        }
        
        try {
            // 1) login first (get currentUser)
            currentUser = authsys.handleLogin(username, password);

            loginRoot = rootPane.getScene().getRoot();

            javafx.fxml.FXMLLoader loader =
                new javafx.fxml.FXMLLoader(getClass().getResource("/application/Gameboard.fxml"));
            javafx.scene.Parent settingsRoot = loader.load();

            application.GameController controller = loader.getController();
            controller.setUser(currentUser);
            

            rootPane.getScene().setRoot(settingsRoot);

        } catch (Exception e) {
            e.printStackTrace(); 

            String msg = (e.getMessage() == null ? "" : e.getMessage().toLowerCase());

            if (msg.contains("user") || msg.contains("username")) {
                showUsernameError("Username does not exist");
            } else if (msg.contains("password")) {
                showPasswordError("Incorrect password");
            } else {
                showPasswordError("Login failed");
            }
        }
    }
    
    @FXML
    private void onRegister() {
    	application_audio.AudioManager.playSfx("chip_bet.wav");
        clearRegisterMessages();

        String username = reusernameField.getText().trim();
        String password = repasswordField.getText();
        String confirm  = reconfirmField.getText();

        if (username.isEmpty()) {
            showReUsernameError("Username is required");
            return;
        }

        if (password.isEmpty()) {
            showRePasswordError("Password is required");
            return;
        }

        if (password.length() < 6) {
            showRePasswordError("Password must be at least 6 characters");
            return;
        }

        if (confirm.isEmpty()) {
            showReConfirmError("Please confirm your password");
            return;
        }

        if (!password.equals(confirm)) {
            showReConfirmError("Passwords do not match");
            return;
        }

        try {
            authsys.handleRegister(username, password, confirm);
            showLogin();   // go back to login if success
        } catch (Exception e) {
            String msg = e.getMessage().toLowerCase();

            if (msg.contains("username")) {
                showReUsernameError("Username already exists");
            } else {
                showRePasswordError("Registration failed");
            }
        }
    }
    
    private User currentUser;
    private final AuthSystem authsys = new AuthSystem();
    
    private void showUsernameError(String msg) {
        usernamemessage.setText(msg);
        usernamemessage.setVisible(true);
    }

    private void showPasswordError(String msg) {
        passwordmessage.setText(msg);
        passwordmessage.setVisible(true);
    }

    private void showReUsernameError(String msg) {
        reusernamemessage.setText(msg);
        reusernamemessage.setVisible(true);
    }

    private void showRePasswordError(String msg) {
        repasswordmessage.setText(msg);
        repasswordmessage.setVisible(true);
    }

    private void showReConfirmError(String msg) {
        reconfirmmessage.setText(msg);
        reconfirmmessage.setVisible(true);
    }

    private void clearRegisterMessages() {
        reusernamemessage.setText(" ");
        repasswordmessage.setText(" ");
        reconfirmmessage.setText(" ");

        reusernamemessage.setVisible(false);
        repasswordmessage.setVisible(false);
        reconfirmmessage.setVisible(false);
    }
    
    private void clearMessages() {
        usernamemessage.setVisible(false);
        passwordmessage.setVisible(false);
    }
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
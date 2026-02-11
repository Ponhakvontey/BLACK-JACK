package application;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.Random;

import application_audio.AudioManager;
import application_login.User;
import application_login.UserRepositoryMySQL;

public class GameController {

    // Top labels
    @FXML private Label balanceLabel;
    @FXML private Label betLabel;
    @FXML private Button exitButton;
    @FXML private ImageView backgroundImage;
    @FXML private javafx.scene.layout.AnchorPane rootPane;
    
    // Betting screen
    @FXML private VBox bettingBox;
    @FXML private TextField betTextField;
    @FXML private Button placeBetButton;
    @FXML private Label bettingMessageLabel;
    @FXML private HBox topBar;

    
    // Game screen (your original)
    @FXML private BorderPane gameBox;
    @FXML private HBox dealerCardsBox;
    @FXML private HBox playerCardsBox;
    @FXML private Label dealerScoreLabel;
    @FXML private Label playerScoreLabel;
    @FXML private Label messageLabel;
    @FXML private Button hitButton;
    @FXML private Button standButton;
    @FXML private Button newGameButton;
    
    // New buttons
    @FXML private Button doubleButton;
    @FXML private Button splitButton;
    @FXML private Button surrenderButton;
    @FXML private Button insuranceButton;
    
    // Split hand
    @FXML private VBox splitHandSection;
    @FXML private VBox normalPlayerBox;
    @FXML private HBox splitHandsBox;
	@FXML private Label hand1ScoreLabel;
	@FXML private VBox hand1Box;

	@FXML private HBox hand1CardsBox;
    @FXML private HBox splitCardsBox;
    @FXML private Label splitScoreLabel;
    
  //statusbox
    @FXML private VBox statusbox;
    
    //guide(how to play)
    @FXML private VBox guideLayer;
    @FXML private VBox guideBox;
    
    //user status box
    @FXML private VBox Userstatusbox;
    @FXML private HBox guideBackBar;
    
    //label in status box
    @FXML private Label totalGamesValue;
    @FXML private Label winsValue;
    @FXML private Label lossesValue;
    @FXML private Label winRateValue;
    @FXML private Label chipsValue;
    @FXML private Label highestWinValue;
    
    // button in status box
    @FXML private Button playbutton;
    @FXML private Button settingsButton;
    @FXML private Button guidebutton;
    @FXML private Button Userstatusbutton;
    
 // profile overlays
    @FXML private VBox profileBox;          
    @FXML private VBox newUsernameBox;      
    @FXML private VBox newPasswordBox;      
    @FXML private VBox historyBox;  
    
    @FXML private Label prousername;
    @FXML private Label progmail;
    @FXML private Label moneywin;
    @FXML private Label moneyloss;
    
    @FXML private Button changepassbutton;
    @FXML private Button changeuserbutton;
    @FXML private Button viewgamebutton;

 // Change username form
    @FXML private TextField newUsernameField;
    @FXML private TextField emailTochangeusername;
    @FXML private PasswordField passTochangeusername;
    @FXML private Label usernameMessageLabel;
    @FXML private Button newusersavebutton;

 // Change password form
    @FXML private TextField newpassemailfield;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label passwordMessageLabel;


 // message labels
    @FXML private Label profileMessageLabel;

    @FXML private Label historyMessageLabel;

    @FXML private VBox historyListBox;  

    private ArrayList<Card> deck;
    private Random random = new Random();

    // Dealer
    private Card hiddenCard;
    private ArrayList<Card> dealerHand;
    private int dealerSum;
    private int dealerAceCount;

    // Player
    private ArrayList<Card> playerHand;
    private int playerSum;
    private int playerAceCount;

    // Split
    private boolean isSplit = false;
    private ArrayList<Card> splitHand;
    private int splitSum;
    private int splitAceCount;
    private boolean playingFirstHand = true;

    // Game state
    private boolean gameOver = false;
    private boolean surrendered = false;
    private boolean doubled = false;
    
    // Insurance
    private boolean tookInsurance = false;
    private double insuranceBet = 0.0;


    // Betting & User
    private double balance;
    private double currentBet = 0.0;
    private application_login.User user;
    
    

    // Round outcomes
    private static final int RESULT_BLACKJACK = 0;
    private static final int RESULT_WIN = 1;
    private static final int RESULT_PUSH = 2;
    private static final int RESULT_LOSE = 3;

    private static final String CARD_IMAGE_PATH = "/application/Cards/";
    private static final String CARD_BACK_IMAGE = "/application/Cards/BACK.png";

    public static javafx.scene.Parent gameRoot;
    
    private ImageView dealerHiddenView; 

    
    private Image loadCardImage(Card card) {
        String path = CARD_IMAGE_PATH + card.getImageFileName();
        var stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            System.out.println("Image not found: " + path);
            return null;
        }
        return new Image(stream);
    }

    private void showOverlay(Node target) {
        
    	if (statusbox != null) { statusbox.setVisible(false); }
        if (guideLayer != null) { guideLayer.setVisible(false);  }
        if (Userstatusbox != null) { Userstatusbox.setVisible(false);  }

        if (profileBox != null) { profileBox.setVisible(false); }
        if (newUsernameBox != null) { newUsernameBox.setVisible(false); }
        if (newPasswordBox != null) { newPasswordBox.setVisible(false); }
        if (historyBox != null) { historyBox.setVisible(false); }
        
        if (bettingBox != null) bettingBox.setVisible(false);
        if (gameBox != null) gameBox.setVisible(false);
        

        boolean showExit = (target == bettingBox) || (target == gameBox); 
        if (topBar != null) {
            topBar.setVisible(showExit);
            topBar.setManaged(showExit);
            if (showExit) topBar.toFront();
        }
        setExitVisible(showExit);
        // show selected
        if (target != null) {
            target.setVisible(true);
            target.toFront(); 
        }
        if (exitButton != null) exitButton.toFront();
        if (newGameButton != null) newGameButton.toFront();
        if (balanceLabel != null) balanceLabel.toFront();
        if (betLabel != null) betLabel.toFront();

    }

    private void revealThenDealerPlaysAndFinish() {
        // reveal hidden card with delay
        Image faceUp = loadCardImage(hiddenCard);
        dealerHiddenView.setImage(faceUp);

        dealerTurn();      // dealer draws more cards if needed
        gameCondition();   // show result + payout
    }

    private void setSplitMode(boolean split) {
        normalPlayerBox.setVisible(!split);
        splitHandsBox.setVisible(split);
        splitHandsBox.setVisible(split);
        hand1Box.setVisible(split);
        splitHandSection.setVisible(split);

    }

    private void redrawHand(HBox box, ArrayList<Card> hand) {
        box.getChildren().clear();
        for (Card c : hand) {
            addCardToDisplay(box, c, false);
        }
    }
    
    public void setUser(application_login.User user) {
        this.user = user;

        if (this.user != null) {
            this.balance = this.user.getCredits();   // credits now stored in users table
        } else {
            this.balance = 1000;
        }

        updateBalanceLabel();
        showBettingPhase();
        showOverlay(statusbox);
    }


    @FXML
    public void initialize() {
    	javafx.application.Platform.runLater(() -> gameRoot = rootPane.getScene().getRoot());
        // Bind background image
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        backgroundImage.setPreserveRatio(false); // fill background

        // Initial UI state
        showOverlay(statusbox);
        setExitVisible(false);

        
        
//        normalPlayerBox.setVisible(false); 
        splitHandSection.setVisible(false);
        
        
        if (statusbox != null) statusbox.managedProperty().bind(statusbox.visibleProperty());
        if (guideLayer != null) guideLayer.managedProperty().bind(guideLayer.visibleProperty());
        if (Userstatusbox != null) Userstatusbox.managedProperty().bind(Userstatusbox.visibleProperty());
        if (bettingBox != null) bettingBox.managedProperty().bind(bettingBox.visibleProperty());
        if (gameBox != null) gameBox.managedProperty().bind(gameBox.visibleProperty());

        if (profileBox != null) profileBox.managedProperty().bind(profileBox.visibleProperty());
        if (newUsernameBox != null) newUsernameBox.managedProperty().bind(newUsernameBox.visibleProperty());
        if (newPasswordBox != null) newPasswordBox.managedProperty().bind(newPasswordBox.visibleProperty());
        if (historyBox != null) historyBox.managedProperty().bind(historyBox.visibleProperty());

        
        if (splitHandSection != null) splitHandSection.managedProperty().bind(splitHandSection.visibleProperty());
        if (normalPlayerBox != null) normalPlayerBox.managedProperty().bind(normalPlayerBox.visibleProperty());
        if (splitHandsBox != null) splitHandsBox.managedProperty().bind(splitHandsBox.visibleProperty());

        setSplitMode(false);
        
        if (changepassbutton != null) changepassbutton.setOnAction(e -> onOpenChangePassword());
        if (changeuserbutton != null) changeuserbutton.setOnAction(e -> onOpenChangeUsername());
        if (viewgamebutton != null) viewgamebutton.setOnAction(e -> onOpenHistory());
    }
    
    @FXML
    public void onPlay() {
        AudioManager.playSfx("chip_bet.wav");
        showOverlay(bettingBox);
    }
    @FXML
    public void onOpenguide() {
        AudioManager.playSfx("chip_bet.wav");
        showOverlay(guideLayer);
    }
    public void onBackFromGuide() {
        AudioManager.playSfx("chip_bet.wav");
        showOverlay(statusbox);
    }
    @FXML
    private void refreshUserStatusLabels() {
        if (user == null) return;

        int total = user.getGamesPlayed();
        int wins = user.getWins();
        int losses = user.getLosses();
        double rate = (total == 0) ? 0 : (wins * 100.0 / total);

        totalGamesValue.setText(String.valueOf(total));
        winsValue.setText(String.valueOf(wins));
        lossesValue.setText(String.valueOf(losses));
        winRateValue.setText(String.format("%.0f%%", rate));

        chipsValue.setText(String.format("%.2f", balance)); // show current balance
        highestWinValue.setText(String.valueOf(user.getHighestWin()));
    }

    @FXML
    public void onBackFromUserStatus() {
        AudioManager.playSfx("chip_bet.wav");
        showOverlay(statusbox);
    }
    @FXML
    public void onOpenUserstatus() {
        AudioManager.playSfx("chip_bet.wav");
        showOverlay(Userstatusbox);
        refreshUserStatusLabels();
    }

    @FXML
    public void onOpenProfile() {
        AudioManager.playSfx("chip_bet.wav");
        refreshProfileLabels();
        showOverlay(profileBox);
    }
    
    @FXML
    public void onBackToProfile() {
        AudioManager.playSfx("chip_bet.wav");
        refreshProfileLabels();
        showOverlay(profileBox);
    }
    
    @FXML
    public void onBackFromProfileToStatus() {
        AudioManager.playSfx("chip_bet.wav");
        showOverlay(Userstatusbox);
    }
    
    @FXML
    public void onOpenChangeUsername() {
        AudioManager.playSfx("chip_bet.wav");
        clearUsernameForm();
        showOverlay(newUsernameBox);
    }
    
    @FXML
    public void onSaveNewUsername() {
        AudioManager.playSfx("chip_bet.wav");

        String newName = (newUsernameField != null) ? newUsernameField.getText().trim() : "";
        String email = (emailTochangeusername != null) ? emailTochangeusername.getText().trim().toLowerCase() : "";
        String pass = (passTochangeusername != null) ? passTochangeusername.getText() : "";

        if (newName.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            usernameMessageLabel.setText("Please fill all fields.");
            return;
        }

        User dbUser = repo.findUserByEmail(email);
        if (dbUser == null) {
            usernameMessageLabel.setText("Email not found.");
            return;
        }
        if (!dbUser.checkPassword(pass)) {
            usernameMessageLabel.setText("Wrong password.");
            return;
        }
        if (repo.usernameExists(newName)) {
            usernameMessageLabel.setText("Username already taken.");
            return;
        }

        boolean ok = repo.updateUsernameByEmail(email, newName);
        if (!ok) {
            usernameMessageLabel.setText("Update failed. Try again.");
            return;
        }

        // Update current session user (must have setUsername in User class)
        if (user != null && user.getEmail().equalsIgnoreCase(email)) {
            user.setUsername(newName);
        }

        usernameMessageLabel.setStyle("-fx-text-fill: #90EE90;");
        usernameMessageLabel.setText("Username updated!");
        refreshProfileLabels();
        try {
            gameRoot = rootPane.getScene().getRoot();

            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource("/application_login/homepage.fxml"));
            javafx.scene.Parent settingsRoot = loader.load();

            rootPane.getScene().setRoot(settingsRoot);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @FXML
    public void onOpenChangePassword() {
        AudioManager.playSfx("chip_bet.wav");
        clearPasswordForm();
        showOverlay(newPasswordBox);
    }

    @FXML
    public void onSaveNewPassword() {
        AudioManager.playSfx("chip_bet.wav");

        String email = (newpassemailfield != null) ? newpassemailfield.getText().trim().toLowerCase() : "";
        String oldPass = (oldPasswordField != null) ? oldPasswordField.getText() : "";
        String newPass = (newPasswordField != null) ? newPasswordField.getText() : "";
        String confirm = (confirmPasswordField != null) ? confirmPasswordField.getText() : "";

        if (email.isEmpty() || oldPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            passwordMessageLabel.setText("Please fill all fields.");
            return;
        }
        if (!newPass.equals(confirm)) {
            passwordMessageLabel.setText("New passwords do not match.");
            return;
        }
        if (newPass.length() < 4) {
            passwordMessageLabel.setText("Password too short.");
            return;
        }

        User dbUser = repo.findUserByEmail(email);
        if (dbUser == null) {
            passwordMessageLabel.setText("Email not found.");
            return;
        }
        if (!dbUser.checkPassword(oldPass)) {
            passwordMessageLabel.setText("Old password incorrect.");
            return;
        }

        boolean ok = repo.updatePasswordByEmail(email, newPass);
        if (!ok) {
            passwordMessageLabel.setText("Update failed. Try again.");
            return;
        }

        if (user != null && user.getEmail().equalsIgnoreCase(email)) {
            user.setPassword(newPass); // add setter in User
        }

        passwordMessageLabel.setStyle("-fx-text-fill: #90EE90;");
        passwordMessageLabel.setText("Password updated!");
        try {
            gameRoot = rootPane.getScene().getRoot();

            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource("/application_login/homepage.fxml"));
            javafx.scene.Parent settingsRoot = loader.load();

            rootPane.getScene().setRoot(settingsRoot);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @FXML
    public void onOpenHistory() {
        AudioManager.playSfx("chip_bet.wav");
        showOverlay(historyBox);
        loadHistoryUI();
    }
    
    private void loadHistoryUI() {
        if (historyListBox == null || user == null) return;

        historyListBox.getChildren().clear();

        var rows = repo.getRecentHistory(user.getId(), 15);

        if (rows.isEmpty()) {
            Label empty = new Label("No history yet.");
            empty.setStyle("-fx-text-fill: #D7B8A4; -fx-font-size: 14;");
            historyListBox.getChildren().add(empty);
            return;
        }

        for (var r : rows) {
            HBox row = new HBox(20);
            row.setAlignment(javafx.geometry.Pos.CENTER);
            row.setStyle("-fx-padding: 6 0;");

            Label date = new Label(r.playedAt);
            date.setPrefWidth(200);
            date.setStyle("-fx-text-fill: #CFB2AB; -fx-font-size: 12;");

            Label amount = new Label(String.format("$%.2f", r.profit));
            amount.setPrefWidth(200);
            amount.setStyle(
                "-fx-text-fill: " + (r.profit >= 0 ? "#90EE90" : "#FF6B6B") +
                "; -fx-font-size: 12; -fx-font-weight: bold;"
            );

            row.getChildren().addAll(date, amount);
            historyListBox.getChildren().add(row);
        }
    }


    @FXML
    public void onLogout() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        

        try {
            gameRoot = rootPane.getScene().getRoot();

            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource("/application_login/homepage.fxml"));
            javafx.scene.Parent settingsRoot = loader.load();

            rootPane.getScene().setRoot(settingsRoot);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onPlaceBet() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        String betText = betTextField.getText().trim();
        try {
            double bet = Double.parseDouble(betText);
            if (bet <= 0) {
                bettingMessageLabel.setText("Bet must be greater than 0!");
                return;
            }
            if (bet > balance) {
                bettingMessageLabel.setText("Not enough balance!");
                return;
            }
            currentBet = bet;
            balance -= currentBet;
            updateBetLabel();
            updateBalanceLabel();
            if (user != null) {
            	autosaveCredit();

            }

            startNewGame();
        } catch (NumberFormatException e) {
            bettingMessageLabel.setText("Please enter a valid bet amount!");
        }
    }

    @FXML
    public void onHit() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        if (gameOver) return;
        
        ArrayList<Card> hand;
        HBox box;
        
        if (!isSplit) {
            hand = playerHand;
            box = playerCardsBox;
        } else if(playingFirstHand){
            hand = playerHand;
            box = hand1CardsBox;
        } else {
        	hand = splitHand;
            box = splitCardsBox;
        }

        Card card = deck.remove(deck.size() - 1);
        hand.add(card);
        
        if (isSplit && !playingFirstHand) {
            splitSum += card.getValue();
            if (card.isAce()) splitAceCount++;
            while (splitSum > 21 && splitAceCount > 0) {
                splitSum -= 10;
                splitAceCount--;
            }
        } else {
            playerSum += card.getValue();
            if (card.isAce()) playerAceCount++;
            while (playerSum > 21 && playerAceCount > 0) {
                playerSum -= 10;
                playerAceCount--;
            }
        }

        addCardToDisplay(box, card, false);
        updateScoreLabels();

        if (doubled) {
            onStand();
            return;
        }

        int currentSum = (isSplit && !playingFirstHand) ? splitSum : playerSum;
        if (currentSum > 21) {
            if (isSplit && playingFirstHand) {
                playingFirstHand = false;
                messageLabel.setText("Hand 1 bust! Playing Hand 2...");
            } else {
            	revealThenDealerPlaysAndFinish();
            }
        }
    }

    @FXML
    public void onStand() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        if (gameOver) return;
        
        if (isSplit && playingFirstHand) {
            playingFirstHand = false;
            messageLabel.setText("Now playing Hand 2...");
            return;
        }
        
        revealThenDealerPlaysAndFinish();

    }
    
    @FXML
    public void onInsurance() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        if (gameOver) {
            messageLabel.setText("Round is over.");
            return;
        }

        if (tookInsurance) {
            messageLabel.setText("Insurance already taken.");
            return;
        }

        // Insurance only before any action (player must still have 2 cards)
        if (playerHand == null || playerHand.size() != 2) {
            messageLabel.setText("Insurance only available before you hit/stand.");
            return;
        }

        if (!dealerUpcardAllowsInsurance()) {
            messageLabel.setText("Insurance only available when dealer shows Ace or 10.");
            return;
        }

        double maxInsurance = currentBet / 2.0;

        if (balance < maxInsurance) {
            messageLabel.setText("Not enough balance for insurance.");
            return;
        }

        // Take insurance
        tookInsurance = true;
        insuranceBet = maxInsurance;
        balance -= insuranceBet;
        updateBalanceLabel();
        if (user != null) {
        	autosaveCredit();
        }

        messageLabel.setText("Insurance taken: $" + String.format("%.2f", insuranceBet));
    }

    @FXML
    public void onDouble() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        if (gameOver || doubled) return;

        if (playerHand.size() != 2 || isSplit) {
            messageLabel.setText("Can only double on first 2 cards!");
            return;
        }
        if (balance < currentBet) {
            messageLabel.setText("Not enough balance to double!");
            return;
        }

        balance -= currentBet;
        currentBet *= 2;
        doubled = true;
        updateBalanceLabel();
        updateBetLabel();
        if (user != null) {
        	autosaveCredit();

        }  
        onHit();
    }

    @FXML
    public void onSplit() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        if (gameOver || isSplit) return;
        if (playerHand.size() != 2) return;
        splitHandSection.setVisible(true);
        
        
        Card c1 = playerHand.get(0);
        Card c2 = playerHand.get(1);
        if (!c1.getRank().equals(c2.getRank())) {
            messageLabel.setText("Cards must match to split!");
            return;
        }

        if (balance < currentBet) {
            messageLabel.setText("Not enough balance to split!");
            return;
        }

        balance -= currentBet;
        updateBalanceLabel();
        if ( user != null) {
        	autosaveCredit();

        }

        
        isSplit = true;
        playingFirstHand = true;

        splitHand = new ArrayList<>();
        splitCardsBox.getChildren().clear();
        Card second = playerHand.remove(1);
        splitHand.add(second);
        splitSum = second.getValue();
        splitAceCount = second.isAce() ? 1 : 0;

        playerSum = playerHand.get(0).getValue();
        playerAceCount = playerHand.get(0).isAce() ? 1 : 0;
        playerCardsBox.getChildren().clear();

        Card card1 = deck.remove(deck.size() - 1);
        playerHand.add(card1);
        playerSum += card1.getValue();
        if (card1.isAce()) playerAceCount++;

        Card card2 = deck.remove(deck.size() - 1);
        splitHand.add(card2);
        splitSum += card2.getValue();
        if (card2.isAce()) splitAceCount++;
        
        setSplitMode(true);
        hand1CardsBox.setVisible(true);
        splitCardsBox.setVisible(true);

        playerCardsBox.getChildren().clear();
        redrawHand(hand1CardsBox, playerHand);
        redrawHand(splitCardsBox, splitHand);
        updateScoreLabels();
        
        messageLabel.setText("Playing Hand 1. Hit or Stand?");
        setSplitMode(true);
        redrawHand(hand1CardsBox, playerHand);
        redrawHand(splitCardsBox, splitHand);


    }

    @FXML
    public void onSurrender() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        if (gameOver || surrendered) return;
        if (playerHand.size() != 2) {
            messageLabel.setText("Can only surrender with first 2 cards!");
            return;
        }

        surrendered = true;
        double returnAmount = currentBet * 0.5;
        balance += returnAmount;
        gameOver = true;
        messageLabel.setText("Surrendered. $" + String.format("%.2f", returnAmount) + " returned");
        disableButtons();
        newGameButton.setVisible(true);
        newGameButton.setDisable(false);
        updateBalanceLabel();
        autosaveCredit();

    }

    @FXML
    public void onNewGame() {
        AudioManager.playSfx("chip_bet.wav");

        if (balance <= 0) {
            messageLabel.setText("Out of credits! Game Over.");
            newGameButton.setDisable(true);  // Disable New Game since they have no credits
            return;
        }

        // Show betting phase again
        showBettingPhase();
    }


    @FXML
    public void onExit() {
        AudioManager.playSfx("chip_bet.wav");
        autosaveCredit();
        refreshUserStatusLabels();
        showOverlay(statusbox);
    }


    @FXML
    private void onOpenSettings() {
        application_audio.AudioManager.playSfx("chip_bet.wav");
        try {
        	AudioManager.initializeAudio();

            // save current screen before switching
            gameRoot = rootPane.getScene().getRoot();

            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource("/application_audio/AudioSettings.fxml"));
            javafx.scene.Parent settingsRoot = loader.load();

            rootPane.getScene().setRoot(settingsRoot);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBettingPhase() {
        gameBox.setVisible(false);
        showOverlay(bettingBox);
        newGameButton.setVisible(false);
        
        dealerCardsBox.getChildren().clear();
        playerCardsBox.getChildren().clear();
        splitCardsBox.getChildren().clear();
        splitHandSection.setVisible(false);
        
        bettingMessageLabel.setText("");
        betTextField.clear();
        betTextField.requestFocus();
        
        betLabel.setText("Bet: $0.00");
        currentBet = 0.0;
    }

    private void startNewGame() {
        gameOver = false;
        surrendered = false;
        doubled = false;
        isSplit = false;
        playingFirstHand = true;
        tookInsurance = false;
        insuranceBet = 0.0;
        insuranceButton.setVisible(true);
      
        showOverlay(gameBox);

        dealerCardsBox.getChildren().clear();
        playerCardsBox.getChildren().clear();
        splitCardsBox.getChildren().clear();
        splitHandSection.setVisible(false);

        buildDeck();
        shuffleDeck();

        // Dealer
        dealerHand = new ArrayList<>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card visible = deck.remove(deck.size() - 1);
        dealerHand.add(visible);
        dealerSum += visible.getValue();
        dealerAceCount += visible.isAce() ? 1 : 0;

        dealerHiddenView = addCardToDisplay(dealerCardsBox, null, true); // store BACK card view
        addCardToDisplay(dealerCardsBox, visible, false);

        // Player
        playerHand = new ArrayList<>();
        playerSum = 0;
        playerAceCount = 0;

        for (int i = 0; i < 2; i++) {
            Card c = deck.remove(deck.size() - 1);
            playerHand.add(c);
            playerSum += c.getValue();
            playerAceCount += c.isAce() ? 1 : 0;
            addCardToDisplay(playerCardsBox, c, false);
        }

        updateScoreLabels();
        messageLabel.setText("Hit or Stand?");
        
        setSplitMode(false);
        hand1CardsBox.getChildren().clear();
        splitCardsBox.getChildren().clear();
        splitHandSection.setVisible(false);
        

        hitButton.setDisable(false);
        standButton.setDisable(false);
        doubleButton.setDisable(false);
        splitButton.setDisable(false);
        surrenderButton.setDisable(false);
        newGameButton.setVisible(true);
        newGameButton.setDisable(false);
        insuranceButton.setVisible(true);
        insuranceButton.setDisable(false);
    }

    private void dealerTurn() {
    	dealerScoreLabel.setText("Dealer: " + dealerSum);

        while (dealerSum < 17) {
            Card c = deck.remove(deck.size() - 1);
            dealerHand.add(c);
            dealerSum += c.getValue();
            dealerAceCount += c.isAce() ? 1 : 0;

            while (dealerSum > 21 && dealerAceCount > 0) {
                dealerSum -= 10;
                dealerAceCount--;
            }

            addCardToDisplay(dealerCardsBox, c, false);
        }

        dealerScoreLabel.setText("Dealer: " + dealerSum);
    }

    private void gameCondition() {
        double insuranceReturn = payoutInsurance();
        balance += insuranceReturn;

        double totalReturn;

        if (!isSplit) {
            int result = determineOutcome(playerHand, playerSum, playerAceCount);

            if (result == RESULT_BLACKJACK || result == RESULT_WIN) {
                application_audio.AudioManager.playSfx("win_sound.wav");
            } else if (result == RESULT_LOSE) {
                application_audio.AudioManager.playSfx("lose_sound.wav");
            }

            totalReturn = payoutMain(currentBet, result);
            balance += totalReturn;

            double profit = totalReturn - currentBet;

            boolean wonRound = (result == RESULT_WIN || result == RESULT_BLACKJACK);
            boolean lostRound = (result == RESULT_LOSE);

            //  history
            if (user != null) {
                repo.insertHistory(user.getId(), currentBet, profit, resultMessage(result));
            }

            
            applyRoundStatsAndSave(wonRound, lostRound, profit);

            String msg = resultMessage(result) + " | Return: $" + String.format("%.2f", totalReturn);
            if (insuranceReturn > 0) {
                msg += " | Insurance: $" + String.format("%.2f", insuranceReturn);
            }
            messageLabel.setText(msg);

        } else {
            int result1 = determineOutcome(playerHand, playerSum, playerAceCount);
            int result2 = determineOutcome(splitHand, splitSum, splitAceCount);

            if (result1 == RESULT_WIN || result1 == RESULT_BLACKJACK || result2 == RESULT_WIN || result2 == RESULT_BLACKJACK) {
                application_audio.AudioManager.playSfx("win_sound.wav");
            } else if (result1 == RESULT_LOSE && result2 == RESULT_LOSE) {
                application_audio.AudioManager.playSfx("lose_sound.wav");
            }

            double return1 = payoutMain(currentBet, result1);
            double return2 = payoutMain(currentBet, result2);

            totalReturn = return1 + return2;
            balance += totalReturn;

            double profit = totalReturn - (currentBet * 2.0);

            boolean wonRound = (result1 == RESULT_WIN || result1 == RESULT_BLACKJACK ||
                                result2 == RESULT_WIN || result2 == RESULT_BLACKJACK);

            boolean lostRound = (result1 == RESULT_LOSE && result2 == RESULT_LOSE);

            // history
            if (user != null) {
                repo.insertHistory(user.getId(), currentBet * 2.0, profit, "SPLIT");
            }

           
            applyRoundStatsAndSave(wonRound, lostRound, profit);

            String msg = "H1: " + resultMessage(result1) + " | H2: " + resultMessage(result2)
                    + " | $" + String.format("%.2f", totalReturn);

            if (insuranceReturn > 0) {
                msg += " | Insurance: $" + String.format("%.2f", insuranceReturn);
            }
            messageLabel.setText(msg);
        }

        gameOver = true;
        disableButtons();
        newGameButton.setVisible(true);
        newGameButton.setDisable(false);
        updateBalanceLabel();
    }

    private int determineOutcome(ArrayList<Card> hand, int sum, int aceCount) {
        int p = bestTotal(sum, aceCount);
        int d = bestTotal(dealerSum, dealerAceCount);

        boolean playerBJ = isBlackjack(hand, sum, aceCount);
        boolean dealerBJ = dealerHasBlackjack();

        if (playerBJ && dealerBJ) return RESULT_PUSH;
        if (playerBJ) return RESULT_BLACKJACK;
        if (dealerBJ) return RESULT_LOSE;

        if (p > 21) return RESULT_LOSE;
        if (d > 21) return RESULT_WIN;
        if (p > d) return RESULT_WIN;
        if (p < d) return RESULT_LOSE;
        return RESULT_PUSH;
    }

    private double payoutMain(double bet, int result) {
        if (result == RESULT_BLACKJACK) return bet * 2.5;
        if (result == RESULT_WIN) return bet * 2.0;
        if (result == RESULT_PUSH) return bet;
        return 0.0;
    }

    private String resultMessage(int result) {
        if (result == RESULT_BLACKJACK) return "BLACKJACK!";
        if (result == RESULT_WIN) return "WIN";
        if (result == RESULT_PUSH) return "PUSH";
        return "LOSE";
    }




    private void disableButtons() {
        hitButton.setDisable(true);
        standButton.setDisable(true);
        doubleButton.setDisable(true);
        splitButton.setDisable(true);
        surrenderButton.setDisable(true);
        insuranceButton.setDisable(true);
    }

    private void setExitVisible(boolean visible) {
        if (exitButton != null) {
            exitButton.setVisible(visible);
            exitButton.setManaged(visible);
        }
    }

    private int bestTotal(int sum, int aceCount) {
        while (sum > 21 && aceCount > 0) {
            sum -= 10;
            aceCount--;
        }
        return sum;
    }

    private boolean isBlackjack(ArrayList<Card> hand, int sum, int aceCount) {
        return hand.size() == 2 && bestTotal(sum, aceCount) == 21;
    }

    private boolean dealerHasBlackjack() {
        int sum = hiddenCard.getValue() + dealerHand.get(0).getValue();
        int aces = (hiddenCard.isAce() ? 1 : 0) + (dealerHand.get(0).isAce() ? 1 : 0);
        return bestTotal(sum, aces) == 21;
    }
    
    private boolean dealerUpcardAllowsInsurance() {
        if (dealerHand == null || dealerHand.isEmpty()) return false;
        Card upcard = dealerHand.get(0);
        return upcard.isAce() || upcard.getValue() == 10;
    }

    private double payoutInsurance() {
        if (!tookInsurance) return 0.0;
        if (dealerHasBlackjack()) return insuranceBet * 3.0; // total return
        return 0.0;
    }

    private void buildDeck() {
        deck = new ArrayList<>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "S", "H", "D"};

        for (String t : types)
            for (String v : values)
                deck.add(new Card(v, t));
    }

    private void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card temp = deck.get(i);
            deck.set(i, deck.get(j));
            deck.set(j, temp);
        }
    }

    private ImageView addCardToDisplay(HBox box, Card card, boolean hidden) {
        ImageView view = new ImageView();
        view.setFitWidth(100);
        view.setFitHeight(145);
        view.setPreserveRatio(true);

        String path;
        if (hidden) {
            path = CARD_BACK_IMAGE;
        } else {
            path = CARD_IMAGE_PATH + card.getImageFileName();
        }

        var url = getClass().getResource(path);
        if (url == null) {
            System.out.println("Image not found: " + path);
            return null;
        }

        view.setImage(new Image(url.toExternalForm()));
        box.getChildren().add(view);
        return view;
    }

    private void updateScoreLabels() {
        dealerScoreLabel.setText("Dealer: ?");
        if (!isSplit) {
        	playerScoreLabel.setText("Player: " + playerSum);
        }else {
        	hand1ScoreLabel.setText("Hand 1: " +playerSum);
        	splitScoreLabel.setText("Hand 2: " + splitSum);
        	
        }
    }

    private void updateBalanceLabel() {
        balanceLabel.setText("Balance: $" + String.format("%.2f", balance));
    }

    private void updateBetLabel() {
        betLabel.setText("Bet: $" + String.format("%.2f", currentBet));
    }

    class Card {
        private String value;
        private String type;

        public Card(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public int getValue() {
            if ("AJQK".contains(value)) {
                if (value.equals("A"))
                    return 11;
                return 10;
            }
            return Integer.parseInt(value);
        }

        public boolean isAce() {
            return value.equals("A");
        }

        public String getRank() {
            return value;
        }

        public String getImageFileName() {
            return value + "-" + type + ".png";
        }
    }
    private void refreshProfileLabels() {
        if (user == null) return;

        if (prousername != null) prousername.setText(user.getUsername());
        if (progmail != null) progmail.setText(user.getEmail());

        // If you don’t have real profit/loss tracking yet, show placeholders:
        if (moneywin != null) moneywin.setText(String.format("%.2f", user.getTotalMoneyWon()));
        if (moneyloss != null) moneyloss.setText(String.format("%.2f", user.getTotalMoneyLost()));

    }
    private void clearUsernameForm() {
        if (newUsernameField != null) newUsernameField.clear();
        if (emailTochangeusername != null) emailTochangeusername.clear();
        if (passTochangeusername != null) passTochangeusername.clear();

        if (usernameMessageLabel != null) {
            usernameMessageLabel.setStyle("-fx-text-fill: #CFB2AB;");
            usernameMessageLabel.setText("");
        }
    }

    private void clearPasswordForm() {
        if (newpassemailfield != null) newpassemailfield.clear();
        if (oldPasswordField != null) oldPasswordField.clear();
        if (newPasswordField != null) newPasswordField.clear();
        if (confirmPasswordField != null) confirmPasswordField.clear();

        if (passwordMessageLabel != null) {
            passwordMessageLabel.setStyle("-fx-text-fill: #CFB2AB;");
            passwordMessageLabel.setText("");
        }
    }

    private void applyRoundStatsAndSave(boolean wonRound, boolean lostRound, double profit) {
        if (user == null) return;

        user.setGamesPlayed(user.getGamesPlayed() + 1);

        if (wonRound) user.setWins(user.getWins() + 1);
        if (lostRound) user.setLosses(user.getLosses() + 1);

        // totals
        if (profit > 0) user.setTotalMoneyWon(user.getTotalMoneyWon() + profit);
        if (profit < 0) user.setTotalMoneyLost(user.getTotalMoneyLost() + (-profit));

        // highest win based on profit (you can change to payout if you want)
        int profitInt = (int) Math.round(profit);
        if (profitInt > user.getHighestWin()) user.setHighestWin(profitInt);

        // credits
        user.setCredits(balance);

        // persist all
        repo.updateUserByEmail(user);
    }

    UserRepositoryMySQL repo = new UserRepositoryMySQL();
    private void autosaveCredit() {
        if (user == null) return;
        user.setCredits(balance);
        repo.updateUserByEmail(user);
    }
}
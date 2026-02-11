package application_login;

import java.util.Objects;

public class User{
    // Basic account info
    private String username;
    private final String email;
    private String password;
    // Primary key
    private int id;
    // Game stats
    private double credits;
    private int gamesPlayed;
    private int wins;
    private int losses;
    private int highestWin;
    private double totalMoneyWon;
    private double totalMoneyLost;

    // Preferences
    private boolean musicEnabled;
    private String theme;
    
    // Constructor for new registrations
    public User(String username, String email, String password) {
        this.username = username.trim();
        this.email = email.trim().toLowerCase();
        this.password = password; 
        this.credits = 1000;
        this.musicEnabled = true;
        this.theme = "classic";
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.highestWin = 0;
        this.totalMoneyWon = 0;
        this.totalMoneyLost = 0;
        this.musicEnabled = true;
        this.theme = "classic";
    }

    public int getId() {
        return id;
    }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; } 
    public double getCredits() { return credits; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getHighestWin() { return highestWin; }
    public double getTotalMoneyWon() { return totalMoneyWon; }
    public double getTotalMoneyLost() { return totalMoneyLost; }
    public String getTheme() { return theme; }
    public boolean isMusicEnabled() { return musicEnabled; }
    
    // Setters (for loading from CSV)
    public void setId(int id) {
        this.id = id;
    }
    public void setCredits(double credits) { 
        this.credits = credits; 
    }
    public void setGamesPlayed(int gamesPlayed) { 
        this.gamesPlayed = gamesPlayed; 
    }
    public void setWins(int wins) { 
        this.wins = wins; 
    }
    public void setLosses(int losses) { 
        this.losses = losses; 
    }
    public void setTotalMoneyWon(double totalMoneyWon) {
        this.totalMoneyWon = totalMoneyWon; 
    }
    public void setTotalMoneyLost(double totalMoneyLost) { 
        this.totalMoneyLost = totalMoneyLost; 
    }
    public void setHighestWin(int highestWin) { 
        this.highestWin = highestWin; 
    }
    public void setTheme(String theme) { 
        this.theme = theme; 
    }
    public void setMusicEnabled(boolean musicEnabled) { 
        this.musicEnabled = musicEnabled; 
    }
    
    // Business methods
    public void addCredits(int amount) {
        if (amount > 0) {
            credits += amount;
        }
    }
    
    public boolean subtractCredits(int amount) {
        if (amount > 0 && amount <= credits) {
            credits -= amount;
            return true;
        }
        return false;
    }
    
    public void recordWin(int bet) {
    	wins++;
        gamesPlayed++;
        int winAmount = bet * 2;
        addCredits(winAmount);

        if (winAmount > highestWin) {
            highestWin = winAmount;
        }
    }
    
    public void recordLoss(int bet) {
        losses++;
        gamesPlayed++;
        subtractCredits(bet);
        totalMoneyLost += bet;
    }
 
    public void recordTie(int bet) {
        gamesPlayed++;
        // Just get your bet back - no credit change
    }
    
    public void toggleMusic() {
        musicEnabled = !musicEnabled;
    }
    
    // Password check - plain text comparison (NOT secure for production!)
    public boolean checkPassword(String attempt) {
        return password.equals(attempt);
    }
    
    // Helper to calculate win rate
    public double getWinRate() {
        return gamesPlayed > 0 ? (double) wins / gamesPlayed * 100 : 0.0;
    }
    
    // Helper to get total earnings
    public double getTotalEarnings() {
        return credits - 1000; // Subtract starting credits
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username.equals(user.username) && email.equals(user.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
    
    @Override
    public String toString() {
        return String.format("User{username='%s', credits=%.2f, games=%d, wins=%d, losses=%d}",
                username, credits, gamesPlayed, wins, losses);

    }
    public void setUsername(String username) {
        this.username = username.trim();
    }
    public void setPassword(String password) {
        this.password = password;
    }	
}
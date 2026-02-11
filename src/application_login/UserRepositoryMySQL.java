package application_login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserRepositoryMySQL {

	public User findUser(String username) {
        String sql = "SELECT id, username, email, password, credits, games_Played, wins, losses, highest_Win,theme, music_Enabled, total_money_won, total_money_lost FROM users WHERE username = ?";

        try (Connection c = DB.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return mapRowToUser(rs);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean usernameExists(String username) {
        return findUser(username) != null;
    }

    public void addUser(User user) {
        String sql = """
            INSERT INTO users(username, email, password,
                              credits, games_Played, wins, losses, highest_Win,
                              theme, music_Enabled, total_money_won, total_money_lost)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DB.getConnection();
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

               ps.setString(1, user.getUsername());
               ps.setString(2, user.getEmail());
               ps.setString(3, user.getPassword());

               ps.setDouble(4, user.getCredits());
               ps.setInt(5, user.getGamesPlayed());
               ps.setInt(6, user.getWins());
               ps.setInt(7, user.getLosses());
               ps.setInt(8, user.getHighestWin());

               ps.setString(9, user.getTheme());
               ps.setBoolean(10, user.isMusicEnabled());

               ps.setDouble(11, user.getTotalMoneyWon());
               ps.setDouble(12, user.getTotalMoneyLost());

               ps.executeUpdate();

               try (ResultSet keys = ps.getGeneratedKeys()) {
                   if (keys.next()) user.setId(keys.getInt(1));
               }

               System.out.println("User inserted into MySQL");

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    
    public boolean updateUserByEmail(User user) {
        String sql = """
            UPDATE users
            SET username=?,
                password=?,
                credits=?,
                games_Played=?,
                wins=?,
                losses=?,
                highest_Win=?,
                theme=?,
                music_Enabled=?,
                total_money_won=?,
                total_money_lost=?
            WHERE email=?
        """;

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());

            ps.setDouble(3, user.getCredits());
            ps.setInt(4, user.getGamesPlayed());
            ps.setInt(5, user.getWins());
            ps.setInt(6, user.getLosses());
            ps.setInt(7, user.getHighestWin());

            ps.setString(8, user.getTheme());
            ps.setBoolean(9, user.isMusicEnabled());

            ps.setDouble(10, user.getTotalMoneyWon());
            ps.setDouble(11, user.getTotalMoneyLost());

            ps.setString(12, user.getEmail().toLowerCase());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateUsernameByEmail(String email, String newUsername) {
        String sql = "UPDATE users SET username = ? WHERE email = ?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newUsername);
            ps.setString(2, email.toLowerCase());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, email.toLowerCase());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User findUserByEmail(String email) {
    	String sql = """
                SELECT id, username, email, password,
                       credits, games_Played, wins, losses, highest_Win,
                       theme, music_Enabled, total_money_won, total_money_lost
                FROM users
                WHERE email = ?
            """;
    	 try (Connection c = DB.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setString(1, email.toLowerCase().trim());
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) return null;

                return mapRowToUser(rs);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    public void insertHistory(int userId, double bet, double profit, String result) {
        String sql = "INSERT INTO game_history(user_id, bet, profit, result) VALUES (?, ?, ?, ?)";

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDouble(2, bet);
            ps.setDouble(3, profit);
            ps.setString(4, result);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static class HistoryRow {
        public final String playedAt;
        public final double profit;
        public HistoryRow(String playedAt, double profit) {
            this.playedAt = playedAt;
            this.profit = profit;
        }
    }

    public java.util.List<HistoryRow> getRecentHistory(int userId, int limit) {
        String sql = """
            SELECT played_at, profit
            FROM game_history
            WHERE user_id = ?
            ORDER BY played_at DESC
            LIMIT ?
        """;

        java.util.List<HistoryRow> rows = new java.util.ArrayList<>();

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String t = rs.getTimestamp("played_at").toString();
                double p = rs.getDouble("profit");
                rows.add(new HistoryRow(t, p));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
    
    
    private User mapRowToUser(ResultSet rs) throws Exception {
        User u = new User(
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password")
        );

        u.setId(rs.getInt("id"));

        u.setCredits(rs.getDouble("credits"));
        u.setGamesPlayed(rs.getInt("games_Played"));
        u.setWins(rs.getInt("wins"));
        u.setLosses(rs.getInt("losses"));
        u.setHighestWin(rs.getInt("highest_Win"));

        u.setTheme(rs.getString("theme"));
        u.setMusicEnabled(rs.getBoolean("music_Enabled"));

        u.setTotalMoneyWon(rs.getDouble("total_money_won"));
        u.setTotalMoneyLost(rs.getDouble("total_money_lost"));

        return u;
    }
}



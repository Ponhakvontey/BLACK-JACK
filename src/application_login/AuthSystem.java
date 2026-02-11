package application_login;

public class AuthSystem {
    private final UserRepositoryMySQL userRepo = new UserRepositoryMySQL();
    
    public User handleLogin(String username, String password) throws Exception {

        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("Please enter username and password");
        }

        User user = userRepo.findUser(username);
        if (user == null) {
            throw new Exception("User not found");
        }

        if (!user.checkPassword(password)) {
            throw new Exception("Wrong password");
        }

        return user;
    }
    

    public User handleRegister(String username, String password, String confirm)
            throws Exception {

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            throw new Exception("Please fill all fields");
        }

        if (password.length() < 6) {
            throw new Exception("Password must be at least 6 characters");
        }

        if (!password.equals(confirm)) {
            throw new Exception("Passwords do not match");
        }

        if (userRepo.usernameExists(username)) {
            throw new Exception("Username already exists");
        }

        String email = username.toLowerCase() + "@blackjack.com";
        User newUser = new User(username, email, password);

        userRepo.addUser(newUser);
        return newUser;
    }
}
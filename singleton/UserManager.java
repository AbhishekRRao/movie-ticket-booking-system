package singleton;

import enums.UserType;
import factory.UserFactory;
import model.User;

import java.util.HashMap;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles registration and login operations.
 */
public class UserManager {

    private static volatile UserManager instance;

    private final Map<String, User> usersByEmail;
    private final DBConnection dbConnection;
    private int userCounter;

    private UserManager() {
        this.usersByEmail = new HashMap<>();
        this.dbConnection = DBConnection.getInstance();
        this.userCounter = 2000;
        loadUsers();
        ensureDefaultAdmin();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    public User registerCustomer(String name, String email, String password) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail.isEmpty() || password == null || password.isEmpty() || name == null || name.isEmpty()) {
            return null;
        }
        if (usersByEmail.containsKey(normalizedEmail)) {
            return null;
        }

        User user = UserFactory.createUser(UserType.CUSTOMER, ++userCounter, name.trim(), normalizedEmail, password);
        usersByEmail.put(normalizedEmail, user);
        persistUser(user);
        dbConnection.save("REGISTERED_USER: " + user.getEmail());
        return user;
    }

    public User login(String email, String password) {
        String normalizedEmail = normalizeEmail(email);
        User user = usersByEmail.get(normalizedEmail);
        if (user == null) {
            return null;
        }
        if (!user.authenticate(password)) {
            return null;
        }

        dbConnection.save("LOGIN_SUCCESS: " + user.getEmail());
        return user;
    }

    public User getUserByEmail(String email) {
        return usersByEmail.get(normalizeEmail(email));
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return "";
        }
        return email.trim().toLowerCase();
    }

    private void loadUsers() {
        String sql = "SELECT user_id, name, email, password, user_type FROM users";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                UserType userType = UserType.valueOf(rs.getString("user_type"));

                User user = UserFactory.createUser(userType, userId, name, email, password);
                if (user != null) {
                    usersByEmail.put(normalizeEmail(email), user);
                    userCounter = Math.max(userCounter, userId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load users", e);
        }
    }

    private void ensureDefaultAdmin() {
        String adminEmail = "admin@mtbs.com";
        if (usersByEmail.containsKey(adminEmail)) {
            return;
        }
        User admin = UserFactory.createUser(UserType.ADMIN, ++userCounter, "System Admin", adminEmail, "admin123");
        usersByEmail.put(adminEmail, admin);
        persistUser(admin);
    }

    private void persistUser(User user) {
        String sql = "INSERT OR REPLACE INTO users(user_id, name, email, password, user_type) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, user.getUserId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getUserType().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }
}

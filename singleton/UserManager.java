package singleton;

import enums.UserType;
import factory.UserFactory;
import model.User;

import java.util.HashMap;
import java.util.Map;

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

        // Seed one admin account for demo/testing.
        User admin = UserFactory.createUser(UserType.ADMIN, ++userCounter, "System Admin", "admin@mtbs.com", "admin123");
        usersByEmail.put(admin.getEmail().toLowerCase(), admin);
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
        dbConnection.save("REGISTERED_USER: " + user);
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
}

package model;

import enums.UserType;

/**
 * Base user model.
 * Subclasses represent concrete user roles.
 */
public abstract class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private UserType userType;

    protected User(int userId, String name, String email, String password, UserType userType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public boolean authenticate(String rawPassword) {
        return password != null && password.equals(rawPassword);
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserType getUserType() { return userType; }

    @Override
    public String toString() {
        return "User[ID:" + userId + " | Name:" + name + " | Email:" + email + " | Type:" + userType + "]";
    }
}

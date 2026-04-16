package model;

import enums.UserType;

/**
 * Concrete admin user.
 */
public class AdminUser extends User {

    public AdminUser(int userId, String name, String email, String password) {
        super(userId, name, email, password, UserType.ADMIN);
    }
}

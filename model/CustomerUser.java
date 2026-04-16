package model;

import enums.UserType;

/**
 * Concrete customer user.
 */
public class CustomerUser extends User {

    public CustomerUser(int userId, String name, String email, String password) {
        super(userId, name, email, password, UserType.CUSTOMER);
    }
}

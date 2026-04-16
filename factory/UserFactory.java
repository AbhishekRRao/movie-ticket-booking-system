package factory;

import enums.UserType;
import model.AdminUser;
import model.CustomerUser;
import model.User;

/**
 * Factory Pattern: creates concrete user types based on role.
 */
public final class UserFactory {

    private UserFactory() {
    }

    public static User createUser(UserType userType, int userId, String name, String email, String password) {
        switch (userType) {
            case CUSTOMER:
                return new CustomerUser(userId, name, email, password);
            case ADMIN:
                return new AdminUser(userId, name, email, password);
            default:
                return null;
        }
    }
}

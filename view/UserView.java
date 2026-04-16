package view;

import model.Movie;
import model.User;

import java.util.List;

/**
 * View for user/auth and movie search features.
 */
public class UserView {

    public void displayRegistrationSuccess(User user) {
        System.out.println("\n[SUCCESS] Registration successful for " + user.getName() + " (ID: " + user.getUserId() + ")\n");
    }

    public void displayLoginSuccess(User user) {
        System.out.println("\n[SUCCESS] Login successful. Welcome, " + user.getName() + " (" + user.getUserType() + ")\n");
    }

    public void displayMovies(List<Movie> movies, String heading) {
        System.out.println("\n========================================");
        System.out.println("  " + heading);
        System.out.println("========================================");

        if (movies.isEmpty()) {
            System.out.println("  No movies found.");
        } else {
            for (Movie movie : movies) {
                System.out.println("  " + movie);
            }
        }

        System.out.println("========================================\n");
    }

    public void displayError(String message) {
        System.out.println("\n[ERROR] " + message + "\n");
    }
}

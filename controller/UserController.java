package controller;

import model.Movie;
import model.User;
import singleton.MovieCatalog;
import singleton.UserManager;
import view.UserView;

import java.util.List;

/**
 * Controller for user authentication and movie browsing/search.
 */
public class UserController {

    private final UserManager userManager;
    private final MovieCatalog movieCatalog;
    private final UserView userView;

    public UserController() {
        this.userManager = UserManager.getInstance();
        this.movieCatalog = MovieCatalog.getInstance();
        this.userView = new UserView();
    }

    public User registerCustomer(String name, String email, String password) {
        User user = userManager.registerCustomer(name, email, password);
        if (user == null) {
            userView.displayError("Registration failed. Email may already exist or input is invalid.");
            return null;
        }
        userView.displayRegistrationSuccess(user);
        return user;
    }

    public User login(String email, String password) {
        User user = userManager.login(email, password);
        if (user == null) {
            userView.displayError("Login failed. Invalid email or password.");
            return null;
        }
        userView.displayLoginSuccess(user);
        return user;
    }

    public void browseMovies() {
        List<Movie> movies = movieCatalog.getAllActiveMovies();
        userView.displayMovies(movies, "AVAILABLE MOVIES");
    }

    public void searchMovies(String titleKeyword, String genre, String language, double minRating) {
        List<Movie> movies = movieCatalog.searchMovies(titleKeyword, genre, language, minRating);
        userView.displayMovies(movies, "SEARCH RESULTS");
    }
}

package controller;

import model.Movie;
import model.User;
import singleton.MovieCatalog;
import singleton.UserManager;

import java.util.List;

/**
 * Service layer for user and movie-browsing use-cases.
 */
public class UserService {

    private final UserManager userManager;
    private final MovieCatalog movieCatalog;

    public UserService() {
        this.userManager = UserManager.getInstance();
        this.movieCatalog = MovieCatalog.getInstance();
    }

    public User registerCustomer(String name, String email, String password) {
        return userManager.registerCustomer(name, email, password);
    }

    public User login(String email, String password) {
        return userManager.login(email, password);
    }

    public List<Movie> getAllActiveMovies() {
        return movieCatalog.getAllActiveMovies();
    }

    public List<Movie> searchMovies(String titleKeyword, String genre, String language, double minRating) {
        return movieCatalog.searchMovies(titleKeyword, genre, language, minRating);
    }
}

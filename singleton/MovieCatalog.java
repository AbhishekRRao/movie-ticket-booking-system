package singleton;

import model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and searches movie records.
 */
public class MovieCatalog {

    private static volatile MovieCatalog instance;

    private final List<Movie> movies;

    private MovieCatalog() {
        this.movies = new ArrayList<>();
        seedMovies();
    }

    public static MovieCatalog getInstance() {
        if (instance == null) {
            synchronized (MovieCatalog.class) {
                if (instance == null) {
                    instance = new MovieCatalog();
                }
            }
        }
        return instance;
    }

    public List<Movie> getAllActiveMovies() {
        List<Movie> active = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.isActive()) {
                active.add(movie);
            }
        }
        return active;
    }

    public List<Movie> searchMovies(String titleKeyword, String genre, String language, double minRating) {
        String normalizedTitle = normalize(titleKeyword);
        String normalizedGenre = normalize(genre);
        String normalizedLanguage = normalize(language);

        List<Movie> results = new ArrayList<>();
        for (Movie movie : movies) {
            if (!movie.isActive()) {
                continue;
            }

            boolean titleMatch = normalizedTitle.isEmpty() || normalize(movie.getTitle()).contains(normalizedTitle);
            boolean genreMatch = normalizedGenre.isEmpty() || normalize(movie.getGenre()).equals(normalizedGenre);
            boolean languageMatch = normalizedLanguage.isEmpty() || normalize(movie.getLanguage()).equals(normalizedLanguage);
            boolean ratingMatch = movie.getRating() >= minRating;

            if (titleMatch && genreMatch && languageMatch && ratingMatch) {
                results.add(movie);
            }
        }
        return results;
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    private void seedMovies() {
        movies.add(new Movie(1, "Interstellar", "Sci-Fi", "English", 8.9, true));
        movies.add(new Movie(2, "Inception", "Sci-Fi", "English", 8.8, true));
        movies.add(new Movie(3, "Kantara", "Action", "Kannada", 8.2, true));
        movies.add(new Movie(4, "Dangal", "Drama", "Hindi", 8.3, true));
        movies.add(new Movie(5, "The Lion King", "Animation", "English", 8.5, false));
        movies.add(new Movie(6, "Drishyam", "Thriller", "Malayalam", 8.6, true));
    }
}

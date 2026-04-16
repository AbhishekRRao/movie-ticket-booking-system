package admin.builder;

import model.Movie;
import java.time.LocalDate;

/**
 * BUILDER PATTERN - Movie Builder
 * 
 * Constructs Movie objects step-by-step with validation.
 * Simplifies creation of complex Movie objects with many optional fields.
 * 
 * Design Pattern: Builder Pattern
 * Design Principle: Single Responsibility - Only responsible for building movies
 * Design Principle: Open/Closed - Can add new fields without breaking existing code
 *
 * @author Admin Module
 */
public class MovieBuilder {
    
    private int movieId;
    private String title;
    private String genre;
    private String language;
    private double duration;
    private String director;
    private String cast;
    private LocalDate releaseDate;
    private String description;
    private double rating;
    private boolean isActive;
    
    public MovieBuilder setMovieId(int movieId) {
        this.movieId = movieId;
        return this;
    }
    
    public MovieBuilder setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
        return this;
    }
    
    public MovieBuilder setGenre(String genre) {
        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty");
        }
        this.genre = genre;
        return this;
    }
    
    public MovieBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }
    
    public MovieBuilder setDuration(double duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        this.duration = duration;
        return this;
    }
    
    public MovieBuilder setDirector(String director) {
        this.director = director;
        return this;
    }
    
    public MovieBuilder setCast(String cast) {
        this.cast = cast;
        return this;
    }
    
    public MovieBuilder setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }
    
    public MovieBuilder setDescription(String description) {
        this.description = description;
        return this;
    }
    
    public MovieBuilder setRating(double rating) {
        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        }
        this.rating = rating;
        return this;
    }
    
    public MovieBuilder setActive(boolean active) {
        this.isActive = active;
        return this;
    }
    
    /**
     * Build and return the Movie object
     * Validates required fields before building
     */
    public Movie build() {
        // Validate mandatory fields
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Movie title is required");
        }
        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Movie genre is required");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Movie duration must be set");
        }
        
        Movie movie = new Movie();
        movie.setMovieId(movieId);
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setLanguage(language != null ? language : "English");
        movie.setDuration(duration);
        movie.setDirector(director);
        movie.setCast(cast);
        movie.setReleaseDate(releaseDate);
        movie.setDescription(description);
        movie.setRating(rating);
        movie.setActive(isActive);
        
        System.out.println("[BUILDER] Movie built: " + title);
        return movie;
    }
}

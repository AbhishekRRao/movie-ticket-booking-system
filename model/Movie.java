package model;

import java.time.LocalDate;

/**
 * Movie model used for browsing, search, and admin management.
 * Supports both basic info and extended admin details.
 */
public class Movie {
    private int movieId;
    private String title;
    private String genre;
    private String language;
    private double rating;
    private boolean active;
    
    // Extended fields for admin module
    private double duration;
    private String director;
    private String cast;
    private LocalDate releaseDate;
    private String description;

    // No-arg constructor for builder pattern support
    public Movie() {
        this.active = true;
    }
    
    // Original constructor for backward compatibility
    public Movie(int movieId, String title, String genre, String language, double rating, boolean active) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.rating = rating;
        this.active = active;
    }

    // Getters
    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getLanguage() { return language; }
    public double getRating() { return rating; }
    public boolean isActive() { return active; }
    public double getDuration() { return duration; }
    public String getDirector() { return director; }
    public String getCast() { return cast; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public String getDescription() { return description; }
    
    // Setters (for builder pattern support)
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setLanguage(String language) { this.language = language; }
    public void setRating(double rating) { this.rating = rating; }
    public void setActive(boolean active) { this.active = active; }
    public void setDuration(double duration) { this.duration = duration; }
    public void setDirector(String director) { this.director = director; }
    public void setCast(String cast) { this.cast = cast; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Movie[ID:" + movieId + " | " + title + " | " + genre + " | " + language + " | Rating:" + rating + "]";
    }
}

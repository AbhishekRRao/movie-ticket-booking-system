package model;

/**
 * Movie model used for browsing and search.
 */
public class Movie {
    private int movieId;
    private String title;
    private String genre;
    private String language;
    private double rating;
    private boolean active;

    public Movie(int movieId, String title, String genre, String language, double rating, boolean active) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.rating = rating;
        this.active = active;
    }

    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getLanguage() { return language; }
    public double getRating() { return rating; }
    public boolean isActive() { return active; }

    @Override
    public String toString() {
        return "Movie[ID:" + movieId + " | " + title + " | " + genre + " | " + language + " | Rating:" + rating + "]";
    }
}

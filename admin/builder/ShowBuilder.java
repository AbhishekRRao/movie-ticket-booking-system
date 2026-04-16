package admin.builder;

import model.Show;
import model.Movie;
import java.time.LocalDateTime;

/**
 * BUILDER PATTERN - Show Builder
 * 
 * Constructs Show objects step-by-step with validation.
 * Manages pricing with strategy pattern (optional for future extension).
 * 
 * Design Pattern: Builder Pattern
 * Design Principle: Single Responsibility - Only responsible for building shows
 * Design Principle: Open/Closed - Can add pricing strategies later
 *
 * @author Admin Module
 */
public class ShowBuilder {
    
    private int showId;
    private Movie movie;
    private LocalDateTime showTime;
    private String auditorium;
    private int totalSeats;
    private double basePrice;
    private String language;
    private String format; // 2D, 3D, IMAX
    private boolean isActive;
    
    public ShowBuilder setShowId(int showId) {
        this.showId = showId;
        return this;
    }
    
    public ShowBuilder setMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }
        this.movie = movie;
        return this;
    }
    
    public ShowBuilder setShowTime(LocalDateTime showTime) {
        if (showTime == null) {
            throw new IllegalArgumentException("Show time cannot be null");
        }
        this.showTime = showTime;
        return this;
    }
    
    public ShowBuilder setAuditorium(String auditorium) {
        if (auditorium == null || auditorium.isEmpty()) {
            throw new IllegalArgumentException("Auditorium cannot be empty");
        }
        this.auditorium = auditorium;
        return this;
    }
    
    public ShowBuilder setTotalSeats(int totalSeats) {
        if (totalSeats <= 0) {
            throw new IllegalArgumentException("Total seats must be positive");
        }
        this.totalSeats = totalSeats;
        return this;
    }
    
    public ShowBuilder setBasePrice(double basePrice) {
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Base price must be positive");
        }
        this.basePrice = basePrice;
        return this;
    }
    
    public ShowBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }
    
    public ShowBuilder setFormat(String format) {
        if (!format.equals("2D") && !format.equals("3D") && !format.equals("IMAX")) {
            throw new IllegalArgumentException("Invalid format. Use 2D, 3D, or IMAX");
        }
        this.format = format;
        return this;
    }
    
    public ShowBuilder setActive(boolean active) {
        this.isActive = active;
        return this;
    }
    
    /**
     * Build and return the Show object
     * Validates required fields before building
     */
    public Show build() {
        // Validate mandatory fields
        if (movie == null) {
            throw new IllegalArgumentException("Movie is required");
        }
        if (showTime == null) {
            throw new IllegalArgumentException("Show time is required");
        }
        if (auditorium == null || auditorium.isEmpty()) {
            throw new IllegalArgumentException("Auditorium is required");
        }
        if (totalSeats <= 0) {
            throw new IllegalArgumentException("Total seats must be set");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Base price must be set");
        }
        
        Show show = new Show();
        show.setShowId(showId);
        show.setMovie(movie);
        show.setShowTime(showTime);
        show.setAuditorium(auditorium);
        show.setTotalSeats(totalSeats);
        show.setAvailableSeats(totalSeats);
        show.setBasePrice(basePrice);
        show.setLanguage(language != null ? language : "English");
        show.setFormat(format != null ? format : "2D");
        show.setActive(isActive);
        
        System.out.println("[BUILDER] Show built: " + showId + " - " + movie.getTitle());
        return show;
    }
}

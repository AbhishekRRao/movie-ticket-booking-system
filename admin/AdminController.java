package admin;

import admin.builder.MovieBuilder;
import admin.builder.ShowBuilder;
import admin.state.MovieStateContext;
import model.Movie;
import model.Show;
import singleton.MovieCatalog;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ADMIN CONTROLLER - MVC Controller Pattern
 * 
 * Handles HTTP requests and interactions for admin operations.
 * Coordinates between view and service layer.
 * 
 * Design Pattern: MVC Pattern (Controller)
 * Design Principle: Single Responsibility - Only handles request routing
 * Design Principle: Dependency Inversion - Depends on AdminService abstraction
 *
 * @author Admin Module
 */
public class AdminController {
    
    private AdminService adminService;
    private AdminView adminView;
    
    public AdminController() {
        this.adminService = AdminService.getInstance();
        this.adminView = new AdminView();
    }
    
    // ============ MOVIE MANAGEMENT ENDPOINTS ============
    
    /**
     * Handle movie creation request
     */
    public void handleAddMovie(String title, String genre, String language, 
                              double duration, String director, String cast, 
                              LocalDate releaseDate, String description, double rating) {
        try {
            MovieBuilder builder = new MovieBuilder()
                    .setMovieId((int)(Math.random() * 1000))
                    .setTitle(title)
                    .setGenre(genre)
                    .setLanguage(language)
                    .setDuration(duration)
                    .setDirector(director)
                    .setCast(cast)
                    .setReleaseDate(releaseDate)
                    .setDescription(description)
                    .setRating(rating)
                    .setActive(true);
            
            adminService.addMovie(builder);
            adminView.displayMessage("Movie added successfully!");
        } catch (Exception e) {
            adminView.displayError("Error adding movie: " + e.getMessage());
        }
    }
    
    /**
     * Handle movie update request
     */
    public void handleUpdateMovie(int movieId, String title, double rating) {
        try {
            Movie existing = MovieCatalog.getInstance().getMovieById(movieId);
            if (existing == null) {
                adminView.displayError("Movie not found!");
                return;
            }
            
            MovieBuilder builder = new MovieBuilder()
                    .setMovieId(movieId)
                    .setTitle(title != null ? title : existing.getTitle())
                    .setGenre(existing.getGenre())
                    .setDuration(existing.getDuration())
                    .setRating(rating > 0 ? rating : existing.getRating());
            
            adminService.updateMovie(builder);
            adminView.displayMessage("Movie updated successfully!");
        } catch (Exception e) {
            adminView.displayError("Error updating movie: " + e.getMessage());
        }
    }
    
    /**
     * Handle movie deletion request
     */
    public void handleDeleteMovie(int movieId) {
        try {
            adminService.deleteMovie(movieId);
            adminView.displayMessage("Movie deleted successfully!");
        } catch (Exception e) {
            adminView.displayError("Error deleting movie: " + e.getMessage());
        }
    }
    
    /**
     * Handle movie state change request
     */
    public void handleChangeMovieState(int movieId, String action) {
        try {
            MovieStateContext stateContext = adminService.getMovieState(movieId);
            if (stateContext == null) {
                adminView.displayError("Movie state not found!");
                return;
            }
            
            switch (action.toLowerCase()) {
                case "activate":
                    stateContext.activate();
                    break;
                case "deactivate":
                    stateContext.deactivate();
                    break;
                case "archive":
                    stateContext.archive();
                    break;
                default:
                    adminView.displayError("Invalid action!");
            }
            adminView.displayMessage("Movie state changed to: " + stateContext.getStateName());
        } catch (Exception e) {
            adminView.displayError("Error changing movie state: " + e.getMessage());
        }
    }
    
    // ============ SHOW MANAGEMENT ENDPOINTS ============
    
    /**
     * Handle show creation request
     */
    public void handleAddShow(int movieId, LocalDateTime showTime, String auditorium, 
                             int totalSeats, double basePrice, String language, String format) {
        try {
            Movie movie = MovieCatalog.getInstance().getMovieById(movieId);
            if (movie == null) {
                adminView.displayError("Movie not found!");
                return;
            }
            
            ShowBuilder builder = new ShowBuilder()
                    .setMovie(movie)
                    .setShowTime(showTime)
                    .setAuditorium(auditorium)
                    .setTotalSeats(totalSeats)
                    .setBasePrice(basePrice)
                    .setLanguage(language)
                    .setFormat(format)
                    .setActive(true);
            
            adminService.addShow(builder);
            adminView.displayMessage("Show scheduled successfully!");
        } catch (Exception e) {
            adminView.displayError("Error scheduling show: " + e.getMessage());
        }
    }
    
    /**
     * Handle show update request
     */
    public void handleUpdateShow(int showId, double newPrice) {
        try {
            Show existing = MovieCatalog.getInstance().getShowById(showId);
            if (existing == null) {
                adminView.displayError("Show not found!");
                return;
            }
            
            ShowBuilder builder = new ShowBuilder()
                    .setShowId(showId)
                    .setMovie(existing.getMovie())
                    .setShowTime(existing.getShowTimeDateTime())
                    .setAuditorium(existing.getAuditorium())
                    .setTotalSeats(existing.getTotalSeats())
                    .setBasePrice(newPrice > 0 ? newPrice : existing.getBasePrice());
            
            adminService.updateShow(builder);
            adminView.displayMessage("Show updated successfully!");
        } catch (Exception e) {
            adminView.displayError("Error updating show: " + e.getMessage());
        }
    }
    
    /**
     * Handle show deletion request
     */
    public void handleDeleteShow(int showId) {
        try {
            adminService.deleteShow(showId);
            adminView.displayMessage("Show deleted successfully!");
        } catch (Exception e) {
            adminView.displayError("Error deleting show: " + e.getMessage());
        }
    }
    
    // ============ REPORT GENERATION ENDPOINTS ============
    
    /**
     * Handle booking report request
     */
    public void handleGenerateBookingReport(LocalDate startDate, LocalDate endDate) {
        try {
            adminService.generateBookingReport(startDate, endDate);
        } catch (Exception e) {
            adminView.displayError("Error generating booking report: " + e.getMessage());
        }
    }
    
    /**
     * Handle revenue report request
     */
    public void handleGenerateRevenueReport(LocalDate startDate, LocalDate endDate) {
        try {
            adminService.generateRevenueReport(startDate, endDate);
        } catch (Exception e) {
            adminView.displayError("Error generating revenue report: " + e.getMessage());
        }
    }
    
    /**
     * Handle show performance report request
     */
    public void handleGenerateShowReport(LocalDate startDate, LocalDate endDate) {
        try {
            adminService.generateShowReport(startDate, endDate);
        } catch (Exception e) {
            adminView.displayError("Error generating show report: " + e.getMessage());
        }
    }
    
    // ============ HISTORY MANAGEMENT ============
    
    /**
     * Handle undo request
     */
    public void handleUndo() {
        adminService.undoLastOperation();
    }
    
    /**
     * Handle redo request
     */
    public void handleRedo() {
        adminService.redoLastOperation();
    }
    
    // ============ VIEW OPERATIONS ============
    
    /**
     * Handle view all shows request
     */
    public void handleViewAllShows() {
        try {
            adminService.displayAllShows();
        } catch (Exception e) {
            adminView.displayError("Error viewing shows: " + e.getMessage());
        }
    }
}

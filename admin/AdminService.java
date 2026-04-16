package admin;

import admin.builder.MovieBuilder;
import admin.builder.ShowBuilder;
import admin.command.*;
import admin.state.MovieStateContext;
import admin.report.*;
import model.Movie;
import model.Show;
import singleton.MovieCatalog;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * ADMIN SERVICE - Business Logic Layer
 * 
 * Handles all admin operations like movie/show management and report generation.
 * Orchestrates Command Pattern, Builder Pattern, State Pattern, and Template Method Pattern.
 * 
 * Design Principle: Single Responsibility - Only handles admin business logic
 * Design Principle: Dependency Inversion - Depends on abstractions (Command, ReportGenerator, etc.)
 * Design Principle: Open/Closed - Can be extended with new operations
 *
 * @author Admin Module
 */
public class AdminService {
    
    private CommandExecutor commandExecutor;
    private MovieCatalog movieCatalog;
    private Map<Integer, MovieStateContext> movieStateContexts;
    
    private static AdminService instance;
    
    private AdminService() {
        this.commandExecutor = CommandExecutor.getInstance();
        this.movieCatalog = MovieCatalog.getInstance();
        this.movieStateContexts = new HashMap<>();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService();
        }
        return instance;
    }
    
    // ============ MOVIE MANAGEMENT ============
    
    /**
     * Add a new movie using Builder Pattern and Command Pattern
     */
    public void addMovie(MovieBuilder builder) {
        Movie movie = builder.build();
        Command addCommand = new AddMovieCommand(movie);
        commandExecutor.execute(addCommand);
        
        // Create state context for the movie
        MovieStateContext stateContext = new MovieStateContext(movie.getMovieId(), movie.getTitle());
        movieStateContexts.put(movie.getMovieId(), stateContext);
    }
    
    /**
     * Update an existing movie using Command Pattern
     */
    public void updateMovie(MovieBuilder builder) {
        Movie updatedMovie = builder.build();
        Command updateCommand = new UpdateMovieCommand(updatedMovie);
        commandExecutor.execute(updateCommand);
    }
    
    /**
     * Delete a movie using Command Pattern
     */
    public void deleteMovie(int movieId) {
        Movie movie = movieCatalog.getMovieById(movieId);
        if (movie != null) {
            Command deleteCommand = new DeleteMovieCommand(movie);
            commandExecutor.execute(deleteCommand);
        }
    }
    
    /**
     * Get movie state context
     */
    public MovieStateContext getMovieState(int movieId) {
        if (!movieStateContexts.containsKey(movieId)) {
            Movie movie = movieCatalog.getMovieById(movieId);
            if (movie != null) {
                movieStateContexts.put(movieId, new MovieStateContext(movieId, movie.getTitle()));
            }
        }
        return movieStateContexts.getOrDefault(movieId, null);
    }
    
    // ============ SHOW MANAGEMENT ============
    
    /**
     * Add a new show using Builder Pattern and Command Pattern
     */
    public void addShow(ShowBuilder builder) {
        Show show = builder.build();
        ShowCommand addCommand = new AddShowCommand(show);
        commandExecutor.execute(addCommand);
    }
    
    /**
     * Update an existing show using Command Pattern
     */
    public void updateShow(ShowBuilder builder) {
        Show updatedShow = builder.build();
        ShowCommand updateCommand = new UpdateShowCommand(updatedShow);
        commandExecutor.execute(updateCommand);
    }
    
    /**
     * Delete a show using Command Pattern
     */
    public void deleteShow(int showId) {
        Show show = movieCatalog.getShowById(showId);
        if (show != null) {
            ShowCommand deleteCommand = new DeleteShowCommand(show);
            commandExecutor.execute(deleteCommand);
        }
    }
    
    // ============ REPORT GENERATION ============
    
    /**
     * Generate booking history report using Template Method Pattern
     */
    public void generateBookingReport(LocalDate startDate, LocalDate endDate) {
        ReportGenerator report = new BookingReportGenerator(startDate, endDate);
        report.generateReport();
    }
    
    /**
     * Generate revenue report using Template Method Pattern
     */
    public void generateRevenueReport(LocalDate startDate, LocalDate endDate) {
        ReportGenerator report = new RevenueReportGenerator(startDate, endDate);
        report.generateReport();
    }
    
    /**
     * Generate show performance report using Template Method Pattern
     */
    public void generateShowReport(LocalDate startDate, LocalDate endDate) {
        ReportGenerator report = new ShowReportGenerator(startDate, endDate);
        report.generateReport();
    }
    
    // ============ COMMAND HISTORY OPERATIONS ============
    
    /**
     * Undo last command
     */
    public void undoLastOperation() {
        commandExecutor.undo();
    }
    
    /**
     * Redo last undone command
     */
    public void redoLastOperation() {
        commandExecutor.redo();
    }
    
    /**
     * Clear all command history
     */
    public void clearHistory() {
        commandExecutor.clearHistory();
    }
}

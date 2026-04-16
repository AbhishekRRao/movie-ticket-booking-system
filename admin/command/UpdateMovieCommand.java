package admin.command;

import model.Movie;
import singleton.MovieCatalog;

/**
 * COMMAND PATTERN - Update Movie Command
 * 
 * Concrete command for updating an existing movie.
 * Stores the previous state to support undo operations.
 * 
 * Design Principle: Dependency Inversion - Depends on MovieCatalog abstraction
 */
public class UpdateMovieCommand implements Command {
    
    private Movie updatedMovie;
    private Movie previousState;
    private MovieCatalog movieCatalog;
    
    public UpdateMovieCommand(Movie updatedMovie) {
        this.updatedMovie = updatedMovie;
        this.movieCatalog = MovieCatalog.getInstance();
        // Store the previous state for undo
        this.previousState = movieCatalog.getMovieById(updatedMovie.getMovieId());
    }
    
    @Override
    public void execute() {
        movieCatalog.updateMovie(updatedMovie);
        System.out.println("[COMMAND] Movie updated: " + updatedMovie.getTitle());
    }
    
    @Override
    public void undo() {
        if (previousState != null) {
            movieCatalog.updateMovie(previousState);
            System.out.println("[COMMAND] Movie update undone: " + updatedMovie.getTitle());
        }
    }
    
    @Override
    public String getDescription() {
        return "Update Movie: " + updatedMovie.getTitle();
    }
}

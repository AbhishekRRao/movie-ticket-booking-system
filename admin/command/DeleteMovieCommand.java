package admin.command;

import model.Movie;
import singleton.MovieCatalog;

/**
 * COMMAND PATTERN - Delete Movie Command
 * 
 * Concrete command for deleting a movie from the catalog.
 * Preserves the deleted movie for undo operations.
 * 
 * Design Principle: Dependency Inversion - Depends on MovieCatalog abstraction
 */
public class DeleteMovieCommand implements Command {
    
    private Movie deletedMovie;
    private MovieCatalog movieCatalog;
    
    public DeleteMovieCommand(Movie movie) {
        this.deletedMovie = movie;
        this.movieCatalog = MovieCatalog.getInstance();
    }
    
    @Override
    public void execute() {
        movieCatalog.deleteMovie(deletedMovie.getMovieId());
        System.out.println("[COMMAND] Movie deleted: " + deletedMovie.getTitle());
    }
    
    @Override
    public void undo() {
        movieCatalog.addMovie(deletedMovie);
        System.out.println("[COMMAND] Movie deletion undone: " + deletedMovie.getTitle());
    }
    
    @Override
    public String getDescription() {
        return "Delete Movie: " + deletedMovie.getTitle();
    }
}

package admin.command;

import model.Movie;
import singleton.MovieCatalog;

/**
 * COMMAND PATTERN - Add Movie Command
 * 
 * Concrete command for adding a movie to the catalog.
 * Stores the state needed to perform and undo the operation.
 * 
 * Design Principle: Dependency Inversion - Depends on MovieCatalog abstraction
 */
public class AddMovieCommand implements Command {
    
    private Movie movie;
    private MovieCatalog movieCatalog;
    
    public AddMovieCommand(Movie movie) {
        this.movie = movie;
        this.movieCatalog = MovieCatalog.getInstance();
    }
    
    @Override
    public void execute() {
        movieCatalog.addMovie(movie);
        System.out.println("[COMMAND] Movie added: " + movie.getTitle());
    }
    
    @Override
    public void undo() {
        movieCatalog.deleteMovie(movie.getMovieId());
        System.out.println("[COMMAND] Movie addition undone: " + movie.getTitle());
    }
    
    @Override
    public String getDescription() {
        return "Add Movie: " + movie.getTitle();
    }
}

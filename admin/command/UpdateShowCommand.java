package admin.command;

import model.Show;
import singleton.MovieCatalog;

/**
 * COMMAND PATTERN - Update Show Command
 * 
 * Concrete command for updating show details.
 * Supports undo by preserving previous show state.
 * 
 * Design Principle: Dependency Inversion - Depends on MovieCatalog abstraction
 */
public class UpdateShowCommand implements ShowCommand {
    
    private Show updatedShow;
    private Show previousState;
    private MovieCatalog movieCatalog;
    
    public UpdateShowCommand(Show updatedShow) {
        this.updatedShow = updatedShow;
        this.movieCatalog = MovieCatalog.getInstance();
        this.previousState = movieCatalog.getShowById(updatedShow.getShowId());
    }
    
    @Override
    public void execute() {
        movieCatalog.updateShow(updatedShow);
        System.out.println("[COMMAND] Show updated: " + updatedShow.getShowId());
    }
    
    @Override
    public void undo() {
        if (previousState != null) {
            movieCatalog.updateShow(previousState);
            System.out.println("[COMMAND] Show update undone: " + updatedShow.getShowId());
        }
    }
    
    @Override
    public String getDescription() {
        return "Update Show: " + updatedShow.getShowId();
    }
    
    @Override
    public int getShowId() {
        return updatedShow.getShowId();
    }
}

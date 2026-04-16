package admin.command;

import model.Show;
import singleton.MovieCatalog;

/**
 * COMMAND PATTERN - Add Show Command
 * 
 * Concrete command for scheduling a new show.
 * Implements ShowCommand interface.
 * 
 * Design Principle: Dependency Inversion - Depends on MovieCatalog abstraction
 */
public class AddShowCommand implements ShowCommand {
    
    private Show show;
    private MovieCatalog movieCatalog;
    
    public AddShowCommand(Show show) {
        this.show = show;
        this.movieCatalog = MovieCatalog.getInstance();
    }
    
    @Override
    public void execute() {
        movieCatalog.addShow(show);
        System.out.println("[COMMAND] Show scheduled: " + show.getShowId());
    }
    
    @Override
    public void undo() {
        movieCatalog.deleteShow(show.getShowId());
        System.out.println("[COMMAND] Show scheduling undone: " + show.getShowId());
    }
    
    @Override
    public String getDescription() {
        return "Add Show: " + show.getShowId();
    }
    
    @Override
    public int getShowId() {
        return show.getShowId();
    }
}

package admin.command;

import model.Show;
import singleton.MovieCatalog;

/**
 * COMMAND PATTERN - Delete Show Command
 * 
 * Concrete command for removing a show.
 * Preserves show data for undo operations.
 * 
 * Design Principle: Dependency Inversion - Depends on MovieCatalog abstraction
 */
public class DeleteShowCommand implements ShowCommand {
    
    private Show deletedShow;
    private MovieCatalog movieCatalog;
    
    public DeleteShowCommand(Show show) {
        this.deletedShow = show;
        this.movieCatalog = MovieCatalog.getInstance();
    }
    
    @Override
    public void execute() {
        movieCatalog.deleteShow(deletedShow.getShowId());
        System.out.println("[COMMAND] Show deleted: " + deletedShow.getShowId());
    }
    
    @Override
    public void undo() {
        movieCatalog.addShow(deletedShow);
        System.out.println("[COMMAND] Show deletion undone: " + deletedShow.getShowId());
    }
    
    @Override
    public String getDescription() {
        return "Delete Show: " + deletedShow.getShowId();
    }
    
    @Override
    public int getShowId() {
        return deletedShow.getShowId();
    }
}

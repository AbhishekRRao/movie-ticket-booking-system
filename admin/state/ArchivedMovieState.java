package admin.state;

/**
 * STATE PATTERN - Archived Movie State
 * 
 * Represents an archived movie (no longer in circulation).
 * Can only be reactivated or stay archived.
 * 
 * Design Principle: Single Responsibility - Only handles archived movie behavior
 */
public class ArchivedMovieState implements MovieState {
    
    private MovieStateContext context;
    
    public ArchivedMovieState(MovieStateContext context) {
        this.context = context;
    }
    
    @Override
    public void activate() {
        System.out.println("[STATE] Activating archived movie!");
        context.setState(new ActiveMovieState(context));
    }
    
    @Override
    public void deactivate() {
        System.out.println("[STATE] Cannot deactivate archived movie! Reactivate first.");
    }
    
    @Override
    public void archive() {
        System.out.println("[STATE] Movie already archived!");
    }
    
    @Override
    public String getStateName() {
        return "ARCHIVED";
    }
}

package admin.state;

/**
 * STATE PATTERN - Active Movie State
 * 
 * Represents an active movie that is currently showing/available.
 * Can be deactivated or archived.
 * 
 * Design Principle: Single Responsibility - Only handles active movie behavior
 */
public class ActiveMovieState implements MovieState {
    
    private MovieStateContext context;
    
    public ActiveMovieState(MovieStateContext context) {
        this.context = context;
    }
    
    @Override
    public void activate() {
        System.out.println("[STATE] Movie already active!");
    }
    
    @Override
    public void deactivate() {
        System.out.println("[STATE] Movie deactivated!");
        context.setState(new InactiveMovieState(context));
    }
    
    @Override
    public void archive() {
        System.out.println("[STATE] Movie archived!");
        context.setState(new ArchivedMovieState(context));
    }
    
    @Override
    public String getStateName() {
        return "ACTIVE";
    }
}

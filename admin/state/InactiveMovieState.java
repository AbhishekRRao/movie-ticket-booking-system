package admin.state;

/**
 * STATE PATTERN - Inactive Movie State
 * 
 * Represents a movie that is temporarily inactive/hidden.
 * Can be activated or archived.
 * 
 * Design Principle: Single Responsibility - Only handles inactive movie behavior
 */
public class InactiveMovieState implements MovieState {
    
    private MovieStateContext context;
    
    public InactiveMovieState(MovieStateContext context) {
        this.context = context;
    }
    
    @Override
    public void activate() {
        System.out.println("[STATE] Movie activated!");
        context.setState(new ActiveMovieState(context));
    }
    
    @Override
    public void deactivate() {
        System.out.println("[STATE] Movie already inactive!");
    }
    
    @Override
    public void archive() {
        System.out.println("[STATE] Movie archived!");
        context.setState(new ArchivedMovieState(context));
    }
    
    @Override
    public String getStateName() {
        return "INACTIVE";
    }
}

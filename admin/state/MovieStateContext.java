package admin.state;

/**
 * STATE PATTERN - Movie State Context
 * 
 * Context class that maintains current movie state.
 * Delegates state-specific behavior to the current state object.
 * 
 * Design Principle: Dependency Inversion - Depends on MovieState abstraction
 */
public class MovieStateContext {
    
    private MovieState currentState;
    private int movieId;
    private String movieTitle;
    
    public MovieStateContext(int movieId, String movieTitle) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        // Start in active state
        this.currentState = new ActiveMovieState(this);
    }
    
    public void setState(MovieState state) {
        this.currentState = state;
    }
    
    public MovieState getCurrentState() {
        return currentState;
    }
    
    public void activate() {
        currentState.activate();
    }
    
    public void deactivate() {
        currentState.deactivate();
    }
    
    public void archive() {
        currentState.archive();
    }
    
    public String getStateName() {
        return currentState.getStateName();
    }
    
    public int getMovieId() {
        return movieId;
    }
    
    public String getMovieTitle() {
        return movieTitle;
    }
}

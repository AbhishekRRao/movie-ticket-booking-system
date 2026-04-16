package admin.state;

/**
 * STATE PATTERN - Movie State Interface
 * 
 * Represents different states a movie can be in.
 * Different behaviors for each state.
 * 
 * Design Pattern: State Pattern
 * Design Principle: Single Responsibility - Each state handles its own behavior
 * Design Principle: Open/Closed - New states can be added without modifying existing ones
 * Design Principle: Dependency Inversion - Context depends on abstraction (MovieState)
 *
 * @author Admin Module
 */
public interface MovieState {
    
    /**
     * Activate the movie
     */
    void activate();
    
    /**
     * Deactivate the movie
     */
    void deactivate();
    
    /**
     * Archive the movie
     */
    void archive();
    
    /**
     * Get state name
     */
    String getStateName();
}

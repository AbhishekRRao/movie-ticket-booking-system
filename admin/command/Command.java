package admin.command;

/**
 * COMMAND PATTERN - Abstract Command Interface
 * 
 * Design Pattern: Command Pattern
 * Encapsulates requests as objects allowing parameterization of clients
 * with different requests, queuing of requests, and logging of requests.
 * 
 * Design Principle: Single Responsibility - Each command is responsible for ONE action
 * Design Principle: Open/Closed - Can add new commands without modifying existing ones
 *
 * @author Admin Module
 */
public interface Command {
    
    /**
     * Execute the command operation
     */
    void execute();
    
    /**
     * Undo the command operation
     */
    void undo();
    
    /**
     * Get description of the command
     */
    String getDescription();
}

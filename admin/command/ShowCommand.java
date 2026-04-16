package admin.command;

/**
 * COMMAND PATTERN - Show Command Interface
 * 
 * Specialized command interface for show-related operations.
 * Extends basic Command with show-specific functionality.
 * 
 * Design Principle: Interface Segregation - Segregates show-specific operations
 */
public interface ShowCommand extends Command {
    
    /**
     * Get the show ID associated with this command
     */
    int getShowId();
}

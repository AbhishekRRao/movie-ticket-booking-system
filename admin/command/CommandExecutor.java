package admin.command;

import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

/**
 * COMMAND PATTERN - Command Executor (Invoker)
 * 
 * Executes commands and maintains history for undo/redo operations.
 * Acts as the invoker in the Command Pattern.
 * 
 * Design Principle: Single Responsibility - Only responsible for command execution
 * Design Principle: Dependency Inversion - Depends on Command abstraction
 */
public class CommandExecutor {
    
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;
    private Queue<Command> commandHistory;
    
    private static CommandExecutor instance;
    
    private CommandExecutor() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.commandHistory = new LinkedList<>();
    }
    
    /**
     * Get singleton instance of CommandExecutor
     */
    public static synchronized CommandExecutor getInstance() {
        if (instance == null) {
            instance = new CommandExecutor();
        }
        return instance;
    }
    
    /**
     * Execute a command and add to undo stack
     */
    public void execute(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear redo stack when new command is executed
        commandHistory.add(command);
        System.out.println("[EXECUTOR] Executed: " + command.getDescription());
    }
    
    /**
     * Undo the last executed command
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            System.out.println("[EXECUTOR] Undone: " + command.getDescription());
        } else {
            System.out.println("[EXECUTOR] Nothing to undo!");
        }
    }
    
    /**
     * Redo the last undone command
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            System.out.println("[EXECUTOR] Redone: " + command.getDescription());
        } else {
            System.out.println("[EXECUTOR] Nothing to redo!");
        }
    }
    
    /**
     * Get command history
     */
    public Queue<Command> getCommandHistory() {
        return commandHistory;
    }
    
    /**
     * Clear all history
     */
    public void clearHistory() {
        undoStack.clear();
        redoStack.clear();
        commandHistory.clear();
        System.out.println("[EXECUTOR] History cleared!");
    }
}

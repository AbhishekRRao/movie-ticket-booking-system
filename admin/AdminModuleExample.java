package admin;

import admin.builder.MovieBuilder;
import admin.builder.ShowBuilder;
import model.Movie;
import model.Show;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ADMIN MODULE - USAGE EXAMPLE
 * 
 * Demonstrates practical usage of all 4 design patterns in the Admin Module.
 * Shows how Command, Builder, State, and Template Method patterns work together.
 *
 * @author Admin Module Documentation
 */
public class AdminModuleExample {
    
    public static void main(String[] args) {
        System.out.println("========================================================");
        System.out.println("   ADMIN MODULE - DESIGN PATTERNS DEMONSTRATION          ");
        System.out.println("========================================================\n");
        
        // Initialize admin components
        AdminController adminController = new AdminController();
        AdminService adminService = AdminService.getInstance();
        
        // ===============================================
        // EXAMPLE 1: BUILDER PATTERN - Create A Movie
        // ===============================================
        System.out.println("\n[EXAMPLE 1] Builder Pattern - Creating Movie\n");
        System.out.println("Scene: Admin needs to add a new movie with many details");
        System.out.println("Solution: Use MovieBuilder for clean, readable code\n");
        
        adminController.handleAddMovie(
            "Inception",           // title
            "Science Fiction",     // genre
            "English",            // language
            148,                  // duration
            "Christopher Nolan",  // director
            "Leonardo DiCaprio, Ellen Page", // cast
            LocalDate.of(2010, 7, 16), // releaseDate
            "A skilled thief who steals corporate secrets using dream-sharing technology", // description
            8.8                   // rating
        );
        
        System.out.println("\n✓ Movie created using Builder Pattern");
        System.out.println("  - Fluent API made it easy to set parameters");
        System.out.println("  - Validation happened automatically");
        System.out.println("  - No 'telescoping constructor' problem");
        
        // ===============================================
        // EXAMPLE 2: COMMAND PATTERN - Add, Delete, Undo
        // ===============================================
        System.out.println("\n\n[EXAMPLE 2] Command Pattern - Movie Operations with Undo\n");
        System.out.println("Scene: Admin adds a movie, then realizes it's a duplicate, wants to undo\n");
        
        // Add first movie via command
        adminController.handleAddMovie(
            "Interstellar",
            "Science Fiction",
            "English",
            169,
            "Christopher Nolan",
            "Matthew McConaughey, Anne Hathaway",
            LocalDate.of(2014, 11, 7),
            "A team of explorers travel through a wormhole in space",
            8.6
        );
        System.out.println("✓ Movie added (stored in undo stack)\n");
        
        // Undo the addition
        adminService.undoLastOperation();
        System.out.println("✓ Movie addition undone (removed from catalog)\n");
        
        // Redo the addition
        adminService.redoLastOperation();
        System.out.println("✓ Movie addition redone (restored to catalog)\n");
        
        System.out.println("Benefits of Command Pattern:");
        System.out.println("  - Undo/Redo functionality");
        System.out.println("  - Command history maintained");
        System.out.println("  - Each operation is an object");
        System.out.println("  - Operations can be queued or delayed");
        
        // ===============================================
        // EXAMPLE 3: STATE PATTERN - Movie Lifecycle
        // ===============================================
        System.out.println("\n\n[EXAMPLE 3] State Pattern - Movie Status Management\n");
        System.out.println("Scene: Movie transitions through different states (Active → Inactive → Archived)\n");
        
        // Get movie state
        admin.state.MovieStateContext movieState = adminService.getMovieState(1);
        if (movieState != null) {
            System.out.println("Initial state: " + movieState.getStateName());
            System.out.println("Current state: " + movieState.getStateName() + "\n");
            
            // Transition: Active → Inactive
            System.out.println("Action: Deactivate movie");
            movieState.deactivate();
            System.out.println("Current state: " + movieState.getStateName() + "\n");
            
            // Transition: Inactive → Archived
            System.out.println("Action: Archive movie");
            movieState.archive();
            System.out.println("Current state: " + movieState.getStateName() + "\n");
            
            // Transition: Archived → Active
            System.out.println("Action: Reactivate movie");
            movieState.activate();
            System.out.println("Current state: " + movieState.getStateName() + "\n");
        }
        
        System.out.println("Benefits of State Pattern:");
        System.out.println("  - Clear state management");
        System.out.println("  - State-specific behavior");
        System.out.println("  - Prevents invalid transitions");
        System.out.println("  - No complex if-else chains");
        
        // ===============================================
        // EXAMPLE 4: TEMPLATE METHOD PATTERN - Reports
        // ===============================================
        System.out.println("\n\n[EXAMPLE 4] Template Method Pattern - Report Generation\n");
        System.out.println("Scene: Admin needs different reports, all following same structure\n");
        
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        
        // Generate Booking Report
        System.out.println("--- REQUEST 1: Booking Report ---");
        adminService.generateBookingReport(startDate, endDate);
        
        // Generate Revenue Report
        System.out.println("\n--- REQUEST 2: Revenue Report ---");
        adminService.generateRevenueReport(startDate, endDate);
        
        // Generate Show Report
        System.out.println("\n--- REQUEST 3: Show Report ---");
        adminService.generateShowReport(startDate, endDate);
        
        System.out.println("\nBenefits of Template Method Pattern:");
        System.out.println("  - Code reuse (common steps)");
        System.out.println("  - Consistent report structure");
        System.out.println("  - Easy to add new report types");
        System.out.println("  - Algorithm skeleton is defined");
        System.out.println("  - Subclasses focus only on differences");
        
        // ===============================================
        // EXAMPLE 5: COMBINED USAGE (MVC + All Patterns)
        // ===============================================
        System.out.println("\n\n[EXAMPLE 5] Complete Workflow - MVC + All Patterns\n");
        System.out.println("Scene: Admin schedule a show (uses Builder + Command patterns)\n");
        
        try {
            // Create a movie first
            adminController.handleAddMovie(
                "The Dark Knight",
                "Action",
                "English",
                152,
                "Christopher Nolan",
                "Christian Bale, Heath Ledger",
                LocalDate.of(2008, 7, 18),
                "Batman faces a new criminal mastermind",
                9.0
            );
            
            // Now schedule a show for this movie
            System.out.println("\nNow scheduling a show...\n");
            adminController.handleAddShow(
                1,  // movieId
                LocalDateTime.now().plusDays(3),  // showTime
                "Hall A",  // auditorium
                150,  // totalSeats
                350.0,  // basePrice
                "English",  // language
                "2D"  // format (2D, 3D, IMAX)
            );
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        // ===============================================
        // SUMMARY
        // ===============================================
        System.out.println("\n\n========================================================");
        System.out.println("   PATTERNS & PRINCIPLES SUMMARY              ");
        System.out.println("========================================================");
        System.out.println("");
        System.out.println("  4 Design Patterns Used:                              ");
        System.out.println("  1. Command Pattern       - Movie/Show operations   ");
        System.out.println("  2. Builder Pattern        - Object construction    ");
        System.out.println("  3. State Pattern          - Movie lifecycle        ");
        System.out.println("  4. Template Method        - Report generation      ");
        System.out.println("");
        System.out.println("  5 SOLID Principles Enforced:                         ");
        System.out.println("  - Single Responsibility     - One reason to change   ");
        System.out.println("  - Open/Closed              - Extend, don't modify   ");
        System.out.println("  - Liskov Substitution      - Substitute subtypes    ");
        System.out.println("  - Interface Segregation    - Focused interfaces     ");
        System.out.println("  - Dependency Inversion     - Depend on abstractions ");
        System.out.println("");
        System.out.println("  MVC Architecture:                                     ");
        System.out.println("  - Model     - Movie, Show, Admin entities           ");
        System.out.println("  - View      - AdminView (presentation)              ");
        System.out.println("  - Controller- AdminController (request handling)    ");
        System.out.println("  - Service   - AdminService (business logic)         ");
        System.out.println("");
        System.out.println("  Total Implementation: 25+ classes across 4 packages  ");
        System.out.println("");
        System.out.println("========================================================");
    }
}

/**
 * EXPECTED OUTPUT:
 * 
 * ========================================================
 *    ADMIN MODULE - DESIGN PATTERNS DEMONSTRATION          
 * ========================================================
 * 
 * [EXAMPLE 1] Builder Pattern - Creating Movie
 * 
 * [BUILDER] Movie built: Inception
 * [COMMAND] Movie added: Inception
 * [EXECUTOR] Executed: Add Movie: Inception
 * - Movie added successfully!
 * 
 * [EXAMPLE 2] Command Pattern - Movie Operations with Undo
 * 
 * [BUILDER] Movie built: Interstellar
 * [COMMAND] Movie added: Interstellar
 * [EXECUTOR] Executed: Add Movie: Interstellar
 * [EXECUTOR] Undone: Add Movie: Interstellar
 * [EXECUTOR] Redone: Add Movie: Interstellar
 * 
 * [EXAMPLE 3] State Pattern - Movie Status Management
 * 
 * [STATE] Movie deactivated!
 * [STATE] Movie archived!
 * [STATE] Activating archived movie!
 * 
 * [EXAMPLE 4] Template Method Pattern - Report Generation
 * 
 * ========================================
 * Generating Report: BOOKING HISTORY REPORT
 * ========================================
 * [GENERATOR] Date Range: 2024-01-01 to 2024-12-31
 * [GENERATOR] Fetching data from database...
 * [BOOKING] Processing booking data...
 * [BOOKING] Calculating booking statistics...
 * ... (full report output)
 * 
 * [EXAMPLE 5] Complete Workflow - MVC + All Patterns
 * (All patterns working together)
 * 
 * ========================================================
 * All patterns demonstrated successfully!
 */

package admin;

/**
 * ADMIN VIEW - MVC View Pattern
 * 
 * Presents information to the admin user and captures input.
 * Handles all display and UI interactions.
 * 
 * Design Pattern: MVC Pattern (View)
 * Design Principle: Single Responsibility - Only handles presentation logic
 *
 * @author Admin Module
 */
public class AdminView {
    
    /**
     * Display success message
     */
    public void displayMessage(String message) {
        System.out.println("[ADMIN VIEW] ✓ " + message);
    }
    
    /**
     * Display error message
     */
    public void displayError(String error) {
        System.out.println("[ADMIN VIEW] ✗ ERROR: " + error);
    }
    
    /**
     * Display admin menu
     */
    public void displayAdminMenu() {
        System.out.println("\n========== ADMIN PANEL MENU ==========");
        System.out.println("1. Movie Management");
        System.out.println("   1.1 Add Movie");
        System.out.println("   1.2 Update Movie");
        System.out.println("   1.3 Delete Movie");
        System.out.println("   1.4 Change Movie Status");
        System.out.println("\n2. Show Management");
        System.out.println("   2.1 Schedule Show");
        System.out.println("   2.2 Update Show");
        System.out.println("   2.3 Cancel Show");
        System.out.println("\n3. Reports");
        System.out.println("   3.1 Booking History Report");
        System.out.println("   3.2 Revenue Report");
        System.out.println("   3.3 Show Performance Report");
        System.out.println("\n4. History");
        System.out.println("   4.1 Undo");
        System.out.println("   4.2 Redo");
        System.out.println("========================================\n");
    }
    
    /**
     * Display movie details
     */
    public void displayMovieDetails(String title, String genre, double duration, double rating) {
        System.out.println("[ADMIN VIEW] --- Movie Details ---");
        System.out.println("Title: " + title);
        System.out.println("Genre: " + genre);
        System.out.println("Duration: " + duration + " minutes");
        System.out.println("Rating: " + rating + "/10");
    }
    
    /**
     * Display show details
     */
    public void displayShowDetails(int showId, String movieTitle, String showTime, String auditorium) {
        System.out.println("[ADMIN VIEW] --- Show Details ---");
        System.out.println("Show ID: " + showId);
        System.out.println("Movie: " + movieTitle);
        System.out.println("Time: " + showTime);
        System.out.println("Auditorium: " + auditorium);
    }
    
    /**
     * Display operation status
     */
    public void displayOperationStatus(String operation, boolean successful) {
        if (successful) {
            System.out.println("[ADMIN VIEW] ✓ " + operation + " completed successfully!");
        } else {
            System.out.println("[ADMIN VIEW] ✗ " + operation + " failed!");
        }
    }
}

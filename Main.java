import controller.BookingController;
import controller.UserController;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import enums.UserType;
import model.Booking;
import model.Seat;
import model.Show;
import model.User;
import singleton.BookingManager;
import singleton.DBConnection;

/**
 * MAIN - Interactive Entry Point for Movie Ticket Booking System
 * Member 2: Booking System
 *
 * Demonstrates:
 * - MVC Architecture
 * - Singleton Pattern (BookingManager + DBConnection + SeatAllocator)
 * - SRP + OCP Design Principles
 * - Interactive user input using Scanner
 */
public class Main {

    static Scanner scanner = new Scanner(System.in);
    static BookingController controller = new BookingController();
    static UserController userController = new UserController();
    static Show show = createDefaultShow();
    static User loggedInUser = null;

    public static void main(String[] args) {

        System.out.println("   MOVIE TICKET BOOKING SYSTEM - Member 2    ");

        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> browseMovies();
                case "4" -> searchMovies();
                case "5" -> viewAvailableSeats();
                case "6" -> bookTicket();
                case "7" -> confirmBooking();
                case "8" -> cancelBooking();
                case "9" -> viewBooking();
                case "10" -> verifySingleton();
                case "11" -> {
                    System.out.println("\nThank you for using Movie Ticket Booking System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("\n[!] Invalid choice. Please enter 1-11.\n");
            }
        }

        scanner.close();
    }

    // MENU
    static void printMenu() {
        System.out.println("\n----------------------------------------------");
        System.out.println("  1. Register (Customer)");
        System.out.println("  2. Login");
        System.out.println("  3. Browse Movies");
        System.out.println("  4. Search Movies");
        System.out.println("  5. View Available Seats");
        System.out.println("  6. Book Ticket (requires customer login)");
        System.out.println("  7. Confirm Booking (after payment)");
        System.out.println("  8. Cancel Booking");
        System.out.println("  9. View Booking Details");
        System.out.println("  10. Verify Singleton Pattern");
        System.out.println("  11. Exit");
        System.out.println("----------------------------------------------");
        if (loggedInUser == null) {
            System.out.println("  Current User: Not logged in");
        } else {
            System.out.println("  Current User: " + loggedInUser.getName() + " (" + loggedInUser.getUserType() + ")");
        }
        System.out.println("----------------------------------------------");
    }

    // OPTION 1: Register customer
    static void register() {
        System.out.println("\n--- REGISTER CUSTOMER ---");
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        userController.registerCustomer(name, email, password);
    }

    // OPTION 2: Login
    static void login() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        loggedInUser = userController.login(email, password);
    }

    // OPTION 3: Browse active movies
    static void browseMovies() {
        userController.browseMovies();
    }

    // OPTION 4: Search active movies
    static void searchMovies() {
        System.out.println("\n--- SEARCH MOVIES ---");

        System.out.print("Title keyword (press Enter to skip): ");
        String titleKeyword = scanner.nextLine().trim();

        System.out.print("Genre (press Enter to skip): ");
        String genre = scanner.nextLine().trim();

        System.out.print("Language (press Enter to skip): ");
        String language = scanner.nextLine().trim();

        System.out.print("Minimum rating (0-10, press Enter for 0): ");
        String ratingInput = scanner.nextLine().trim();

        double minRating = 0.0;
        if (!ratingInput.isEmpty()) {
            try {
                minRating = Double.parseDouble(ratingInput);
                if (minRating < 0) {
                    minRating = 0;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid rating. Using 0.");
            }
        }

        userController.searchMovies(titleKeyword, genre, language, minRating);
    }

    // OPTION 1: View Available Seats
    static void viewAvailableSeats() {
        controller.viewAvailableSeats(show);
    }

    // OPTION 2: Book Ticket
    static void bookTicket() {
        System.out.println("\n--- BOOK TICKET ---");

        if (loggedInUser == null) {
            System.out.println("[!] Please login first before booking.");
            return;
        }

        if (loggedInUser.getUserType() != UserType.CUSTOMER) {
            System.out.println("[!] Only CUSTOMER users can book tickets.");
            return;
        }

        controller.viewAvailableSeats(show);

        System.out.print("Enter seat numbers to book (comma separated, e.g. A1,B1): ");
        String seatInput = scanner.nextLine().trim().toUpperCase();

        if (seatInput.isEmpty()) {
            System.out.println("[!] No seats entered.");
            return;
        }

        List<String> seatNumbers = Arrays.asList(seatInput.split(","));

        System.out.print("Enter booking date (e.g. 2026-04-20): ");
        String date = scanner.nextLine().trim();

        Booking booking = controller.bookTicket(loggedInUser.getUserId(), show, seatNumbers, date);

        if (booking != null) {
            System.out.println("\n[INFO] Your Booking ID is: " + booking.getBookingId());
            System.out.println("[INFO] Use this ID to confirm or cancel your booking.");
        }
    }

    // OPTION 3: Confirm Booking
    static void confirmBooking() {
        System.out.println("\n--- CONFIRM BOOKING ---");
        System.out.print("Enter Booking ID to confirm: ");
        try {
            int bookingId = Integer.parseInt(scanner.nextLine().trim());
            controller.confirmBooking(bookingId);
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid Booking ID.");
        }
    }

    // OPTION 4: Cancel Booking
    static void cancelBooking() {
        System.out.println("\n--- CANCEL BOOKING ---");
        System.out.print("Enter Booking ID to cancel: ");
        try {
            int bookingId = Integer.parseInt(scanner.nextLine().trim());
            controller.cancelBooking(bookingId);
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid Booking ID.");
        }
    }

    // OPTION 5: View Booking
    static void viewBooking() {
        System.out.println("\n--- VIEW BOOKING DETAILS ---");
        System.out.print("Enter Booking ID: ");
        try {
            int bookingId = Integer.parseInt(scanner.nextLine().trim());
            controller.viewBooking(bookingId);
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid Booking ID.");
        }
    }

    // OPTION 6: Singleton Verification
    static void verifySingleton() {
        System.out.println("\n--- SINGLETON PATTERN VERIFICATION ---");

        BookingManager bm1 = BookingManager.getInstance();
        BookingManager bm2 = BookingManager.getInstance();
        System.out.println("  BookingManager instance 1 hashcode: " + bm1.hashCode());
        System.out.println("  BookingManager instance 2 hashcode: " + bm2.hashCode());
        System.out.println("  Same instance? " + (bm1 == bm2));

        DBConnection db1 = DBConnection.getInstance();
        DBConnection db2 = DBConnection.getInstance();
        System.out.println("  DBConnection instance 1 hashcode : " + db1.hashCode());
        System.out.println("  DBConnection instance 2 hashcode : " + db2.hashCode());
        System.out.println("  Same instance? " + (db1 == db2));

        System.out.println("\n  Singleton confirmed: Only ONE instance exists for each manager.");
    }

    // SETUP: Default Show with Seats
    static Show createDefaultShow() {
        Show s = new Show(1, "7:00 PM", "2026-04-20");
        s.addSeat(new Seat(1, "A1", "REGULAR", 150.0));
        s.addSeat(new Seat(2, "A2", "REGULAR", 150.0));
        s.addSeat(new Seat(3, "B1", "VIP",     350.0));
        s.addSeat(new Seat(4, "B2", "VIP",     350.0));
        s.addSeat(new Seat(5, "C1", "PREMIUM", 500.0));
        return s;
    }
}
import admin.AdminController;
import admin.AdminService;
import controller.BookingController;
import controller.UserController;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import enums.UserType;
import enums.PaymentMethod;
import model.Booking;
import model.Payment;
import model.Seat;
import model.Show;
import model.User;
import singleton.BookingManager;
import singleton.DBConnection;
import singleton.PaymentManager;
import payment.PaymentProcessor;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    static AdminController adminController = new AdminController();
    static Show show = createDefaultShow();
    static User loggedInUser = null;
    static boolean adminLoggedIn = false;

    public static void main(String[] args) {

        System.out.println("   MOVIE TICKET BOOKING SYSTEM   ");

        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    register();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    browseMovies();
                    break;
                case "4":
                    searchMovies();
                    break;
                case "5":
                    viewAvailableSeats();
                    break;
                case "6":
                    bookTicket();
                    break;
                case "7":
                    makePayment();
                    break;
                case "8":
                    confirmBooking();
                    break;
                case "9":
                    cancelBooking();
                    break;
                case "10":
                    viewBooking();
                    break;
                case "11":
                    verifySingleton();
                    break;
                case "12":
                    adminLogin();
                    break;
                case "13":
                    System.out.println("\nThank you for using Movie Ticket Booking System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\n[!] Invalid choice. Please enter 1-13.\n");
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
        System.out.println("  7. Make Payment (after booking)");
        System.out.println("  8. Confirm Booking (after payment)");
        System.out.println("  9. Cancel Booking");
        System.out.println("  10. View Booking Details");
        System.out.println("  11. Verify Singleton Pattern");
        System.out.println("  12. Admin Login");
        System.out.println("  13. Exit");
        System.out.println("----------------------------------------------");
        if (adminLoggedIn) {
            System.out.println("  Current User: ADMIN (logged in)");
        } else if (loggedInUser == null) {
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

    // OPTION 7: Make Payment
    static void makePayment() {
        System.out.println("\n--- MAKE PAYMENT ---");
        System.out.print("Enter Booking ID: ");
        try {
            int bookingId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get the booking
            Booking booking = BookingManager.getInstance().getBooking(bookingId);
            if (booking == null) {
                System.out.println("[!] Booking not found.");
                return;
            }
            
            // Check if booking is still pending
            if (!booking.getStatus().toString().equals("PENDING")) {
                System.out.println("[!] Booking is not in PENDING status. Current status: " + booking.getStatus());
                return;
            }
            
            // Calculate and display total
            double totalAmount = booking.calculateTotal();
            System.out.println("\n[INFO] Booking Amount: Rs." + totalAmount);
            
            // Display available payment methods
            PaymentProcessor.displayPaymentMethods();
            System.out.print("Select payment method (1-5): ");
            int choice = parseInt(scanner.nextLine().trim(), 1);
            
            if (choice < 1 || choice > 5) {
                System.out.println("[!] Invalid payment method selection.");
                return;
            }
            
            PaymentMethod[] methods = PaymentMethod.values();
            PaymentMethod method = methods[choice - 1];
            
            // Create payment record
            PaymentManager paymentManager = PaymentManager.getInstance();
            Payment payment = paymentManager.createPayment(totalAmount, method, bookingId);
            
            // Get payment details
            System.out.print("Enter payment details (card/UPI/account number): ");
            String details = scanner.nextLine().trim();
            
            if (details.isEmpty()) {
                System.out.println("[!] Payment details cannot be empty.");
                return;
            }
            
            // Process payment
            System.out.println("\n[Processing payment...]\n");
            boolean success = paymentManager.processPayment(payment.getPaymentId(), details);
            
            if (success) {
                System.out.println("\n[SUCCESS] Payment completed successfully!");
                System.out.println("[INFO] Payment ID: " + payment.getPaymentId());
                System.out.println("[INFO] Amount: Rs." + totalAmount);
                System.out.println("[INFO] Method: " + method);
                System.out.println("[INFO] Status: SUCCESS");
                System.out.println("[INFO] Now you can confirm your booking.");
            } else {
                System.out.println("\n[ERROR] Payment failed. Please try again or use a different payment method.");
                System.out.println("[INFO] Payment ID: " + payment.getPaymentId());
                System.out.println("[INFO] Status: FAILED");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid Booking ID or payment method selection.");
        }
    }

    // OPTION 8: Confirm Booking
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

    // ADMIN LOGIN
    static void adminLogin() {
        System.out.println("\n--- ADMIN LOGIN ---");
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter admin password: ");
        String password = scanner.nextLine().trim();

        // Simple admin credentials (hardcoded for demo)
        if ("admin".equals(username) && "admin123".equals(password)) {
            adminLoggedIn = true;
            System.out.println("\n[SUCCESS] Admin login successful!");
            adminMenu();
        } else {
            System.out.println("\n[ERROR] Invalid admin credentials!");
            adminLoggedIn = false;
        }
    }

    // ADMIN MENU
    static void adminMenu() {
        if (!adminLoggedIn) {
            System.out.println("[ERROR] Access denied. Admin not logged in.");
            return;
        }

        AdminService adminService = AdminService.getInstance();
        boolean inAdminMode = true;

        while (inAdminMode && adminLoggedIn) {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("  1. Add Movie");
            System.out.println("  2. Update Movie");
            System.out.println("  3. Delete Movie");
            System.out.println("  4. Add Show");
            System.out.println("  5. Update Show");
            System.out.println("  6. Delete Show");
            System.out.println("  7. Generate Booking Report");
            System.out.println("  8. Logout");
            System.out.println("================================");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("\n--- ADD MOVIE ---");
                    System.out.print("Movie title: ");
                    String title = scanner.nextLine().trim();
                    System.out.print("Genre: ");
                    String genre = scanner.nextLine().trim();
                    System.out.print("Language: ");
                    String language = scanner.nextLine().trim();
                    System.out.print("Duration (in minutes): ");
                    double duration = parseDouble(scanner.nextLine().trim(), 120);
                    System.out.print("Director: ");
                    String director = scanner.nextLine().trim();
                    System.out.print("Cast: ");
                    String cast = scanner.nextLine().trim();
                    System.out.print("Rating (0-10): ");
                    double rating = parseDouble(scanner.nextLine().trim(), 8.0);

                    adminController.handleAddMovie(title, genre, language, duration, director, cast,
                            LocalDate.now(), "New movie added by admin", rating);
                    System.out.println("[SUCCESS] Movie added!");
                    break;

                case "2":
                    System.out.println("\n--- UPDATE MOVIE ---");
                    System.out.print("Movie ID: ");
                    int updateMovieId = parseInt(scanner.nextLine().trim(), 1);
                    System.out.print("New title (or press Enter to skip): ");
                    String newTitle = scanner.nextLine().trim();
                    System.out.print("New rating (0-10, or press Enter to skip): ");
                    String ratingInput = scanner.nextLine().trim();
                    double newRating = ratingInput.isEmpty() ? 8.0 : parseDouble(ratingInput, 8.0);
                    adminController.handleUpdateMovie(updateMovieId, newTitle.isEmpty() ? "Updated Movie" : newTitle, newRating);
                    System.out.println("[SUCCESS] Movie updated!");
                    break;

                case "3":
                    System.out.println("\n--- DELETE MOVIE ---");
                    System.out.print("Movie ID: ");
                    int deleteMovieId = parseInt(scanner.nextLine().trim(), 1);
                    adminController.handleDeleteMovie(deleteMovieId);
                    System.out.println("[SUCCESS] Movie deleted!");
                    break;

                case "4":
                    System.out.println("\n--- ADD SHOW ---");
                    System.out.print("Movie ID: ");
                    int movieId = parseInt(scanner.nextLine().trim(), 1);
                    System.out.print("Auditorium (e.g., Hall A): ");
                    String auditorium = scanner.nextLine().trim();
                    System.out.print("Total seats: ");
                    int totalSeats = parseInt(scanner.nextLine().trim(), 150);
                    System.out.print("Base price: ");
                    double basePrice = parseDouble(scanner.nextLine().trim(), 350.0);

                    adminController.handleAddShow(movieId, LocalDateTime.now().plusDays(1), 
                            auditorium, totalSeats, basePrice, "English", "2D");
                    System.out.println("[SUCCESS] Show added!");
                    break;

                case "5":
                    System.out.println("\n--- UPDATE SHOW PRICE ---");
                    System.out.print("Show ID: ");
                    int showId = parseInt(scanner.nextLine().trim(), 1);
                    System.out.print("New price: ");
                    double newShowPrice = parseDouble(scanner.nextLine().trim(), 350.0);
                    adminController.handleUpdateShow(showId, newShowPrice);
                    System.out.println("[SUCCESS] Show updated!");
                    break;

                case "6":
                    System.out.println("\n--- DELETE SHOW ---");
                    System.out.print("Show ID: ");
                    int deleteShowId = parseInt(scanner.nextLine().trim(), 1);
                    adminController.handleDeleteShow(deleteShowId);
                    System.out.println("[SUCCESS] Show deleted!");
                    break;

                case "7":
                    System.out.println("\n--- GENERATE REPORT ---");
                    adminService.generateBookingReport(LocalDate.now().minusDays(30), LocalDate.now());
                    break;

                case "8":
                    adminLoggedIn = false;
                    inAdminMode = false;
                    System.out.println("\n[SUCCESS] Admin logged out!");
                    break;

                default:
                    System.out.println("\n[!] Invalid choice.");
            }
        }
    }

    // Helper method to parse integer
    static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Helper method to parse double
    static double parseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
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
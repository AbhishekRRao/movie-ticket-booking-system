import controller.BookingController;
import model.Booking;
import model.Payment;
import model.Show;
import model.Seat;
import enums.PaymentMethod;
import payment.PaymentProcessor;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * INTERACTIVE TEST - Manual Testing Interface
 * Allows you to test all features of the payment & notification system
 */
public class InteractiveTest {

    static Scanner scanner = new Scanner(System.in);
    static BookingController controller = new BookingController();
    static Show show = createDefaultShow();
    static boolean notificationsSubscribed = false;

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  INTERACTIVE TEST - Payment & Notifications                    ║");
        System.out.println("║  Test all features of your implementation                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewAvailableSeats();
                case "2" -> bookTicket();
                case "3" -> processPayment();
                case "4" -> confirmBooking();
                case "5" -> cancelBooking();
                case "6" -> subscribeNotifications();
                case "7" -> viewAllBookings();
                case "8" -> viewAllPayments();
                case "9" -> testAdapterPattern();
                case "10" -> testObserverPattern();
                case "11" -> {
                    System.out.println("\n✓ Thank you for testing! Goodbye!\n");
                    running = false;
                }
                default -> System.out.println("\n[!] Invalid choice. Please try again.\n");
            }
        }
        scanner.close();
    }

    static void printMainMenu() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("  MAIN MENU - What would you like to test?");
        System.out.println("═".repeat(60));
        System.out.println("  1. View Available Seats");
        System.out.println("  2. Book Ticket");
        System.out.println("  3. Process Payment (Adapter Pattern Demo)");
        System.out.println("  4. Confirm Booking (Triggers Observer Pattern)");
        System.out.println("  5. Cancel Booking (Triggers Notifications)");
        System.out.println("  6. Subscribe to Notifications (Observer Pattern Setup)");
        System.out.println("  7. View All Bookings");
        System.out.println("  8. View All Payments");
        System.out.println("  9. Test Adapter Pattern (All Payment Methods)");
        System.out.println("  10. Test Observer Pattern (All Notification Types)");
        System.out.println("  11. Exit");
        System.out.println("═".repeat(60));
        System.out.print("Enter your choice (1-11): ");
    }

    static void viewAvailableSeats() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("AVAILABLE SEATS FOR SHOW");
        System.out.println("─".repeat(60));
        controller.viewAvailableSeats(show);
    }

    static void bookTicket() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("BOOK TICKET");
        System.out.println("─".repeat(60));

        System.out.print("Enter Customer ID: ");
        int customerId;
        try {
            customerId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid Customer ID");
            return;
        }

        controller.viewAvailableSeats(show);

        System.out.print("\nEnter seat numbers (comma-separated, e.g., A1,A2): ");
        String seatInput = scanner.nextLine().trim().toUpperCase();
        if (seatInput.isEmpty()) {
            System.out.println("[!] No seats entered");
            return;
        }
        List<String> seatNumbers = Arrays.asList(seatInput.split(","));

        System.out.print("Enter booking date (e.g., 2026-04-25): ");
        String date = scanner.nextLine().trim();

        Booking booking = controller.bookTicket(customerId, show, seatNumbers, date);
        if (booking != null) {
            System.out.println("\n✓ Booking created!");
            System.out.println("  Booking ID: " + booking.getBookingId());
            System.out.println("  Total: Rs. " + booking.getTotalAmount());
        }
    }

    static void processPayment() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("PROCESS PAYMENT (ADAPTER PATTERN)");
        System.out.println("─".repeat(60));

        System.out.print("Enter Booking ID: ");
        int bookingId;
        try {
            bookingId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid Booking ID");
            return;
        }

        System.out.println("\nAvailable Payment Methods:");
        PaymentProcessor.displayPaymentMethods();

        System.out.print("Select payment method (1-5): ");
        int methodChoice;
        try {
            methodChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid choice");
            return;
        }

        PaymentMethod[] methods = PaymentMethod.values();
        if (methodChoice < 1 || methodChoice > methods.length) {
            System.out.println("[!] Invalid payment method");
            return;
        }

        PaymentMethod method = methods[methodChoice - 1];

        System.out.print("Enter account details (card number / UPI ID / etc.): ");
        String accountDetails = scanner.nextLine().trim();

        System.out.println("\nProcessing payment via " + method + "...");
        Payment payment = controller.processPayment(bookingId, method, accountDetails);

        if (payment != null) {
            System.out.println("\n✓ Payment successful!");
            System.out.println("  Payment ID: " + payment.getPaymentId());
            System.out.println("  Amount: Rs. " + payment.getAmount());
            System.out.println("  Status: " + payment.getStatus());
        }
    }

    static void confirmBooking() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("CONFIRM BOOKING (OBSERVER PATTERN DEMO)");
        System.out.println("─".repeat(60));

        if (!notificationsSubscribed) {
            System.out.println("[INFO] Notifications not subscribed.");
            System.out.println("[INFO] Subscribing you to notifications first...");
            subscribeNotifications();
            System.out.println();
        }

        System.out.print("Enter Booking ID to confirm: ");
        int bookingId;
        try {
            bookingId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid Booking ID");
            return;
        }

        System.out.println("\nConfirming booking and triggering all observers...");
        controller.confirmBooking(bookingId);
    }

    static void cancelBooking() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("CANCEL BOOKING (OBSERVER PATTERN DEMO)");
        System.out.println("─".repeat(60));

        if (!notificationsSubscribed) {
            System.out.println("[INFO] Notifications not subscribed.");
            System.out.println("[INFO] Subscribing you to notifications first...");
            subscribeNotifications();
            System.out.println();
        }

        System.out.print("Enter Booking ID to cancel: ");
        int bookingId;
        try {
            bookingId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid Booking ID");
            return;
        }

        System.out.println("\nCancelling booking and triggering all observers...");
        controller.cancelBooking(bookingId);
    }

    static void subscribeNotifications() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("SUBSCRIBE TO NOTIFICATIONS (OBSERVER PATTERN)");
        System.out.println("─".repeat(60));

        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Enter your User ID: ");
        String userId = scanner.nextLine().trim();

        System.out.println("\nSubscribing to 3 notification channels:");
        controller.subscribeToNotifications(email, phone, userId);

        notificationsSubscribed = true;
        System.out.println("\n✓ You will receive notifications via Email, SMS, and In-App!");
    }

    static void viewAllBookings() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("ALL BOOKINGS IN SYSTEM");
        System.out.println("─".repeat(60));
        controller.viewAllBookings();
    }

    static void viewAllPayments() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("ALL PAYMENTS PROCESSED");
        System.out.println("─".repeat(60));
        controller.viewAllPayments();
    }

    static void testAdapterPattern() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("ADAPTER PATTERN TEST - All Payment Gateways");
        System.out.println("═".repeat(60));

        System.out.print("Enter a booking ID to test with: ");
        int bookingId;
        try {
            bookingId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid Booking ID");
            return;
        }

        PaymentMethod[] methods = PaymentMethod.values();
        for (int i = 0; i < methods.length; i++) {
            System.out.println("\n[Test " + (i + 1) + "/" + methods.length + "] Testing " + methods[i]);
            System.out.println("─".repeat(60));

            String testAccountDetails = switch (methods[i]) {
                case CREDIT_CARD -> "4111-1111-1111-1111";
                case DEBIT_CARD -> "4111-2222-2222-2222";
                case UPI -> "customer@upi";
                case NET_BANKING -> "BANK_001";
                case WALLET -> "WALLET_USER_001";
            };

            Payment payment = controller.processPayment(bookingId, methods[i], testAccountDetails);
            if (payment != null) {
                System.out.println("✓ " + methods[i] + " adapter processed successfully!");
            } else {
                System.out.println("✗ " + methods[i] + " adapter failed");
            }
        }

        System.out.println("\n" + "═".repeat(60));
        System.out.println("Adapter Pattern test completed!");
        System.out.println("═".repeat(60));
    }

    static void testObserverPattern() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("OBSERVER PATTERN TEST - All Notification Types");
        System.out.println("═".repeat(60));

        System.out.print("Enter email for Email Observer: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter phone for SMS Observer: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Enter User ID for In-App Observer: ");
        String userId = scanner.nextLine().trim();

        System.out.print("Enter a Booking ID to test with: ");
        int bookingId;
        try {
            bookingId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid Booking ID");
            return;
        }

        System.out.println("\nSubscribing test observers and confirming booking...");
        controller.subscribeToNotifications(email, phone, userId);

        System.out.println("\n" + "─".repeat(60));
        System.out.println("Triggering confirmation notifications...");
        System.out.println("─".repeat(60));
        controller.confirmBooking(bookingId);

        System.out.println("\n" + "═".repeat(60));
        System.out.println("Observer Pattern test completed!");
        System.out.println("═".repeat(60));
    }

    static Show createDefaultShow() {
        Show s = new Show(1, "7:00 PM", "2026-04-25");
        s.addSeat(new Seat(1, "A1", "REGULAR", 150.0));
        s.addSeat(new Seat(2, "A2", "REGULAR", 150.0));
        s.addSeat(new Seat(3, "B1", "VIP", 350.0));
        s.addSeat(new Seat(4, "B2", "VIP", 350.0));
        s.addSeat(new Seat(5, "C1", "PREMIUM", 500.0));
        return s;
    }
}

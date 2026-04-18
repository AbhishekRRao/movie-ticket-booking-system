package controller;

import model.Booking;
import model.Payment;
import model.Show;
import view.BookingView;
import enums.PaymentMethod;
import notification.NotificationManager;

import java.util.List;

/**
 * CONTROLLER - BookingController
 *
 * Acts as the bridge between View and Model.
 * Handles all user requests related to booking.
 *
 * MVC Role    : Controller
 * Design Principle: SRP - Only responsible for handling booking-related requests.
 * Design Principle: OCP - New booking actions can be added without modifying existing methods.
 */
public class BookingController {

    // MVC: Controller holds reference to View and uses Model via BookingManager
    private BookingView bookingView;
    private BookingService bookingService;

    public BookingController() {
        this.bookingView = new BookingView();
        this.bookingService = new BookingService();
    }

    /**
     * Show available seats for a show.
     * Called when customer wants to select seats.
     */
    public void viewAvailableSeats(Show show) {
        bookingView.displayAvailableSeats(show);
    }

    /**
     * Show all seats (including booked) for a show.
     */
    public void viewAllSeats(Show show) {
        bookingView.displayAllSeats(show);
    }

    /**
     * Book tickets for a customer.
     * Handles seat selection + booking creation + prevents double booking.
     */
    public Booking bookTicket(int customerId, Show show, List<String> seatNumbers, String date) {
        System.out.println("\n[BookingController] Initiating booking for Customer #" + customerId);

        // Check seat availability before creating booking
        for (String seatNumber : seatNumbers) {
            if (!bookingService.isSeatAvailable(show, seatNumber)) {
                bookingView.displayError("Seat " + seatNumber + " is not available. Booking aborted.");
                return null;
            }
        }

        // Create booking via BookingManager (Singleton)
        Booking booking = bookingService.createBooking(customerId, show, seatNumbers, date);

        if (booking == null) {
            bookingView.displayError("Booking could not be created. Please try again.");
            return null;
        }

        // Display confirmation
        bookingView.displayBookingConfirmation(booking);
        return booking;
    }

    /**
     * Confirm a booking after payment is done.
     */
    public void confirmBooking(int bookingId) {
        System.out.println("\n[BookingController] Confirming Booking #" + bookingId);
        boolean success = bookingService.confirmBooking(bookingId);
        if (success) {
            Booking booking = bookingService.getBooking(bookingId);
            bookingView.displayBookingConfirmation(booking);
            bookingView.displaySuccess("Booking #" + bookingId + " is now CONFIRMED!");
        } else {
            bookingView.displayError("Could not confirm Booking #" + bookingId);
        }
    }

    /**
     * Cancel an existing booking.
     */
    public void cancelBooking(int bookingId) {
        System.out.println("\n[BookingController] Cancelling Booking #" + bookingId);
        boolean success = bookingService.cancelBooking(bookingId);
        bookingView.displayCancellationMessage(bookingId, success);
    }

    /**
     * View booking details by ID.
     */
    public void viewBooking(int bookingId) {
        Booking booking = bookingService.getBooking(bookingId);
        if (booking == null) {
            bookingView.displayError("Booking #" + bookingId + " not found.");
        } else {
            bookingView.displayBookingConfirmation(booking);
        }
    }

    /**
     * Process payment for a booking (Adapter Pattern).
     * Creates a payment and processes it using the selected payment gateway.
     */
    public Payment processPayment(int bookingId, PaymentMethod method, String accountDetails) {
        System.out.println("\n[BookingController] Processing payment for Booking #" + bookingId);

        Booking booking = bookingService.getBooking(bookingId);
        if (booking == null) {
            bookingView.displayError("Booking #" + bookingId + " not found.");
            return null;
        }

        // Create payment
        Payment payment = bookingService.createPayment(booking.getTotalAmount(), method, bookingId);

        // Process payment using Adapter Pattern
        boolean success = bookingService.processPayment(payment.getPaymentId(), accountDetails);

        if (success) {
            bookingView.displaySuccess("Payment of Rs. " + payment.getAmount() + " processed successfully!");
            bookingView.displaySuccess("Payment ID: " + payment.getPaymentId() + " | Status: " + payment.getStatus());
            return payment;
        } else {
            bookingView.displayError("Payment processing failed. Please try again with different account details.");
            return null;
        }
    }

    /**
     * Refund payment for a cancelled booking.
     */
    public boolean refundPayment(int bookingId, int paymentId, String transactionId) {
        System.out.println("\n[BookingController] Processing refund for Booking #" + bookingId);

        boolean success = bookingService.refundPayment(paymentId, transactionId);

        if (success) {
            Payment payment = bookingService.getPayment(paymentId);
            if (payment != null) {
                bookingView.displaySuccess("Refund of Rs. " + payment.getAmount() + " processed successfully!");
                bookingView.displaySuccess("Payment ID: " + paymentId + " | Status: " + payment.getStatus());
            }
        } else {
            bookingView.displayError("Refund processing failed.");
        }

        return success;
    }

    /**
     * Subscribe a customer to notifications (Observer Pattern).
     * Adds observers for different notification types.
     */
    public void subscribeToNotifications(String email, String phone, String userId) {
        System.out.println("\n[BookingController] Subscribing to notifications...");
        bookingService.subscribeToNotifications(email, phone, userId);

        NotificationManager notificationManager = bookingService.getNotificationManager();

        notificationManager.displayActiveObservers();
    }

    /**
     * Display all processed payments.
     */
    public void viewAllPayments() {
        bookingService.displayAllPayments();
    }

    /**
     * Display all bookings.
     */
    public void viewAllBookings() {
        bookingService.displayAllBookings();
    }
}

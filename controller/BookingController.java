package controller;

import model.Booking;
import model.Show;
import singleton.BookingManager;
import singleton.SeatAllocator;
import view.BookingView;

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
    private BookingManager bookingManager;
    private SeatAllocator seatAllocator;

    public BookingController() {
        this.bookingView = new BookingView();
        this.bookingManager = BookingManager.getInstance(); // Singleton
        this.seatAllocator = SeatAllocator.getInstance();   // Singleton
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
            if (!seatAllocator.isSeatAvailable(show, seatNumber)) {
                bookingView.displayError("Seat " + seatNumber + " is not available. Booking aborted.");
                return null;
            }
        }

        // Create booking via BookingManager (Singleton)
        Booking booking = bookingManager.createBooking(customerId, show, seatNumbers, date);

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
        boolean success = bookingManager.confirmBooking(bookingId);
        if (success) {
            Booking booking = bookingManager.getBooking(bookingId);
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
        boolean success = bookingManager.cancelBooking(bookingId);
        bookingView.displayCancellationMessage(bookingId, success);
    }

    /**
     * View booking details by ID.
     */
    public void viewBooking(int bookingId) {
        Booking booking = bookingManager.getBooking(bookingId);
        if (booking == null) {
            bookingView.displayError("Booking #" + bookingId + " not found.");
        } else {
            bookingView.displayBookingConfirmation(booking);
        }
    }
}

package view;

import model.Booking;
import model.Seat;
import model.Show;

import java.util.List;

/**
 * VIEW - BookingView
 * 
 * Responsible ONLY for displaying information to the user.
 * Design Principle: SRP - Only handles UI/display logic.
 * Design Principle: OCP - New display methods can be added without changing existing ones.
 */
public class BookingView {

    // Display available seats for a show
    public void displayAvailableSeats(Show show) {
        System.out.println("\n========================================");
        System.out.println("  Available Seats for " + show);
        System.out.println("========================================");
        List<Seat> available = show.getAvailableSeats();
        if (available.isEmpty()) {
            System.out.println("  No seats available for this show.");
        } else {
            for (Seat seat : available) {
                System.out.println("  " + seat);
            }
        }
        System.out.println("========================================\n");
    }

    // Display all seats for a show (including booked)
    public void displayAllSeats(Show show) {
        System.out.println("\n========================================");
        System.out.println("  All Seats for " + show);
        System.out.println("========================================");
        for (Seat seat : show.getSeats()) {
            System.out.println("  " + seat);
        }
        System.out.println("========================================\n");
    }

    // Display booking confirmation details
    public void displayBookingConfirmation(Booking booking) {
        System.out.println("\n========================================");
        System.out.println("       BOOKING CONFIRMATION");
        System.out.println("========================================");
        System.out.println("  Booking ID   : " + booking.getBookingId());
        System.out.println("  Customer ID  : " + booking.getCustomerId());
        System.out.println("  Date         : " + booking.getBookingDate());
        System.out.println("  Status       : " + booking.getStatus());
        System.out.println("  Seats Booked :");
        for (Seat seat : booking.getBookedSeats()) {
            System.out.println("    -> " + seat);
        }
        System.out.println("  Total Amount : Rs." + booking.getTotalAmount());
        System.out.println("========================================\n");
    }

    // Display booking cancellation message
    public void displayCancellationMessage(int bookingId, boolean success) {
        System.out.println("\n========================================");
        if (success) {
            System.out.println("  Booking #" + bookingId + " CANCELLED successfully.");
        } else {
            System.out.println("  ERROR: Could not cancel Booking #" + bookingId);
        }
        System.out.println("========================================\n");
    }

    // Display error message
    public void displayError(String message) {
        System.out.println("\n[ERROR] " + message + "\n");
    }

    // Display success message
    public void displaySuccess(String message) {
        System.out.println("\n[SUCCESS] " + message + "\n");
    }
}

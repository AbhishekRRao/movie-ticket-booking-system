package notification;

import model.Booking;

/**
 * CONCRETE OBSERVER - EmailNotification
 * Sends email notifications when booking status changes.
 *
 * Design Pattern: Observer Pattern
 * This is a concrete observer that implements the BookingObserver interface.
 */
public class EmailNotification implements BookingObserver {

    private String customerEmail;

    public EmailNotification(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("\n[EmailNotification] Sending confirmation email to: " + customerEmail);
        System.out.println("========== BOOKING CONFIRMATION EMAIL ==========");
        System.out.println("Dear Customer,");
        System.out.println("\nYour booking has been successfully confirmed!");
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Booking Date: " + booking.getBookingDate());
        System.out.println("Total Amount: Rs. " + booking.getTotalAmount());
        System.out.println("Seats: " + booking.getBookedSeats().size());
        System.out.println("Status: " + booking.getStatus());
        System.out.println("\nThank you for booking with us!");
        System.out.println("================================================");
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("\n[EmailNotification] Sending cancellation email to: " + customerEmail);
        System.out.println("========== BOOKING CANCELLATION EMAIL ==========");
        System.out.println("Dear Customer,");
        System.out.println("\nYour booking has been cancelled.");
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Refund Amount: Rs. " + booking.getTotalAmount());
        System.out.println("Status: " + booking.getStatus());
        System.out.println("\nYour refund will be processed within 3-5 business days.");
        System.out.println("================================================");
    }

    @Override
    public String getObserverType() {
        return "Email";
    }
}

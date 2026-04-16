package notification;

import model.Booking;

/**
 * CONCRETE OBSERVER - InAppNotification
 * Sends in-app notifications when booking status changes.
 *
 * Design Pattern: Observer Pattern
 * This is a concrete observer that implements the BookingObserver interface.
 */
public class InAppNotification implements BookingObserver {

    private String userId;

    public InAppNotification(String userId) {
        this.userId = userId;
    }

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("\n[InAppNotification] Pushing notification to user: " + userId);
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║  BOOKING CONFIRMED                 ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Booking ID: " + String.format("%-18s", booking.getBookingId()) + "║");
        System.out.println("║ Date: " + String.format("%-24s", booking.getBookingDate()) + "║");
        System.out.println("║ Amount: Rs. " + String.format("%-18.2f", booking.getTotalAmount()) + "║");
        System.out.println("║ Seats: " + String.format("%-23s", booking.getBookedSeats().size()) + "║");
        System.out.println("║ Status: " + String.format("%-21s", booking.getStatus()) + "║");
        System.out.println("╚════════════════════════════════════╝");
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("\n[InAppNotification] Pushing notification to user: " + userId);
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║  BOOKING CANCELLED                 ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Booking ID: " + String.format("%-18s", booking.getBookingId()) + "║");
        System.out.println("║ Refund Amount: Rs. " + String.format("%-11.2f", booking.getTotalAmount()) + "║");
        System.out.println("║ Status: " + String.format("%-21s", booking.getStatus()) + "║");
        System.out.println("║ Processing in 3-5 days              ║");
        System.out.println("╚════════════════════════════════════╝");
    }

    @Override
    public String getObserverType() {
        return "In-App";
    }
}

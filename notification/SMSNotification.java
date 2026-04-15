package notification;

import model.Booking;

/**
 * CONCRETE OBSERVER - SMSNotification
 * Sends SMS notifications when booking status changes.
 *
 * Design Pattern: Observer Pattern
 * This is a concrete observer that implements the BookingObserver interface.
 */
public class SMSNotification implements BookingObserver {

    private String phoneNumber;

    public SMSNotification(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("\n[SMSNotification] Sending SMS to: " + maskPhoneNumber(phoneNumber));
        System.out.println("----------- SMS MESSAGE -----------");
        System.out.println("Booking confirmed!");
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Amount: Rs. " + booking.getTotalAmount());
        System.out.println("Seats: " + booking.getBookedSeats().size());
        System.out.println("Thank you!");
        System.out.println("-----------------------------------");
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("\n[SMSNotification] Sending cancellation SMS to: " + maskPhoneNumber(phoneNumber));
        System.out.println("----------- SMS MESSAGE -----------");
        System.out.println("Booking cancelled!");
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Refund: Rs. " + booking.getTotalAmount());
        System.out.println("Refund in 3-5 business days.");
        System.out.println("-----------------------------------");
    }

    @Override
    public String getObserverType() {
        return "SMS";
    }

    // Mask phone number for privacy
    private String maskPhoneNumber(String phone) {
        if (phone.length() >= 4) {
            return "****" + phone.substring(phone.length() - 4);
        }
        return "****";
    }
}

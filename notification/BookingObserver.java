package notification;

import model.Booking;

/**
 * INTERFACE - BookingObserver
 * Observer interface for booking confirmation notifications.
 *
 * Design Pattern: Observer Pattern
 * This is the Observer interface that all concrete observers must implement.
 * Used for loose coupling between Booking (Subject) and Notification handlers (Observers).
 */
public interface BookingObserver {
    /**
     * Notify observer when a booking is confirmed.
     * @param booking The booking that was confirmed
     */
    void onBookingConfirmed(Booking booking);

    /**
     * Notify observer when a booking is cancelled.
     * @param booking The booking that was cancelled
     */
    void onBookingCancelled(Booking booking);

    /**
     * Get the observer type name.
     * @return the observer type (e.g., "Email", "SMS")
     */
    String getObserverType();
}

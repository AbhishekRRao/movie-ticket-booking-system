package notification;

import model.Booking;
import java.util.ArrayList;
import java.util.List;

/**
 * SUBJECT - NotificationManager
 * Manages observers and notifies them when booking status changes.
 *
 * Design Pattern: Observer Pattern
 * This is the Subject that maintains a list of observers and notifies them
 * when booking events occur (confirmation, cancellation).
 * Follows SRP: Only responsible for managing observers and sending notifications.
 */
public class NotificationManager {

    private List<BookingObserver> observers = new ArrayList<>();

    /**
     * Subscribe an observer to booking notifications.
     * @param observer The observer to subscribe
     */
    public void subscribe(BookingObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("[NotificationManager] Observer subscribed: " + observer.getObserverType());
        }
    }

    /**
     * Unsubscribe an observer from booking notifications.
     * @param observer The observer to unsubscribe
     */
    public void unsubscribe(BookingObserver observer) {
        if (observers.remove(observer)) {
            System.out.println("[NotificationManager] Observer unsubscribed: " + observer.getObserverType());
        }
    }

    /**
     * Notify all observers that a booking has been confirmed.
     * @param booking The confirmed booking
     */
    public void notifyBookingConfirmed(Booking booking) {
        System.out.println("\n[NotificationManager] Notifying " + observers.size() + " observer(s) of booking confirmation...");
        for (BookingObserver observer : observers) {
            observer.onBookingConfirmed(booking);
        }
    }

    /**
     * Notify all observers that a booking has been cancelled.
     * @param booking The cancelled booking
     */
    public void notifyBookingCancelled(Booking booking) {
        System.out.println("\n[NotificationManager] Notifying " + observers.size() + " observer(s) of booking cancellation...");
        for (BookingObserver observer : observers) {
            observer.onBookingCancelled(booking);
        }
    }

    /**
     * Get the number of active observers.
     * @return the count of observers
     */
    public int getObserverCount() {
        return observers.size();
    }

    /**
     * Display all active observers.
     */
    public void displayActiveObservers() {
        System.out.println("\nActive Observers: " + observers.size());
        for (BookingObserver observer : observers) {
            System.out.println("  - " + observer.getObserverType());
        }
    }
}

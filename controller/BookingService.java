package controller;

import enums.PaymentMethod;
import model.Booking;
import model.Payment;
import model.Show;
import notification.EmailNotification;
import notification.InAppNotification;
import notification.NotificationManager;
import notification.SMSNotification;
import singleton.BookingManager;
import singleton.PaymentManager;
import singleton.SeatAllocator;

import java.util.List;

/**
 * Service layer for booking and payment workflows.
 */
public class BookingService {

    private final BookingManager bookingManager;
    private final PaymentManager paymentManager;
    private final SeatAllocator seatAllocator;

    public BookingService() {
        this.bookingManager = BookingManager.getInstance();
        this.paymentManager = PaymentManager.getInstance();
        this.seatAllocator = SeatAllocator.getInstance();
    }

    public boolean isSeatAvailable(Show show, String seatNumber) {
        return seatAllocator.isSeatAvailable(show, seatNumber);
    }

    public Booking createBooking(int customerId, Show show, List<String> seatNumbers, String date) {
        return bookingManager.createBooking(customerId, show, seatNumbers, date);
    }

    public boolean confirmBooking(int bookingId) {
        return bookingManager.confirmBooking(bookingId);
    }

    public boolean cancelBooking(int bookingId) {
        return bookingManager.cancelBooking(bookingId);
    }

    public Booking getBooking(int bookingId) {
        return bookingManager.getBooking(bookingId);
    }

    public Payment createPayment(double amount, PaymentMethod method, int bookingId) {
        return paymentManager.createPayment(amount, method, bookingId);
    }

    public boolean processPayment(int paymentId, String accountDetails) {
        return paymentManager.processPayment(paymentId, accountDetails);
    }

    public boolean refundPayment(int paymentId, String transactionId) {
        return paymentManager.refundPayment(paymentId, transactionId);
    }

    public Payment getPayment(int paymentId) {
        return paymentManager.getPayment(paymentId);
    }

    public void subscribeToNotifications(String email, String phone, String userId) {
        NotificationManager notificationManager = bookingManager.getNotificationManager();
        notificationManager.subscribe(new EmailNotification(email));
        notificationManager.subscribe(new SMSNotification(phone));
        notificationManager.subscribe(new InAppNotification(userId));
    }

    public NotificationManager getNotificationManager() {
        return bookingManager.getNotificationManager();
    }

    public void displayAllPayments() {
        paymentManager.displayAllPayments();
    }

    public void displayAllBookings() {
        bookingManager.displayAllBookings();
    }
}

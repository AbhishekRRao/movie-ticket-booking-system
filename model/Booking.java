package model;

import enums.BookingStatus;
import java.util.List;
import java.util.ArrayList;

/**
 * MODEL - Booking
 * SRP: Only holds booking data and booking-level calculations.
 * Matches the Booking class from the class diagram.
 */
public class Booking {
    private int bookingId;
    private String bookingDate;
    private double totalAmount;
    private BookingStatus status;
    private int customerId;
    private List<Seat> bookedSeats;

    public Booking(int bookingId, int customerId, String bookingDate) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.bookingDate = bookingDate;
        this.status = BookingStatus.PENDING;  // Always starts as PENDING
        this.bookedSeats = new ArrayList<>();
    }

    // Add a seat to this booking
    public void addSeat(Seat seat) {
        bookedSeats.add(seat);
    }

    // Calculate total based on seats added
    public double calculateTotal() {
        totalAmount = 0;
        for (Seat seat : bookedSeats) {
            totalAmount += seat.getPrice();
        }
        return totalAmount;
    }

    // Confirm the booking
    public void confirmBooking() {
        this.status = BookingStatus.CONFIRMED;
    }

    // Cancel the booking
    public void cancelBooking() {
        this.status = BookingStatus.CANCELLED;
        // Release all seats back
        for (Seat seat : bookedSeats) {
            seat.releaseSeat();
        }
    }

    // Getters
    public int getBookingId() { return bookingId; }
    public String getBookingDate() { return bookingDate; }
    public double getTotalAmount() { return totalAmount; }
    public BookingStatus getStatus() { return status; }
    public int getCustomerId() { return customerId; }
    public List<Seat> getBookedSeats() { return bookedSeats; }

    @Override
    public String toString() {
        return "Booking[ID:" + bookingId + " | Customer:" + customerId +
               " | Status:" + status + " | Total:Rs." + totalAmount + "]";
    }
}

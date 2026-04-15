package singleton;

import model.Booking;
import model.Seat;
import model.Show;
import enums.BookingStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SINGLETON PATTERN - BookingManager
 *
 * The central manager for ALL booking operations.
 * Only ONE instance exists — ensures consistent booking state.
 *
 * Design Pattern : Singleton (Thread-safe)
 * Design Principle: SRP - Only responsible for managing bookings.
 * Design Principle: OCP - New booking types (Group, VIP) can extend
 *                         without modifying this class.
 */
public class BookingManager {

    // Single instance
    private static volatile BookingManager instance = null;

    // In-memory store (simulates DB)
    private Map<Integer, Booking> bookingStore;
    private int bookingCounter;

    // Dependencies (Singletons)
    private DBConnection dbConnection;
    private SeatAllocator seatAllocator;

    // Private constructor
    private BookingManager() {
        this.bookingStore = new HashMap<>();
        this.bookingCounter = 1000; // Booking IDs start from 1000
        this.dbConnection = DBConnection.getInstance();
        this.seatAllocator = SeatAllocator.getInstance();
        System.out.println("[BookingManager] Initialized successfully.");
    }

    // Global access point
    public static BookingManager getInstance() {
        if (instance == null) {
            synchronized (BookingManager.class) {
                if (instance == null) {
                    instance = new BookingManager();
                }
            }
        }
        return instance;
    }

    /**
     * Create a new booking for a customer.
     * Allocates seats and prevents double booking.
     */
    public Booking createBooking(int customerId, Show show, List<String> seatNumbers, String date) {
        // Generate new booking ID
        int bookingId = ++bookingCounter;
        Booking booking = new Booking(bookingId, customerId, date);

        // Allocate each requested seat
        for (String seatNumber : seatNumbers) {
            Seat seat = seatAllocator.allocateSeat(show, seatNumber);
            if (seat == null) {
                System.out.println("[BookingManager] Booking FAILED: Could not allocate seat " + seatNumber);
                // Release any already allocated seats
                for (Seat s : booking.getBookedSeats()) {
                    seatAllocator.releaseSeat(s);
                }
                return null;
            }
            booking.addSeat(seat);
        }

        // Calculate total amount
        booking.calculateTotal();

        // Save to store and DB
        bookingStore.put(bookingId, booking);
        dbConnection.save(booking.toString());

        System.out.println("[BookingManager] Booking created: " + booking);
        return booking;
    }

    /**
     * Confirm a pending booking (after payment).
     */
    public boolean confirmBooking(int bookingId) {
        Booking booking = bookingStore.get(bookingId);
        if (booking == null) {
            System.out.println("[BookingManager] Booking ID " + bookingId + " not found.");
            return false;
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            System.out.println("[BookingManager] Booking " + bookingId + " is not in PENDING state.");
            return false;
        }

        // Confirm all seats
        for (Seat seat : booking.getBookedSeats()) {
            seatAllocator.confirmSeat(seat);
        }

        booking.confirmBooking();
        dbConnection.save("CONFIRMED: " + booking.toString());
        System.out.println("[BookingManager] Booking CONFIRMED: " + booking);
        return true;
    }

    /**
     * Cancel an existing booking and release seats.
     */
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingStore.get(bookingId);
        if (booking == null) {
            System.out.println("[BookingManager] Booking ID " + bookingId + " not found.");
            return false;
        }

        booking.cancelBooking(); // Also releases all seats inside
        dbConnection.save("CANCELLED: " + booking.toString());
        System.out.println("[BookingManager] Booking CANCELLED: " + booking);
        return true;
    }

    /**
     * Retrieve a booking by ID.
     */
    public Booking getBooking(int bookingId) {
        return bookingStore.get(bookingId);
    }

    /**
     * Get all bookings for a customer.
     */
    public List<Booking> getBookingsByCustomer(int customerId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookingStore.values()) {
            if (b.getCustomerId() == customerId) {
                result.add(b);
            }
        }
        return result;
    }
}

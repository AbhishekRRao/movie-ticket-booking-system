package singleton;

import model.Booking;
import model.Seat;
import model.Show;
import enums.BookingStatus;
import enums.SeatStatus;
import notification.NotificationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    private int bookingCounter;

    // Dependencies (Singletons)
    private final DBConnection dbConnection;
    private final SeatAllocator seatAllocator;
    private final NotificationManager notificationManager;

    // Private constructor
    private BookingManager() {
        this.bookingCounter = 1000;
        this.dbConnection = DBConnection.getInstance();
        this.seatAllocator = SeatAllocator.getInstance();
        this.notificationManager = new NotificationManager();
        this.bookingCounter = loadMaxBookingId();
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
        saveBooking(booking, show.getShowId());
        saveBookingSeats(booking, show.getShowId());
        dbConnection.save("BOOKING_CREATED: " + bookingId);

        System.out.println("[BookingManager] Booking created: " + booking);
        return booking;
    }

    /**
     * Confirm a pending booking (after payment).
     */
    public boolean confirmBooking(int bookingId) {
        Booking booking = getBooking(bookingId);
        if (booking == null) {
            System.out.println("[BookingManager] Booking ID " + bookingId + " not found.");
            return false;
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            System.out.println("[BookingManager] Booking " + bookingId + " is not in PENDING state.");
            return false;
        }

        Integer showId = getShowIdForBooking(bookingId);
        Show show = showId != null ? MovieCatalog.getInstance().getShowById(showId) : null;

        // Confirm all seats
        for (Seat seat : booking.getBookedSeats()) {
            if (show != null) {
                seatAllocator.confirmSeat(show, seat);
            } else {
                seat.setStatus(SeatStatus.BOOKED);
            }
        }

        booking.confirmBooking();
        updateBookingStatus(bookingId, BookingStatus.CONFIRMED);
        dbConnection.save("BOOKING_CONFIRMED: " + bookingId);
        System.out.println("[BookingManager] Booking CONFIRMED: " + booking);

        // Notify all observers (Observer Pattern)
        notificationManager.notifyBookingConfirmed(booking);

        return true;
    }

    /**
     * Cancel an existing booking and release seats.
     */
    public boolean cancelBooking(int bookingId) {
        Booking booking = getBooking(bookingId);
        if (booking == null) {
            System.out.println("[BookingManager] Booking ID " + bookingId + " not found.");
            return false;
        }

        Integer showId = getShowIdForBooking(bookingId);
        Show show = showId != null ? MovieCatalog.getInstance().getShowById(showId) : null;

        if (show != null) {
            for (Seat seat : booking.getBookedSeats()) {
                seatAllocator.releaseSeat(show, seat);
            }
        }

        // Notify all observers (Observer Pattern)
        notificationManager.notifyBookingCancelled(booking);

        booking.cancelBooking();
        updateBookingStatus(bookingId, BookingStatus.CANCELLED);
        dbConnection.save("BOOKING_CANCELLED: " + bookingId);
        System.out.println("[BookingManager] Booking CANCELLED: " + booking);
        return true;
    }

    /**
     * Retrieve a booking by ID.
     */
    public Booking getBooking(int bookingId) {
        String sql = "SELECT booking_id, customer_id, booking_date, total_amount, status FROM bookings WHERE booking_id = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("customer_id"),
                        rs.getString("booking_date")
                );
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
                loadBookingSeats(booking);
                return booking;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch booking " + bookingId, e);
        }
    }

    /**
     * Get all bookings for a customer.
     */
    public List<Booking> getBookingsByCustomer(int customerId) {
        List<Booking> result = new ArrayList<>();
        String sql = "SELECT booking_id FROM bookings WHERE customer_id = ? ORDER BY booking_id";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking booking = getBooking(rs.getInt("booking_id"));
                    if (booking != null) {
                        result.add(booking);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch customer bookings", e);
        }
        return result;
    }

    /**
     * Get the NotificationManager for subscribing observers.
     */
    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    /**
     * Display all bookings in the store.
     */
    public void displayAllBookings() {
        System.out.println("\n========== All Bookings ==========");
        Collection<Booking> bookings = getAllBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            bookings.forEach(System.out::println);
        }
        System.out.println("=================================");
    }

    /**
     * Get total number of bookings.
     */
    public int getTotalBookings() {
        String sql = "SELECT COUNT(*) AS total FROM bookings";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("total") : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count bookings", e);
        }
    }

    /**
     * Get all bookings (for reporting purposes).
     */
    public java.util.Collection<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT booking_id FROM bookings ORDER BY booking_id";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Booking booking = getBooking(rs.getInt("booking_id"));
                if (booking != null) {
                    bookings.add(booking);
                }
            }
            return bookings;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all bookings", e);
        }
    }

    private int loadMaxBookingId() {
        String sql = "SELECT COALESCE(MAX(booking_id), 1000) AS max_id FROM bookings";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("max_id") : 1000;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize booking counter", e);
        }
    }

    private void saveBooking(Booking booking, int showId) {
        String sql = "INSERT OR REPLACE INTO bookings(booking_id, customer_id, booking_date, total_amount, status, show_id) VALUES(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, booking.getBookingId());
            ps.setInt(2, booking.getCustomerId());
            ps.setString(3, booking.getBookingDate());
            ps.setDouble(4, booking.getTotalAmount());
            ps.setString(5, booking.getStatus().name());
            ps.setInt(6, showId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save booking", e);
        }
    }

    private void saveBookingSeats(Booking booking, int showId) {
        String sql = "INSERT OR REPLACE INTO booking_seats(booking_id, show_id, seat_number, seat_id, seat_type, price) VALUES(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            for (Seat seat : booking.getBookedSeats()) {
                ps.setInt(1, booking.getBookingId());
                ps.setInt(2, showId);
                ps.setString(3, seat.getSeatNumber());
                ps.setInt(4, seat.getSeatId());
                ps.setString(5, seat.getType());
                ps.setDouble(6, seat.getPrice());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save booking seats", e);
        }
    }

    private void loadBookingSeats(Booking booking) {
        String sql = "SELECT bs.seat_id, bs.seat_number, bs.seat_type, bs.price, s.status "
                + "FROM booking_seats bs "
                + "LEFT JOIN seats s ON s.show_id = bs.show_id AND s.seat_number = bs.seat_number "
                + "WHERE bs.booking_id = ? ORDER BY bs.seat_number";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, booking.getBookingId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat(
                            rs.getInt("seat_id"),
                            rs.getString("seat_number"),
                            rs.getString("seat_type"),
                            rs.getDouble("price")
                    );
                    String status = rs.getString("status");
                    if (status != null) {
                        seat.setStatus(SeatStatus.valueOf(status));
                    }
                    booking.addSeat(seat);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load booking seats", e);
        }
    }

    private Integer getShowIdForBooking(int bookingId) {
        String sql = "SELECT show_id FROM bookings WHERE booking_id = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int showId = rs.getInt("show_id");
                    return rs.wasNull() ? null : showId;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch show for booking", e);
        }
    }

    private void updateBookingStatus(int bookingId, BookingStatus status) {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update booking status", e);
        }
    }
}

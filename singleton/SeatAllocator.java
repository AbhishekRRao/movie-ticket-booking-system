package singleton;

import model.Seat;
import model.Show;
import enums.SeatStatus;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SeatAllocator - Handles ALL seat allocation logic.
 * 
 * Design Principle: SRP - Only responsible for seat allocation.
 * This is separate from BookingManager to ensure each class has one job.
 * Design Principle: OCP - New seat allocation strategies can be added
 *                         without modifying this class.
 */
public class SeatAllocator {

    private static volatile SeatAllocator instance = null;
    private final DBConnection dbConnection;

    // Private constructor - Singleton
    private SeatAllocator() {
        this.dbConnection = DBConnection.getInstance();
    }

    public static SeatAllocator getInstance() {
        if (instance == null) {
            synchronized (SeatAllocator.class) {
                if (instance == null) {
                    instance = new SeatAllocator();
                }
            }
        }
        return instance;
    }

    /**
     * Check if a seat is available in a show.
     * PREVENTS DOUBLE BOOKING.
     */
    public boolean isSeatAvailable(Show show, String seatNumber) {
        Seat seat = show.getSeatByNumber(seatNumber);
        if (seat == null) {
            System.out.println("[SeatAllocator] Seat " + seatNumber + " not found.");
            return false;
        }
        return seat.getStatus() == SeatStatus.AVAILABLE;
    }

    /**
     * Allocate (reserve) a seat for a show.
     * Returns the Seat if successful, null if already booked.
     */
    public Seat allocateSeat(Show show, String seatNumber) {
        Seat seat = show.getSeatByNumber(seatNumber);

        if (seat == null) {
            System.out.println("[SeatAllocator] ERROR: Seat " + seatNumber + " does not exist.");
            return null;
        }

        // PREVENT DOUBLE BOOKING
        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            System.out.println("[SeatAllocator] ERROR: Seat " + seatNumber +
                               " is already " + seat.getStatus() + ". Cannot book.");
            return null;
        }

        // Reserve the seat
        seat.setStatus(SeatStatus.RESERVED);
        updateSeatStatus(show.getShowId(), seat.getSeatNumber(), SeatStatus.RESERVED);
        System.out.println("[SeatAllocator] Seat " + seatNumber + " reserved successfully.");
        return seat;
    }

    /**
     * Finalize seat as BOOKED after payment/confirmation.
     */
    public void confirmSeat(Seat seat) {
        seat.bookSeat();
        System.out.println("[SeatAllocator] Seat " + seat.getSeatNumber() + " confirmed as BOOKED.");
    }

    /**
     * Release a seat back to AVAILABLE (on cancellation).
     */
    public void releaseSeat(Seat seat) {
        seat.releaseSeat();
        System.out.println("[SeatAllocator] Seat " + seat.getSeatNumber() + " released back to AVAILABLE.");
    }

    public void confirmSeat(Show show, Seat seat) {
        seat.bookSeat();
        updateSeatStatus(show.getShowId(), seat.getSeatNumber(), SeatStatus.BOOKED);
        System.out.println("[SeatAllocator] Seat " + seat.getSeatNumber() + " confirmed as BOOKED.");
    }

    public void releaseSeat(Show show, Seat seat) {
        seat.releaseSeat();
        updateSeatStatus(show.getShowId(), seat.getSeatNumber(), SeatStatus.AVAILABLE);
        System.out.println("[SeatAllocator] Seat " + seat.getSeatNumber() + " released back to AVAILABLE.");
    }

    private void updateSeatStatus(int showId, String seatNumber, SeatStatus status) {
        String sql = "UPDATE seats SET status = ? WHERE show_id = ? AND seat_number = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, showId);
            ps.setString(3, seatNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update seat status", e);
        }
    }
}
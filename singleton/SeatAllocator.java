package singleton;

import model.Seat;
import model.Show;
import enums.SeatStatus;

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

    // Private constructor - Singleton
    private SeatAllocator() {}

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
}
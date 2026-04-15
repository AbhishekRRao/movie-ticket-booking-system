package model;

import java.util.ArrayList;
import java.util.List;
import enums.SeatStatus;

/**
 * MODEL - Show
 * SRP: Only holds show data and provides available seat info.
 * Matches the Show class from the class diagram.
 */
public class Show {
    private int showId;
    private String showTime;
    private String showDate;
    private List<Seat> seats;

    public Show(int showId, String showTime, String showDate) {
        this.showId = showId;
        this.showTime = showTime;
        this.showDate = showDate;
        this.seats = new ArrayList<>();
    }

    // Add a seat to this show
    public void addSeat(Seat seat) {
        seats.add(seat);
    }

    // Get all available seats for this show
    public List<Seat> getAvailableSeats() {
        List<Seat> available = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.getStatus() == SeatStatus.AVAILABLE) {
                available.add(seat);
            }
        }
        return available;
    }

    // Find a seat by seat number
    public Seat getSeatByNumber(String seatNumber) {
        for (Seat seat : seats) {
            if (seat.getSeatNumber().equalsIgnoreCase(seatNumber)) {
                return seat;
            }
        }
        return null;
    }

    // Getters
    public int getShowId() { return showId; }
    public String getShowTime() { return showTime; }
    public String getShowDate() { return showDate; }
    public List<Seat> getSeats() { return seats; }

    @Override
    public String toString() {
        return "Show[ID:" + showId + " | " + showDate + " " + showTime + "]";
    }
}

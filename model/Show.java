package model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import enums.SeatStatus;

/**
 * MODEL - Show
 * SRP: Only holds show data and provides available seat info.
 * Supports both basic booking and extended admin management features.
 */
public class Show {
    private int showId;
    private String showTime;
    private String showDate;
    private List<Seat> seats;
    
    // Extended fields for admin module
    private Movie movie;
    private LocalDateTime showTimeDateTime;
    private String auditorium;
    private int totalSeats;
    private int availableSeats;
    private double basePrice;
    private String language;
    private String format; // 2D, 3D, IMAX
    private boolean isActive;

    // No-arg constructor for builder pattern support
    public Show() {
        this.seats = new ArrayList<>();
        this.isActive = true;
    }
    
    // Original constructor for backward compatibility
    public Show(int showId, String showTime, String showDate) {
        this.showId = showId;
        this.showTime = showTime;
        this.showDate = showDate;
        this.seats = new ArrayList<>();
        this.isActive = true;
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
    public Movie getMovie() { return movie; }
    public LocalDateTime getShowTimeDateTime() { return showTimeDateTime; }
    public String getAuditorium() { return auditorium; }
    public int getTotalSeats() { return totalSeats; }
    public int getAvailableSeatsCount() { return availableSeats; }
    public double getBasePrice() { return basePrice; }
    public String getLanguage() { return language; }
    public String getFormat() { return format; }
    public boolean isActive() { return isActive; }
    
    // Setters (for builder pattern support)
    public void setShowId(int showId) { this.showId = showId; }
    public void setShowTime(String showTime) { this.showTime = showTime; }
    public void setShowDate(String showDate) { this.showDate = showDate; }
    public void setMovie(Movie movie) { this.movie = movie; }
    public void setShowTime(LocalDateTime showTimeDateTime) { this.showTimeDateTime = showTimeDateTime; }
    public void setAuditorium(String auditorium) { this.auditorium = auditorium; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public void setLanguage(String language) { this.language = language; }
    public void setFormat(String format) { this.format = format; }
    public void setActive(boolean active) { this.isActive = active; }

    @Override
    public String toString() {
        return "Show[ID:" + showId + " | " + showDate + " " + showTime + "]";
    }
}

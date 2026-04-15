package model;

import enums.SeatStatus;

/**
 * MODEL - Seat
 * SRP: Only holds seat data and seat-level operations.
 * Matches the Seat class from the class diagram.
 */
public class Seat {
    private int seatId;
    private String seatNumber;
    private String type;         // e.g. "REGULAR", "VIP", "PREMIUM"
    private double price;
    private SeatStatus status;

    public Seat(int seatId, String seatNumber, String type, double price) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.type = type;
        this.price = price;
        this.status = SeatStatus.AVAILABLE; // Default status
    }

    // Book this seat
    public void bookSeat() {
        if (this.status == SeatStatus.AVAILABLE) {
            this.status = SeatStatus.BOOKED;
        }
    }

    // Release this seat back to available
    public void releaseSeat() {
        this.status = SeatStatus.AVAILABLE;
    }

    // Getters
    public int getSeatId() { return seatId; }
    public String getSeatNumber() { return seatNumber; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public SeatStatus getStatus() { return status; }

    // Setter for status
    public void setStatus(SeatStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Seat[" + seatNumber + " | " + type + " | Rs." + price + " | " + status + "]";
    }
}

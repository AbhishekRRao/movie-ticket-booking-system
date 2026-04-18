package admin.report;

import singleton.BookingManager;
import model.Booking;
import enums.BookingStatus;
import java.time.LocalDate;

/**
 * TEMPLATE METHOD PATTERN - Booking Report Generator
 * 
 * Generates booking history reports.
 * Concrete implementation of ReportGenerator for booking data.
 * 
 * Design Principle: Liskov Substitution - Can be used wherever ReportGenerator is expected
 */
public class BookingReportGenerator extends ReportGenerator {
    
    private int totalBookings;
    private int successfulBookings;
    private int cancelledBookings;
    private double totalRevenue;
    
    public BookingReportGenerator(LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate);
    }
    
    @Override
    protected void processData() {
        System.out.println("[BOOKING] Processing booking data...");
        
        // Fetch real bookings from BookingManager
        BookingManager manager = BookingManager.getInstance();
        totalBookings = manager.getTotalBookings();
        
        // Initialize counters
        successfulBookings = 0;
        cancelledBookings = 0;
        totalRevenue = 0.0;
        
        // Iterate through all bookings and count by status
        for (Booking booking : manager.getAllBookings()) {
            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                successfulBookings++;
                totalRevenue += booking.getTotalAmount();
                System.out.println("[BOOKING] Confirmed booking found: Rs." + booking.getTotalAmount());
            } else if (booking.getStatus() == BookingStatus.CANCELLED) {
                cancelledBookings++;
                System.out.println("[BOOKING] Cancelled booking found");
            } else if (booking.getStatus() == BookingStatus.PENDING) {
                System.out.println("[BOOKING] Pending booking found (not counted in stats)");
            }
        }
        
        System.out.println("[BOOKING] Total Bookings: " + totalBookings);
        System.out.println("[BOOKING] Successful: " + successfulBookings + " | Cancelled: " + cancelledBookings);
    }
    
    @Override
    protected void calculateStatistics() {
        System.out.println("[BOOKING] Calculating booking statistics...");
        double successRate = totalBookings > 0 ? (successfulBookings * 100.0 / totalBookings) : 0.0;
        double avgBookingValue = successfulBookings > 0 ? (totalRevenue / successfulBookings) : 0.0;
        
        System.out.println("[BOOKING] Avg Booking Value: " + String.format("%.2f", avgBookingValue));
        System.out.println("[BOOKING] Success Rate: " + String.format("%.2f", successRate) + "%");
    }
    
    @Override
    protected void formatAndDisplay() {
        System.out.println("\n--- Booking Report Details ---");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Successful Bookings: " + successfulBookings);
        System.out.println("Cancelled Bookings: " + cancelledBookings);
        System.out.println("Total Revenue: Rs. " + String.format("%.2f", totalRevenue));
    }
    
    @Override
    protected void generateSummary() {
        System.out.println("\n--- Booking Report Summary ---");
        System.out.println("Period: " + startDate + " to " + endDate);
        System.out.println("Overall Performance: " + (successfulBookings > cancelledBookings ? "Good" : "Poor"));
    }
    
    @Override
    protected String getReportTitle() {
        return "BOOKING HISTORY REPORT";
    }
}

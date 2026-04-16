package admin.report;

import singleton.BookingManager;
import java.time.LocalDate;
import java.util.Map;

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
        // Fetch bookings from BookingManager
        totalBookings = BookingManager.getInstance().getTotalBookings();
        successfulBookings = (int) (totalBookings * 0.85); // Simulated
        cancelledBookings = totalBookings - successfulBookings;
        System.out.println("[BOOKING] Total Bookings: " + totalBookings);
    }
    
    @Override
    protected void calculateStatistics() {
        System.out.println("[BOOKING] Calculating booking statistics...");
        double avgBookingValue = totalRevenue / (successfulBookings > 0 ? successfulBookings : 1);
        System.out.println("[BOOKING] Avg Booking Value: " + String.format("%.2f", avgBookingValue));
        System.out.println("[BOOKING] Success Rate: " + String.format("%.2f", (successfulBookings * 100.0 / totalBookings)) + "%");
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

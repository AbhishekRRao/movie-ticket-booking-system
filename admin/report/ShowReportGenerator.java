package admin.report;

import java.time.LocalDate;
import singleton.MovieCatalog;
import model.Show;
import java.util.List;

/**
 * TEMPLATE METHOD PATTERN - Show Report Generator
 * 
 * Generates show performance and occupancy reports.
 * Concrete implementation of ReportGenerator for show statistics.
 * 
 * Design Principle: Liskov Substitution - Can be used wherever ReportGenerator is expected
 */
public class ShowReportGenerator extends ReportGenerator {
    
    private int totalShows;
    private int occupiedSeats;
    private int totalSeats;
    private double avgOccupancyRate;
    
    public ShowReportGenerator(LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate);
    }
    
    @Override
    protected void processData() {
        System.out.println("[SHOW] Processing show data...");
        // Fetch real show data from MovieCatalog
        MovieCatalog catalog = MovieCatalog.getInstance();
        List<Show> shows = catalog.getAllShows();
        
        totalShows = shows.size();
        totalSeats = 0;
        occupiedSeats = 0;
        
        for (Show show : shows) {
            totalSeats += show.getTotalSeats();
            int occupied = show.getTotalSeats() - show.getAvailableSeatsCount();
            occupiedSeats += occupied;
        }
        
        System.out.println("[SHOW] Show Data Fetched: " + totalShows + " shows found");
    }
    
    @Override
    protected void calculateStatistics() {
        System.out.println("[SHOW] Calculating show statistics...");
        avgOccupancyRate = (occupiedSeats * 100.0) / totalSeats;
        
        System.out.println("[SHOW] Occupancy Rate: " + String.format("%.2f", avgOccupancyRate) + "%");
        System.out.println("[SHOW] Average Seats per Show: " + (occupiedSeats / totalShows));
    }
    
    @Override
    protected void formatAndDisplay() {
        System.out.println("\n--- Show Report Details ---");
        System.out.println("Total Shows: " + totalShows);
        System.out.println("Occupied Seats: " + occupiedSeats);
        System.out.println("Total Seats Capacity: " + totalSeats);
        System.out.println("Occupancy Rate: " + String.format("%.2f", avgOccupancyRate) + "%");
    }
    
    @Override
    protected void generateSummary() {
        System.out.println("\n--- Show Report Summary ---");
        System.out.println("Period: " + startDate + " to " + endDate);
        System.out.println("Performance: " + (avgOccupancyRate > 70 ? "Excellent" : avgOccupancyRate > 50 ? "Good" : "Needs Improvement"));
    }
    
    @Override
    protected String getReportTitle() {
        return "SHOW PERFORMANCE REPORT";
    }
}

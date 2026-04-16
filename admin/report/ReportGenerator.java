package admin.report;

import java.time.LocalDate;

/**
 * TEMPLATE METHOD PATTERN - Abstract Report Generator
 * 
 * Defines the skeleton of report generation algorithm.
 * Subclasses implement specific report types.
 * 
 * Design Pattern: Template Method Pattern
 * Design Principle: Single Responsibility - Each report type has ONE responsibility
 * Design Principle: Open/Closed - New report types can be added without modifying this class
 * Design Principle: Dependency Inversion - Depends on abstraction, not concrete implementations
 *
 * @author Admin Module
 */
public abstract class ReportGenerator {
    
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected String reportTitle;
    
    public ReportGenerator(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    /**
     * Template method that defines the structure of report generation
     */
    public final void generateReport() {
        System.out.println("\n========================================");
        System.out.println("Generating Report: " + getReportTitle());
        System.out.println("========================================");
        
        // Step 1: Validate dates
        validateDates();
        
        // Step 2: Fetch data
        fetchData();
        
        // Step 3: Process data (specific to each report type)
        processData();
        
        // Step 4: Calculate statistics (specific to each report type)
        calculateStatistics();
        
        // Step 5: Format and display report
        formatAndDisplay();
        
        // Step 6: Generate summary
        generateSummary();
        
        System.out.println("========================================");
        System.out.println("Report Generation Complete!");
        System.out.println("========================================\n");
    }
    
    /**
     * Validate date range
     */
    protected void validateDates() {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        System.out.println("[GENERATOR] Date Range: " + startDate + " to " + endDate);
    }
    
    /**
     * Fetch data from database - common logic
     */
    protected void fetchData() {
        System.out.println("[GENERATOR] Fetching data from database...");
    }
    
    /**
     * Process data - implemented by subclasses
     */
    protected abstract void processData();
    
    /**
     * Calculate statistics - implemented by subclasses
     */
    protected abstract void calculateStatistics();
    
    /**
     * Format and display report - implemented by subclasses
     */
    protected abstract void formatAndDisplay();
    
    /**
     * Generate summary - implemented by subclasses
     */
    protected abstract void generateSummary();
    
    /**
     * Get report title - implemented by subclasses
     */
    protected abstract String getReportTitle();
}

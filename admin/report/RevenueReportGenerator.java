package admin.report;

import java.time.LocalDate;

/**
 * TEMPLATE METHOD PATTERN - Revenue Report Generator
 * 
 * Generates revenue and financial reports.
 * Concrete implementation of ReportGenerator for revenue data.
 * 
 * Design Principle: Liskov Substitution - Can be used wherever ReportGenerator is expected
 */
public class RevenueReportGenerator extends ReportGenerator {
    
    private double totalRevenue;
    private double totalExpenses;
    private double netProfit;
    private int totalTransactions;
    
    public RevenueReportGenerator(LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate);
    }
    
    @Override
    protected void processData() {
        System.out.println("[REVENUE] Processing revenue data...");
        // Simulate fetching revenue data
        totalRevenue = 500000.00; // Simulated
        totalExpenses = 150000.00; // Simulated
        totalTransactions = 5000; // Simulated
        System.out.println("[REVENUE] Revenue Data Fetched");
    }
    
    @Override
    protected void calculateStatistics() {
        System.out.println("[REVENUE] Calculating financial statistics...");
        netProfit = totalRevenue - totalExpenses;
        double profitMargin = (netProfit / totalRevenue) * 100;
        double avgTransactionValue = totalRevenue / totalTransactions;
        
        System.out.println("[REVENUE] Net Profit: Rs. " + String.format("%.2f", netProfit));
        System.out.println("[REVENUE] Profit Margin: " + String.format("%.2f", profitMargin) + "%");
        System.out.println("[REVENUE] Avg Transaction Value: Rs. " + String.format("%.2f", avgTransactionValue));
    }
    
    @Override
    protected void formatAndDisplay() {
        System.out.println("\n--- Revenue Report Details ---");
        System.out.println("Total Revenue: Rs. " + String.format("%.2f", totalRevenue));
        System.out.println("Total Expenses: Rs. " + String.format("%.2f", totalExpenses));
        System.out.println("Net Profit: Rs. " + String.format("%.2f", netProfit));
        System.out.println("Total Transactions: " + totalTransactions);
    }
    
    @Override
    protected void generateSummary() {
        System.out.println("\n--- Revenue Report Summary ---");
        System.out.println("Period: " + startDate + " to " + endDate);
        double profitMargin = (netProfit / totalRevenue) * 100;
        System.out.println("Profit Margin: " + String.format("%.2f", profitMargin) + "%");
        System.out.println("Financial Status: " + (netProfit > 0 ? "Profitable" : "Loss"));
    }
    
    @Override
    protected String getReportTitle() {
        return "REVENUE REPORT";
    }
}

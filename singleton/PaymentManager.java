package singleton;

import model.Payment;
import enums.PaymentMethod;
import enums.PaymentStatus;
import payment.PaymentGateway;
import payment.PaymentProcessor;

import java.util.HashMap;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * SINGLETON PATTERN - PaymentManager
 * The central manager for ALL payment operations.
 * Only ONE instance exists — ensures consistent payment processing.
 *
 * Design Pattern: Singleton (Thread-safe)
 * Design Pattern: Adapter Pattern (uses PaymentProcessor for gateways)
 * Design Principle: SRP - Only responsible for managing payments.
 */
public class PaymentManager {

    // Single instance
    private static volatile PaymentManager instance = null;

    // In-memory store (simulates DB)
    private Map<Integer, Payment> paymentStore;
    private int paymentCounter;
    private final DBConnection dbConnection;

    // Private constructor
    private PaymentManager() {
        this.paymentStore = new HashMap<>();
        this.dbConnection = DBConnection.getInstance();
        this.paymentCounter = loadMaxPaymentId();
        loadPayments();
        System.out.println("[PaymentManager] Initialized successfully.");
    }

    // Global access point (thread-safe)
    public static PaymentManager getInstance() {
        if (instance == null) {
            synchronized (PaymentManager.class) {
                if (instance == null) {
                    instance = new PaymentManager();
                }
            }
        }
        return instance;
    }

    /**
     * Create a new payment for a booking.
     * @param amount The payment amount
     * @param method The payment method
     * @param bookingId The associated booking ID
     * @return The created Payment object
     */
    public Payment createPayment(double amount, PaymentMethod method, int bookingId) {
        Payment payment = new Payment(++paymentCounter, amount, method, bookingId);
        paymentStore.put(payment.getPaymentId(), payment);
        persistPayment(payment);
        System.out.println("[PaymentManager] Created " + payment);
        return payment;
    }

    /**
     * Process a payment using the appropriate payment gateway.
     * @param paymentId The payment ID to process
     * @param accountDetails The account/card/wallet details
     * @return true if successful, false otherwise
     */
    public boolean processPayment(int paymentId, String accountDetails) {
        Payment payment = paymentStore.get(paymentId);

        if (payment == null) {
            System.out.println("[PaymentManager] Payment not found: " + paymentId);
            return false;
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            System.out.println("[PaymentManager] Payment already processed: " + payment.getStatus());
            return false;
        }

        // Get the appropriate payment gateway using Adapter Pattern
        PaymentGateway gateway = PaymentProcessor.getPaymentGateway(payment.getMethod());

        System.out.println("[PaymentManager] Processing payment using: " + gateway.getPaymentMethodName());

        // Process the payment
        boolean success = gateway.processPayment(payment.getAmount(), accountDetails);

        if (success) {
            payment.processPayment();
            updatePaymentStatus(payment.getPaymentId(), PaymentStatus.SUCCESS);
            System.out.println("[PaymentManager] Payment processed successfully: " + payment);
        } else {
            payment.failPayment();
            updatePaymentStatus(payment.getPaymentId(), PaymentStatus.FAILED);
            System.out.println("[PaymentManager] Payment failed: " + payment);
        }

        return success;
    }

    /**
     * Refund a payment.
     * @param paymentId The payment ID to refund
     * @param transactionId The transaction ID for the refund
     * @return true if successful, false otherwise
     */
    public boolean refundPayment(int paymentId, String transactionId) {
        Payment payment = paymentStore.get(paymentId);

        if (payment == null) {
            System.out.println("[PaymentManager] Payment not found: " + paymentId);
            return false;
        }

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            System.out.println("[PaymentManager] Cannot refund payment with status: " + payment.getStatus());
            return false;
        }

        // Get the appropriate payment gateway
        PaymentGateway gateway = PaymentProcessor.getPaymentGateway(payment.getMethod());

        System.out.println("[PaymentManager] Processing refund using: " + gateway.getPaymentMethodName());

        // Process the refund
        boolean success = gateway.refundPayment(payment.getAmount(), transactionId);

        if (success) {
            payment.refund();
            updatePaymentStatus(payment.getPaymentId(), PaymentStatus.REFUNDED);
            System.out.println("[PaymentManager] Refund processed successfully: " + payment);
        } else {
            System.out.println("[PaymentManager] Refund failed");
        }

        return success;
    }

    /**
     * Get a payment by ID.
     * @param paymentId The payment ID
     * @return The Payment object or null
     */
    public Payment getPayment(int paymentId) {
        Payment payment = paymentStore.get(paymentId);
        if (payment != null) {
            return payment;
        }
        String sql = "SELECT payment_id, amount, payment_date, status, method, booking_id FROM payments WHERE payment_id = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Payment loaded = new Payment(
                        rs.getInt("payment_id"),
                        rs.getDouble("amount"),
                        PaymentMethod.valueOf(rs.getString("method")),
                        rs.getInt("booking_id")
                );
                loaded.setPaymentDate(new Date(rs.getLong("payment_date")));
                loaded.setStatus(PaymentStatus.valueOf(rs.getString("status")));
                paymentStore.put(loaded.getPaymentId(), loaded);
                return loaded;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch payment " + paymentId, e);
        }
    }

    /**
     * Display all payments in the store.
     */
    public void displayAllPayments() {
        System.out.println("\n========== All Payments ==========");
        if (paymentStore.isEmpty()) {
            System.out.println("No payments found.");
        } else {
            paymentStore.values().forEach(System.out::println);
        }
        System.out.println("==================================");
    }

    private int loadMaxPaymentId() {
        String sql = "SELECT COALESCE(MAX(payment_id), 5000) AS max_id FROM payments";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("max_id") : 5000;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize payment counter", e);
        }
    }

    private void loadPayments() {
        String sql = "SELECT payment_id, amount, payment_date, status, method, booking_id FROM payments";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("payment_id"),
                        rs.getDouble("amount"),
                        PaymentMethod.valueOf(rs.getString("method")),
                        rs.getInt("booking_id")
                );
                payment.setPaymentDate(new Date(rs.getLong("payment_date")));
                payment.setStatus(PaymentStatus.valueOf(rs.getString("status")));
                paymentStore.put(payment.getPaymentId(), payment);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load payments", e);
        }
    }

    private void persistPayment(Payment payment) {
        String sql = "INSERT OR REPLACE INTO payments(payment_id, amount, payment_date, status, method, booking_id) VALUES(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, payment.getPaymentId());
            ps.setDouble(2, payment.getAmount());
            ps.setLong(3, payment.getPaymentDate().getTime());
            ps.setString(4, payment.getStatus().name());
            ps.setString(5, payment.getMethod().name());
            ps.setInt(6, payment.getBookingId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save payment", e);
        }
    }

    private void updatePaymentStatus(int paymentId, PaymentStatus status) {
        String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";
        try (PreparedStatement ps = dbConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, paymentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update payment status", e);
        }
    }
}

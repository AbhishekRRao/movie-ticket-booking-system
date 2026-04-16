package singleton;

import model.Payment;
import enums.PaymentMethod;
import enums.PaymentStatus;
import payment.PaymentGateway;
import payment.PaymentProcessor;

import java.util.HashMap;
import java.util.Map;

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

    // Private constructor
    private PaymentManager() {
        this.paymentStore = new HashMap<>();
        this.paymentCounter = 5000; // Payment IDs start from 5000
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
            System.out.println("[PaymentManager] Payment processed successfully: " + payment);
        } else {
            payment.failPayment();
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
        return paymentStore.get(paymentId);
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
}

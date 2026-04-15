package payment;

/**
 * INTERFACE - PaymentGateway
 * Standard interface for all payment processing.
 * Used by adapters to provide uniform payment processing.
 *
 * Design Pattern: Adapter Pattern
 * This is the Target interface that all adapters must implement.
 */
public interface PaymentGateway {
    /**
     * Process a payment transaction.
     * @param amount The amount to be charged
     * @param accountDetails Account/card details for the transaction
     * @return true if payment successful, false otherwise
     */
    boolean processPayment(double amount, String accountDetails);

    /**
     * Refund a payment.
     * @param amount The amount to be refunded
     * @param transactionId The transaction ID to refund
     * @return true if refund successful, false otherwise
     */
    boolean refundPayment(double amount, String transactionId);

    /**
     * Get the payment method name.
     * @return the method name (e.g., "Credit Card", "UPI")
     */
    String getPaymentMethodName();
}

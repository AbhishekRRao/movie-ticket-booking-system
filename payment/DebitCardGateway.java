package payment;

/**
 * CONCRETE GATEWAY - DebitCardGateway
 * Implements payments via Debit Card.
 *
 * Design Pattern: Adapter Pattern
 * This is a concrete adapter that implements the PaymentGateway interface.
 */
public class DebitCardGateway implements PaymentGateway {

    @Override
    public boolean processPayment(double amount, String cardDetails) {
        System.out.println("[DebitCardGateway] Processing payment of Rs." + amount);
        System.out.println("[DebitCardGateway] Debit Card Details: " + cardDetails);

        // Simulate debit card validation
        if (cardDetails == null || cardDetails.isEmpty()) {
            System.out.println("[DebitCardGateway] Error: Invalid card details");
            return false;
        }

        // Check balance (simulate)
        if (!hassufficientBalance(amount)) {
            System.out.println("[DebitCardGateway] Error: Insufficient balance");
            return false;
        }

        boolean isSuccessful = simulatePayment();

        if (isSuccessful) {
            System.out.println("[DebitCardGateway] Payment successful via Debit Card");
        } else {
            System.out.println("[DebitCardGateway] Payment failed");
        }

        return isSuccessful;
    }

    @Override
    public boolean refundPayment(double amount, String transactionId) {
        System.out.println("[DebitCardGateway] Processing refund of Rs." + amount);
        System.out.println("[DebitCardGateway] Transaction ID: " + transactionId);

        if (transactionId == null || transactionId.isEmpty()) {
            System.out.println("[DebitCardGateway] Error: Invalid transaction ID");
            return false;
        }

        boolean isSuccessful = simulateRefund();

        if (isSuccessful) {
            System.out.println("[DebitCardGateway] Refund successful to Debit Card");
        } else {
            System.out.println("[DebitCardGateway] Refund failed");
        }

        return isSuccessful;
    }

    @Override
    public String getPaymentMethodName() {
        return "Debit Card";
    }

    // Simulate balance check
    private boolean hassufficientBalance(double amount) {
        return amount > 0 && amount <= 100000;
    }

    // Simulate payment processing (75% success rate)
    private boolean simulatePayment() {
        return Math.random() > 0.25;
    }

    // Simulate refund processing (85% success rate)
    private boolean simulateRefund() {
        return Math.random() > 0.15;
    }
}

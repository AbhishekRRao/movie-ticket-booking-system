package payment;

/**
 * CONCRETE GATEWAY - CreditCardGateway
 * Implements payments via Credit Card.
 *
 * Design Pattern: Adapter Pattern
 * This is a concrete adapter that implements the PaymentGateway interface.
 */
public class CreditCardGateway implements PaymentGateway {

    @Override
    public boolean processPayment(double amount, String cardDetails) {
        System.out.println("[CreditCardGateway] Processing payment of Rs." + amount);
        System.out.println("[CreditCardGateway] Card Details: " + cardDetails);

        // Simulate card validation
        if (cardDetails == null || cardDetails.isEmpty()) {
            System.out.println("[CreditCardGateway] Error: Invalid card details");
            return false;
        }

        // Simulate payment processing
        boolean isSuccessful = simulatePayment();

        if (isSuccessful) {
            System.out.println("[CreditCardGateway] Payment successful via Credit Card");
        } else {
            System.out.println("[CreditCardGateway] Payment failed");
        }

        return isSuccessful;
    }

    @Override
    public boolean refundPayment(double amount, String transactionId) {
        System.out.println("[CreditCardGateway] Processing refund of Rs." + amount);
        System.out.println("[CreditCardGateway] Transaction ID: " + transactionId);

        if (transactionId == null || transactionId.isEmpty()) {
            System.out.println("[CreditCardGateway] Error: Invalid transaction ID");
            return false;
        }

        boolean isSuccessful = simulateRefund();

        if (isSuccessful) {
            System.out.println("[CreditCardGateway] Refund successful to Credit Card");
        } else {
            System.out.println("[CreditCardGateway] Refund failed");
        }

        return isSuccessful;
    }

    @Override
    public String getPaymentMethodName() {
        return "Credit Card";
    }

    // Simulate payment processing (70% success rate)
    private boolean simulatePayment() {
        return Math.random() > 0.3;
    }

    // Simulate refund processing (80% success rate)
    private boolean simulateRefund() {
        return Math.random() > 0.2;
    }
}

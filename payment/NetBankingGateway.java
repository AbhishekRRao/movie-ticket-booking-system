package payment;

/**
 * CONCRETE GATEWAY - NetBankingGateway
 * Implements payments via Net Banking.
 *
 * Design Pattern: Adapter Pattern
 * This is a concrete adapter that implements the PaymentGateway interface.
 */
public class NetBankingGateway implements PaymentGateway {

    @Override
    public boolean processPayment(double amount, String bankCredentials) {
        System.out.println("[NetBankingGateway] Processing payment of Rs." + amount);
        System.out.println("[NetBankingGateway] Bank: " + bankCredentials);

        // Validate bank credentials
        if (bankCredentials == null || bankCredentials.isEmpty()) {
            System.out.println("[NetBankingGateway] Error: Invalid bank credentials");
            return false;
        }

        // Simulate 2FA verification
        if (!verifyTwoFactorAuth()) {
            System.out.println("[NetBankingGateway] Error: 2FA verification failed");
            return false;
        }

        boolean isSuccessful = simulatePayment();

        if (isSuccessful) {
            System.out.println("[NetBankingGateway] Payment successful via Net Banking");
        } else {
            System.out.println("[NetBankingGateway] Payment failed");
        }

        return isSuccessful;
    }

    @Override
    public boolean refundPayment(double amount, String transactionId) {
        System.out.println("[NetBankingGateway] Processing refund of Rs." + amount);
        System.out.println("[NetBankingGateway] Transaction ID: " + transactionId);

        if (transactionId == null || transactionId.isEmpty()) {
            System.out.println("[NetBankingGateway] Error: Invalid transaction ID");
            return false;
        }

        boolean isSuccessful = simulateRefund();

        if (isSuccessful) {
            System.out.println("[NetBankingGateway] Refund successful via Net Banking");
        } else {
            System.out.println("[NetBankingGateway] Refund failed");
        }

        return isSuccessful;
    }

    @Override
    public String getPaymentMethodName() {
        return "Net Banking";
    }

    // Simulate 2FA verification (95% success)
    private boolean verifyTwoFactorAuth() {
        return Math.random() > 0.05;
    }

    // Simulate payment processing (80% success rate)
    private boolean simulatePayment() {
        return Math.random() > 0.2;
    }

    // Simulate refund processing (85% success rate)
    private boolean simulateRefund() {
        return Math.random() > 0.15;
    }
}

package payment;

/**
 * CONCRETE GATEWAY - WalletGateway
 * Implements payments via Digital Wallet (e.g., PayTM, Google Pay).
 *
 * Design Pattern: Adapter Pattern
 * This is a concrete adapter that implements the PaymentGateway interface.
 */
public class WalletGateway implements PaymentGateway {

    @Override
    public boolean processPayment(double amount, String walletId) {
        System.out.println("[WalletGateway] Processing payment of Rs." + amount);
        System.out.println("[WalletGateway] Wallet ID: " + walletId);

        // Validate wallet ID
        if (walletId == null || walletId.isEmpty()) {
            System.out.println("[WalletGateway] Error: Invalid wallet ID");
            return false;
        }

        // Check wallet balance (simulate)
        if (!hasWalletBalance(amount)) {
            System.out.println("[WalletGateway] Error: Insufficient wallet balance");
            return false;
        }

        boolean isSuccessful = simulatePayment();

        if (isSuccessful) {
            System.out.println("[WalletGateway] Payment successful via Digital Wallet");
        } else {
            System.out.println("[WalletGateway] Payment failed");
        }

        return isSuccessful;
    }

    @Override
    public boolean refundPayment(double amount, String transactionId) {
        System.out.println("[WalletGateway] Processing refund of Rs." + amount);
        System.out.println("[WalletGateway] Transaction ID: " + transactionId);

        if (transactionId == null || transactionId.isEmpty()) {
            System.out.println("[WalletGateway] Error: Invalid transaction ID");
            return false;
        }

        boolean isSuccessful = simulateRefund();

        if (isSuccessful) {
            System.out.println("[WalletGateway] Refund successful to Digital Wallet");
        } else {
            System.out.println("[WalletGateway] Refund failed");
        }

        return isSuccessful;
    }

    @Override
    public String getPaymentMethodName() {
        return "Digital Wallet";
    }

    // Simulate wallet balance check
    private boolean hasWalletBalance(double amount) {
        return amount > 0 && amount <= 50000;
    }

    // Simulate payment processing (88% success rate)
    private boolean simulatePayment() {
        return Math.random() > 0.12;
    }

    // Simulate refund processing (92% success rate)
    private boolean simulateRefund() {
        return Math.random() > 0.08;
    }
}

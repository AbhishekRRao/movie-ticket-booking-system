package payment;

/**
 * CONCRETE GATEWAY - UPIGateway
 * Implements payments via UPI (Unified Payments Interface).
 *
 * Design Pattern: Adapter Pattern
 * This is a concrete adapter that implements the PaymentGateway interface.
 */
public class UPIGateway implements PaymentGateway {

    @Override
    public boolean processPayment(double amount, String upiId) {
        System.out.println("[UPIGateway] Processing payment of Rs." + amount);
        System.out.println("[UPIGateway] UPI ID: " + upiId);

        // Validate UPI ID format
        if (upiId == null || !isValidUPI(upiId)) {
            System.out.println("[UPIGateway] Error: Invalid UPI ID");
            return false;
        }

        // Simulate OTP verification
        if (!verifyOTP()) {
            System.out.println("[UPIGateway] Error: OTP verification failed");
            return false;
        }

        boolean isSuccessful = simulatePayment();

        if (isSuccessful) {
            System.out.println("[UPIGateway] Payment successful via UPI");
        } else {
            System.out.println("[UPIGateway] Payment failed");
        }

        return isSuccessful;
    }

    @Override
    public boolean refundPayment(double amount, String transactionId) {
        System.out.println("[UPIGateway] Processing refund of Rs." + amount);
        System.out.println("[UPIGateway] Transaction ID: " + transactionId);

        if (transactionId == null || transactionId.isEmpty()) {
            System.out.println("[UPIGateway] Error: Invalid transaction ID");
            return false;
        }

        boolean isSuccessful = simulateRefund();

        if (isSuccessful) {
            System.out.println("[UPIGateway] Refund successful via UPI");
        } else {
            System.out.println("[UPIGateway] Refund failed");
        }

        return isSuccessful;
    }

    @Override
    public String getPaymentMethodName() {
        return "UPI";
    }

    // Validate UPI ID format (simple validation)
    private boolean isValidUPI(String upiId) {
        return upiId.contains("@") && upiId.length() > 5;
    }

    // Simulate OTP verification (90% success)
    private boolean verifyOTP() {
        return Math.random() > 0.1;
    }

    // Simulate payment processing (85% success rate)
    private boolean simulatePayment() {
        return Math.random() > 0.15;
    }

    // Simulate refund processing (90% success rate)
    private boolean simulateRefund() {
        return Math.random() > 0.1;
    }
}

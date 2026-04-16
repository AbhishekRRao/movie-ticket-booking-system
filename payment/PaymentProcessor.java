package payment;

import enums.PaymentMethod;

/**
 * ADAPTER FACTORY - PaymentProcessor
 * Creates and returns the appropriate PaymentGateway adapter based on the payment method.
 *
 * Design Pattern: Adapter Pattern + Factory Pattern
 * This factory produces the correct adapter for the requested payment method.
 * Simplifies client code by providing a single point to get the right adapter.
 */
public class PaymentProcessor {

    /**
     * Get the appropriate payment gateway adapter for the given payment method.
     * @param method The payment method selected by the user
     * @return The corresponding PaymentGateway adapter
     */
    public static PaymentGateway getPaymentGateway(PaymentMethod method) {
        switch (method) {
            case CREDIT_CARD:
                return new CreditCardGateway();
            case DEBIT_CARD:
                return new DebitCardGateway();
            case UPI:
                return new UPIGateway();
            case NET_BANKING:
                return new NetBankingGateway();
            case WALLET:
                return new WalletGateway();
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + method);
        }
    }

    /**
     * Display all available payment methods to the user.
     */
    public static void displayPaymentMethods() {
        System.out.println("\n========== Available Payment Methods ==========");
        PaymentMethod[] methods = PaymentMethod.values();
        for (int i = 0; i < methods.length; i++) {
            System.out.println((i + 1) + ". " + methods[i]);
        }
        System.out.println("==============================================");
    }
}

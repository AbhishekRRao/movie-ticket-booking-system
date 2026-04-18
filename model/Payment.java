package model;

import enums.PaymentMethod;
import enums.PaymentStatus;
import java.util.Date;

/**
 * MODEL - Payment
 * SRP: Only holds payment data and payment-level information.
 * Matches the Payment class from the class diagram.
 */
public class Payment {
    private int paymentId;
    private double amount;
    private Date paymentDate;
    private PaymentStatus status;
    private PaymentMethod method;
    private int bookingId;

    public Payment(int paymentId, double amount, PaymentMethod method, int bookingId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.method = method;
        this.bookingId = bookingId;
        this.status = PaymentStatus.PENDING;
        this.paymentDate = new Date();
    }

    // Process payment - marks as success
    public void processPayment() {
        this.status = PaymentStatus.SUCCESS;
    }

    // Refund payment
    public void refund() {
        this.status = PaymentStatus.REFUNDED;
    }

    // Mark payment as failed
    public void failPayment() {
        this.status = PaymentStatus.FAILED;
    }

    // Getters
    public int getPaymentId() { return paymentId; }
    public double getAmount() { return amount; }
    public Date getPaymentDate() { return paymentDate; }
    public PaymentStatus getStatus() { return status; }
    public PaymentMethod getMethod() { return method; }
    public int getBookingId() { return bookingId; }

    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Payment[ID:" + paymentId + " | Amount:Rs." + amount +
               " | Method:" + method + " | Status:" + status + "]";
    }
}

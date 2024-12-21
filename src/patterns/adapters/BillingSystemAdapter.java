package patterns.adapters;

// Simulate an external billing system
class ExternalBillingSystem {
    public void makePayment(String accountNumber, double amount) {
        System.out.println("Payment of $" + amount + " made for account: " + accountNumber);
    }
}

// Adapter class to integrate with the external billing system
public class BillingSystemAdapter implements BillingSystem {
    private final ExternalBillingSystem externalBillingSystem;

    public BillingSystemAdapter() {
        this.externalBillingSystem = new ExternalBillingSystem();
    }

    @Override
    public void processPayment(String patientId, double amount) {
        String accountNumber = getAccountFromPatientId(patientId);
        externalBillingSystem.makePayment(accountNumber, amount);
    }

    private String getAccountFromPatientId(String patientId) {
        return "ACC-" + patientId; // Simulate mapping patient ID to account number
    }
}

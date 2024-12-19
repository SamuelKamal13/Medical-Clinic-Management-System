/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package medicalclinicmanagement.clinic;

/**
 *
 * @author Samoueel
 */

public class ExternalBillingSystemImpl implements ExternalBillingSystem {
    @Override
    public void processPayment(String patientName, double amount) {
        System.out.println("Processing payment for " + patientName + " of amount " + amount);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package medicalclinicmanagement.clinic;

/**
 *
 * @author Samoueel
 */

public interface ExternalBillingSystem {
    void processPayment(String patientName, double amount);
}

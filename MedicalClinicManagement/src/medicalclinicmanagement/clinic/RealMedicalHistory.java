/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package medicalclinicmanagement.clinic;

/**
 *
 * @author Samoueel
 */

public class RealMedicalHistory implements MedicalHistory {
    @Override
    public void viewMedicalHistory(String patientName) {
        System.out.println("Viewing medical history for " + patientName);
    }
}

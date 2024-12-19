/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package medicalclinicmanagement.clinic;

/**
 *
 * @author Samoueel
 */

import java.util.List;

public class MedicalHistoryProxy implements MedicalHistory {
    private RealMedicalHistory realMedicalHistory;
    private List<String> authorizedUsers;

    public MedicalHistoryProxy(List<String> authorizedUsers) {
        this.realMedicalHistory = new RealMedicalHistory();
        this.authorizedUsers = authorizedUsers;
    }

    @Override
    public void viewMedicalHistory(String patientName) {
        if (authorizedUsers.contains(patientName)) {
            realMedicalHistory.viewMedicalHistory(patientName);
        } else {
            System.out.println("Access denied for " + patientName);
        }
    }
}

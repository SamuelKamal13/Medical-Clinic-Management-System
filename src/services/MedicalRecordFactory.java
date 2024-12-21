/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import models.*;

public class MedicalRecordFactory {
    public static MedicalRecord createMedicalRecord(String type, String patientId) {
        switch (type) {
            case "History":
                return new PatientHistory(patientId);
            case "Prescription":
                return new Prescription(patientId);
            case "LabResult":
                return new LabResult(patientId);
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}

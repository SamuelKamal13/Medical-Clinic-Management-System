/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patterns.proxies;

import java.util.HashMap;
import java.util.Map;

interface PatientDataAccess {
    String getPatientRecord(String patientId);
}

class RealPatientDataAccess implements PatientDataAccess {
    private final Map<String, String> patientRecords = new HashMap<>();

    public RealPatientDataAccess() {
        // Simulated data
        patientRecords.put("P001", "Patient 001 Record");
        patientRecords.put("P002", "Patient 002 Record");
    }

    @Override
    public String getPatientRecord(String patientId) {
        return patientRecords.getOrDefault(patientId, "No record found");
    }
}

public class PatientDataAccessProxy implements PatientDataAccess {
    private final RealPatientDataAccess realAccess;

    public PatientDataAccessProxy() {
        this.realAccess = new RealPatientDataAccess();
    }

    @Override
    public String getPatientRecord(String patientId) {
        if (hasAccessRights(patientId)) {
            return realAccess.getPatientRecord(patientId);
        } else {
            throw new SecurityException("Unauthorized access to patient data");
        }
    }

    private boolean hasAccessRights(String patientId) {
        // Simulated access control
        return patientId.startsWith("P");
    }
}

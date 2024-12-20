package models;

// Abstract base class for medical records
public abstract class MedicalRecord {
    private String patientId;

    public MedicalRecord(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patterns.builders;

// Class to represent a detailed patient report
public class PatientReport {
    private final String patientName;
    private final String diagnosis;
    private final String treatmentPlan;

    private PatientReport(Builder builder) {
        this.patientName = builder.patientName;
        this.diagnosis = builder.diagnosis;
        this.treatmentPlan = builder.treatmentPlan;
    }

    public static class Builder {
        private String patientName;
        private String diagnosis;
        private String treatmentPlan;

        public Builder setPatientName(String patientName) {
            this.patientName = patientName;
            return this;
        }

        public Builder setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
            return this;
        }

        public Builder setTreatmentPlan(String treatmentPlan) {
            this.treatmentPlan = treatmentPlan;
            return this;
        }

        public PatientReport build() {
            return new PatientReport(this);
        }
    }

    @Override
    public String toString() {
        return "PatientReport{" +
                "patientName='" + patientName + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", treatmentPlan='" + treatmentPlan + '\'' +
                '}';
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package patterns.builders;

public class PatientReportBuilder {
    public static void main(String[] args) {
        // Example of using the Builder to create a PatientReport
        PatientReport report = new PatientReport.Builder()
                .setPatientName("John Doe")
                .setDiagnosis("Flu")
                .setTreatmentPlan("Rest and hydration")
                .build();

        System.out.println(report);
    }
}

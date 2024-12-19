/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package medicalclinicmanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.SwingUtilities;
import medicalclinicmanagement.clinic.Appointment;
import medicalclinicmanagement.clinic.BillingSystemAdapter;
import medicalclinicmanagement.clinic.DatabaseConnection;
import medicalclinicmanagement.clinic.MedicalClinicGUI;
import medicalclinicmanagement.clinic.MedicalHistory;
import medicalclinicmanagement.clinic.MedicalHistoryProxy;
import medicalclinicmanagement.clinic.User;
import medicalclinicmanagement.clinic.UserFactory;


/**
 *
 * @author Samoueel
 */
public class MedicalClinicManagement {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            // Singleton: Test Database Connection
            DatabaseConnection db = DatabaseConnection.getInstance();
            Connection conn = db.getConnection();

            // Validate connection and fetch data
            if (conn != null && !conn.isClosed()) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM patients")) {

                    System.out.println("Patients table data:");
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
                    }
                }
            } else {
                System.err.println("Database connection is not available.");
            }

            // Factory: Create Users
            User doctor = UserFactory.createUser("doctor", "Dr. Smith", 101);
            doctor.displayRole();

            User receptionist = UserFactory.createUser("receptionist", "Alice", 102);
            receptionist.displayRole();

            // Builder: Create an Appointment
            Appointment appointment = new Appointment.AppointmentBuilder("John Doe", "Dr. Smith")
                    .setDate(new Date())
                    .setNotes("Regular check-up")
                    .build();
            System.out.println(appointment);

            // Adapter: Process Payment
            BillingSystemAdapter billingSystem = new BillingSystemAdapter();
            billingSystem.makePayment("John Doe", 200.0);

            // Proxy: Access Medical History
            List<String> authorizedUsers = Arrays.asList("John Doe", "Alice");
            MedicalHistory medicalHistory = new MedicalHistoryProxy(authorizedUsers);
            medicalHistory.viewMedicalHistory("John Doe");
            medicalHistory.viewMedicalHistory("Unknown User");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        // Start the GUI
        SwingUtilities.invokeLater(() -> new MedicalClinicGUI());

        // Add a shutdown hook to close the connection when the application exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseConnection.getInstance().closeConnection();
        }));
    }
    
}

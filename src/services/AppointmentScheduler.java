/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppointmentScheduler {
    public boolean scheduleAppointment(String patientId, String doctorId, String timeSlot) {
        String sql = "INSERT INTO Appointments (patient_id, doctor_id, time_slot) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            System.out.println("Executing SQL Query: " + sql);
            statement.setString(1, patientId);
            statement.setString(2, doctorId);
            statement.setString(3, timeSlot);

            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows inserted: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Failed to schedule appointment: " + e.getMessage());
            return false;
        }
    }
}

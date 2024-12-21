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
    
    public boolean scheduleAppointment(String patientId, String doctorName, String timeSlot) {
        String sql = "INSERT INTO Appointments (patient_id, doctor_name, time_slot) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            System.out.println("Executing SQL Query: " + sql);
            statement.setString(1, patientId);
            statement.setString(2, doctorName);
            statement.setString(3, timeSlot);

            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows inserted: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Failed to schedule appointment: " + e.getMessage());
            return false;
        }
    }

    // Update the appointment based on appointment ID
    public boolean updateAppointment(int appointmentId, String patientId, String doctorName, String timeSlot) {
        String sql = "UPDATE Appointments SET patient_id = ?, doctor_name = ?, time_slot = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patientId);
            statement.setString(2, doctorName);
            statement.setString(3, timeSlot);
            statement.setInt(4, appointmentId); // Use the appointment ID to update the correct record

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Failed to update appointment: " + e.getMessage());
            return false;
        }
    }

    // Delete the appointment based on appointment ID
    public boolean deleteAppointmentById(int appointmentId) {
        String sql = "DELETE FROM Appointments WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);  // Use appointment ID to delete the correct record

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Failed to delete appointment: " + e.getMessage());
            return false;
        }
    }
}

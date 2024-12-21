/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import models.Patient;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDatabaseManager {

    // Fetch the last patient ID and generate the next ID
    public String generateNextPatientId() {
        String lastId = null;
        String sql = "SELECT id FROM Patients ORDER BY id DESC LIMIT 1";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                lastId = resultSet.getString("id");
            }

        } catch (SQLException e) {
            System.err.println("Failed to fetch last patient ID: " + e.getMessage());
        }

        // If no patients are found, start with "P001"
        if (lastId == null) {
            return "P001";
        }

        // Extract the numeric part of the ID and increment it
        int numericPart = Integer.parseInt(lastId.substring(1));
        return "P" + String.format("%03d", numericPart + 1); // E.g., "P002"
    }

    public void addPatient(Patient patient) {
        String sql = "INSERT INTO Patients (id, name, age, contact_info) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patient.getId());
            statement.setString(2, patient.getName());
            statement.setInt(3, patient.getAge());
            statement.setString(4, patient.getContactInfo());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to add patient: " + e.getMessage());
        }
    }

    // Fetch all patients from the database
    public List<Patient> getPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT id, name, age, contact_info FROM Patients";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String contactInfo = resultSet.getString("contact_info");

                Patient patient = new Patient(id, name, age, contactInfo);
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch patients: " + e.getMessage());
        }
        return patients;
    }

    // Delete patient by ID
    public void deletePatient(String patientId) {
        String sql = "DELETE FROM Patients WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patientId);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to delete patient: " + e.getMessage());
        }
    }

    // Update patient details
    public void updatePatient(Patient patient) {
        String sql = "UPDATE Patients SET name = ?, age = ?, contact_info = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patient.getName());
            statement.setInt(2, patient.getAge());
            statement.setString(3, patient.getContactInfo());
            statement.setString(4, patient.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to update patient: " + e.getMessage());
        }
    }
}

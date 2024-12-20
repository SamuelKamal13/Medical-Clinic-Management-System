/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import models.Patient;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PatientDatabaseManager {
    public void addPatient(Patient patient) {
        String sql = "INSERT INTO Patients (id, name, age, contact_info) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            System.out.println("Executing SQL Query: " + sql);
            statement.setString(1, patient.getId());
            statement.setString(2, patient.getName());
            statement.setInt(3, patient.getAge());
            statement.setString(4, patient.getContactInfo());

            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Failed to add patient: " + e.getMessage());
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import models.Patient;
import services.PatientDatabaseManager;

import javax.swing.*;
import java.awt.*;

public class ManagePatientsUI extends JFrame {
    private final PatientDatabaseManager patientManager;

    public ManagePatientsUI() {
        this.patientManager = new PatientDatabaseManager();
        setTitle("Manage Patients");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable patientTable = new JTable();
        JButton addButton = new JButton("Add Patient");

        // Add patient button action
        addButton.addActionListener(e -> addPatient());

        // Placeholder for table population (you can update this logic to fetch data from the database)
        String[] columnNames = {"ID", "Name", "Age", "Contact"};
        Object[][] data = {
                {"P001", "John Doe", 30, "john@example.com"},
                {"P002", "Jane Smith", 45, "jane@example.com"}
        };
        patientTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.SOUTH);

        add(panel);
    }

    private void addPatient() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField contactField = new JTextField();

        Object[] message = {
                "ID:", idField,
                "Name:", nameField,
                "Age:", ageField,
                "Contact Info:", contactField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Patient", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Patient patient = new Patient(
                        idField.getText(),
                        nameField.getText(),
                        Integer.parseInt(ageField.getText()),
                        contactField.getText()
                );
                patientManager.addPatient(patient);
                JOptionPane.showMessageDialog(this, "Patient added successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid age entered!");
            }
        }
    }
}

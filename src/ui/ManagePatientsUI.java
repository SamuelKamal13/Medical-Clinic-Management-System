/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import models.Patient;
import services.PatientDatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManagePatientsUI extends JFrame {
    private final PatientDatabaseManager patientManager;
    private final JTable patientTable;

    public ManagePatientsUI() {
        this.patientManager = new PatientDatabaseManager();
        this.patientTable = new JTable();
        setTitle("Manage Patients");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton addButton = new JButton("Add Patient");
        JButton deleteButton = new JButton("Delete Patient");
        JButton updateButton = new JButton("Update Patient");

        // Add button action
        addButton.addActionListener(e -> addPatient());

        // Delete button action
        deleteButton.addActionListener(e -> deletePatient());

        // Update button action
        updateButton.addActionListener(e -> updatePatient());

        // Set up table
        String[] columnNames = {"ID", "Name", "Age", "Contact"};
        patientTable.setModel(new DefaultTableModel(null, columnNames)); // Empty data initially

        // Add buttons to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Fetch data from the database and update the table
        loadPatientData();
    }

    private void loadPatientData() {
        List<Patient> patients = patientManager.getPatients();

        // Prepare the table data
        String[][] data = new String[patients.size()][4];
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            data[i][0] = patient.getId();
            data[i][1] = patient.getName();
            data[i][2] = String.valueOf(patient.getAge());
            data[i][3] = patient.getContactInfo();
        }

        // Update the table model with the data
        DefaultTableModel model = (DefaultTableModel) patientTable.getModel();
        model.setDataVector(data, new String[] {"ID", "Name", "Age", "Contact"});
    }

    private void addPatient() {
        // Automatically generate the next patient ID
        String newPatientId = patientManager.generateNextPatientId();

        JTextField idField = new JTextField(newPatientId);
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
                        newPatientId,  // Use the auto-generated ID
                        nameField.getText(),
                        Integer.parseInt(ageField.getText()),
                        contactField.getText()
                );
                patientManager.addPatient(patient);
                JOptionPane.showMessageDialog(this, "Patient added successfully!");

                // Reload the data after adding a patient
                loadPatientData();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid age entered!");
            }
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            String patientId = (String) patientTable.getValueAt(selectedRow, 0);
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this patient?",
                    "Delete Patient", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                patientManager.deletePatient(patientId);
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
                loadPatientData(); // Reload data after deletion
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.");
        }
    }

    private void updatePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            String patientId = (String) patientTable.getValueAt(selectedRow, 0);
            String name = (String) patientTable.getValueAt(selectedRow, 1);
            int age = Integer.parseInt((String) patientTable.getValueAt(selectedRow, 2));
            String contact = (String) patientTable.getValueAt(selectedRow, 3);

            JTextField nameField = new JTextField(name);
            JTextField ageField = new JTextField(String.valueOf(age));
            JTextField contactField = new JTextField(contact);

            Object[] message = {
                    "Name:", nameField,
                    "Age:", ageField,
                    "Contact Info:", contactField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Update Patient", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    Patient updatedPatient = new Patient(
                            patientId,  // Keep the same ID
                            nameField.getText(),
                            Integer.parseInt(ageField.getText()),
                            contactField.getText()
                    );
                    patientManager.updatePatient(updatedPatient);
                    JOptionPane.showMessageDialog(this, "Patient updated successfully!");
                    loadPatientData(); // Reload data after update
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid age entered!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a patient to update.");
        }
    }
}

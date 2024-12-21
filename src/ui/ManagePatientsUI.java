package ui;

import models.Patient;
import services.PatientDatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ManagePatientsUI extends JFrame {
    private final PatientDatabaseManager patientManager;
    private final JTable patientTable;
    private final JTextField searchField;

    public ManagePatientsUI() {
        this.patientManager = new PatientDatabaseManager();
        this.patientTable = new JTable();
        this.searchField = new JTextField();
        setTitle("Manage Patients");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create search panel with search field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(52, 73, 94));  // Sidebar color from Doctor UI
        JLabel searchLabel = new JLabel("Search (ID/Name):");
        searchLabel.setForeground(Color.WHITE);
        searchPanel.add(searchLabel);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(searchField);

        // Create search button
        JButton searchButton = createButton("Search", this::searchPatients);
        searchPanel.add(searchButton);

        // Create buttons with new styles
        JButton addButton = createButton("Add Patient", this::addPatient);
        JButton updateButton = createButton("Update Patient", this::updatePatient);
        JButton deleteButton = createDeleteButton("Delete Patient", this::deletePatient);  // Red button for Delete

        // Create table model and set table columns
        String[] columnNames = {"ID", "Name", "Age", "Contact"};
        patientTable.setModel(new DefaultTableModel(null, columnNames));

        // Add buttons to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Adjusted layout
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);  // Make delete the third button

        // Set up main panel and add table with scroll
        panel.add(searchPanel, BorderLayout.NORTH);  // Add search panel
        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Fetch data from the database and update the table
        loadPatientData("");  // Load all patients by default
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40)); // Adjusted size for better consistency
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(31, 97, 141));  // Change background to the new color
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false); // Disable default gradient
        button.setOpaque(true); // Enable solid background

        // Add hover effect for darker color
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(26, 82, 118));  // Slightly darker shade for hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(31, 97, 141)); // Reset to the original blue
            }
        });

        button.addActionListener(e -> action.run());  // Action handler
        return button;
    }

    private JButton createDeleteButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40)); // Adjusted size for better consistency
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.RED);  // Red background for delete button
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false); // Disable default gradient
        button.setOpaque(true); // Enable solid background

        // Add hover effect for darker red color
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 0, 0)); // Darker red on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.RED); // Reset to original red
            }
        });

        button.addActionListener(e -> action.run());  // Action handler
        return button;
    }

    private void loadPatientData(String searchText) {
        List<Patient> patients = patientManager.getPatients(searchText);  // Pass search text for filtering

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

    private void searchPatients() {
        String searchText = searchField.getText();
        loadPatientData(searchText);  // Reload data based on the search text
    }

    private void addPatient() {
        // Automatically generate the next patient ID
        String newPatientId = patientManager.generateNextPatientId();

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField contactField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Age:", ageField,
                "Contact Info:", contactField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Patient", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Validate fields before proceeding
            if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || contactField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

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
                loadPatientData("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid age entered!");
            }
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            String patientId = (String) patientTable.getValueAt(selectedRow, 0);
            
            // Step 1: Delete associated lab results for the patient
            patientManager.deleteLabResultsForPatient(patientId);
            
            // Step 2: Proceed with deleting the patient
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this patient?",
                    "Delete Patient", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                patientManager.deletePatient(patientId);
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
                loadPatientData(""); // Reload data after deletion
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
                // Validate fields before proceeding
                if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || contactField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields are required!");
                    return;
                }

                try {
                    Patient updatedPatient = new Patient(
                            patientId,  // Keep the same ID
                            nameField.getText(),
                            Integer.parseInt(ageField.getText()),
                            contactField.getText()
                    );
                    patientManager.updatePatient(updatedPatient);
                    JOptionPane.showMessageDialog(this, "Patient updated successfully!");
                    loadPatientData(""); // Reload data after update
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid age entered!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a patient to update.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagePatientsUI().setVisible(true));
    }
}

package ui;

import services.AppointmentScheduler;
import utils.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AppointmentUI extends JFrame {

    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> patientComboBox;  // JComboBox for patient selection
    private JComboBox<String> doctorComboBox;   // JComboBox for doctor selection
    private JTextField timeSlotField;
    private int selectedAppointmentId = -1; // Store selected appointment ID for update/delete

    public AppointmentUI() {
        setTitle("Manage Appointments");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
        loadAppointments();
        loadDoctors();
        loadPatients();
    }

    private void initializeComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Title label
        JLabel titleLabel = new JLabel("Appointment Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Form panel for adding/updating appointment
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Use FlowLayout
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        JLabel patientNameLabel = new JLabel("Patient Name:");
        patientNameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        patientComboBox = new JComboBox<>(); // Initialize JComboBox for patients
        patientComboBox.setPreferredSize(new Dimension(200, 30)); // Set the size of JComboBox

        JLabel doctorNameLabel = new JLabel("Doctor Name:");
        doctorNameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        doctorComboBox = new JComboBox<>(); // Initialize JComboBox for doctors
        doctorComboBox.setPreferredSize(new Dimension(200, 30)); // Set the size of JComboBox

        JLabel timeSlotLabel = new JLabel("Time Slot:");
        timeSlotLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        timeSlotField = new JTextField();
        timeSlotField.setPreferredSize(new Dimension(200, 30)); // Adjust JTextField size too

        formPanel.add(patientNameLabel);
        formPanel.add(patientComboBox);
        formPanel.add(doctorNameLabel);
        formPanel.add(doctorComboBox);
        formPanel.add(timeSlotLabel);
        formPanel.add(timeSlotField);

        // Button panel for add, update, and delete actions
JPanel buttonPanel = new JPanel();
buttonPanel.setBackground(Color.WHITE);

// Add Button
JButton addButton = new JButton("Add Appointment");
addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
addButton.setMaximumSize(new Dimension(180, 40));
addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
addButton.setForeground(Color.WHITE);
addButton.setBackground(new Color(52, 73, 94));
addButton.setBorderPainted(false);
addButton.setFocusPainted(false);
addButton.setContentAreaFilled(false);
addButton.setOpaque(true);

// Hover effect for Add Button
addButton.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseEntered(MouseEvent e) {
        addButton.setBackground(new Color(44, 62, 80));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        addButton.setBackground(new Color(52, 73, 94));
    }
});

// Update Button
JButton updateButton = new JButton("Update Appointment");
updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
updateButton.setMaximumSize(new Dimension(180, 40));
updateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
updateButton.setForeground(Color.WHITE);
updateButton.setBackground(new Color(52, 73, 94));
updateButton.setBorderPainted(false);
updateButton.setFocusPainted(false);
updateButton.setContentAreaFilled(false);
updateButton.setOpaque(true);

// Hover effect for Update Button
updateButton.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseEntered(MouseEvent e) {
        updateButton.setBackground(new Color(44, 62, 80));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        updateButton.setBackground(new Color(52, 73, 94));
    }
});

// Delete Button
JButton deleteButton = new JButton("Delete Appointment");
deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
deleteButton.setMaximumSize(new Dimension(180, 40));
deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
deleteButton.setForeground(Color.WHITE);
deleteButton.setBackground(new Color(192, 57, 43)); // Red background for delete button
deleteButton.setBorderPainted(false);
deleteButton.setFocusPainted(false);
deleteButton.setContentAreaFilled(false);
deleteButton.setOpaque(true);

// Hover effect for Delete Button
deleteButton.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseEntered(MouseEvent e) {
        deleteButton.setBackground(new Color(169, 50, 38)); // Darker red on hover
    }

    @Override
    public void mouseExited(MouseEvent e) {
        deleteButton.setBackground(new Color(192, 57, 43)); // Original red
    }
});

// Add buttons to the panel
buttonPanel.add(addButton);
buttonPanel.add(updateButton);
buttonPanel.add(deleteButton);


        addButton.setFocusPainted(false);
        updateButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Table panel to display appointments
        String[] columnNames = {"ID", "Patient Name", "Doctor Name", "Time Slot"};
        tableModel = new DefaultTableModel(columnNames, 0);
        appointmentsTable = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(appointmentsTable);

        // Add components to the main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(tableScrollPane, BorderLayout.EAST);

        add(mainPanel);

        // Action Listeners for buttons
        addButton.addActionListener(e -> addAppointment());
        updateButton.addActionListener(e -> updateAppointment());
        deleteButton.addActionListener(e -> deleteAppointment());

        // Action listener for table row selection to populate data for editing
        appointmentsTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = appointmentsTable.getSelectedRow();
            if (selectedRow != -1) {
                selectedAppointmentId = (int) appointmentsTable.getValueAt(selectedRow, 0); // ID is in column 0
                fillFormForUpdate(selectedRow);
            }
        });
    }

    private void fillFormForUpdate(int selectedRow) {
        String patientName = (String) appointmentsTable.getValueAt(selectedRow, 1); // Patient name in column 1
        String doctorName = (String) appointmentsTable.getValueAt(selectedRow, 2);  // Doctor name in column 2
        String timeSlot = (String) appointmentsTable.getValueAt(selectedRow, 3);     // Time slot in column 3

        patientComboBox.setSelectedItem(patientName);
        doctorComboBox.setSelectedItem(doctorName);
        timeSlotField.setText(timeSlot);
    }

    private void loadDoctors() {
        // Load doctors from the database into the JComboBox
        String sql = "SELECT name FROM Doctors";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String doctorName = resultSet.getString("name");
                doctorComboBox.addItem(doctorName);
            }
        } catch (SQLException e) {
            System.err.println("Failed to load doctors: " + e.getMessage());
        }
    }

    private void loadPatients() {
        // Load patients from the database into the JComboBox
        String sql = "SELECT id, name FROM Patients";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String patientName = resultSet.getString("name");
                patientComboBox.addItem(patientName);
            }
        } catch (SQLException e) {
            System.err.println("Failed to load patients: " + e.getMessage());
        }
    }

    private void addAppointment() {
        String patientName = (String) patientComboBox.getSelectedItem();  // Get selected patient name
        String doctorName = (String) doctorComboBox.getSelectedItem();    // Get selected doctor name
        String timeSlot = timeSlotField.getText();

        // Fetch patient ID based on selected name
        String patientId = getPatientIdByName(patientName);

        AppointmentScheduler scheduler = new AppointmentScheduler();
        boolean success = scheduler.scheduleAppointment(patientId, doctorName, timeSlot);
        if (success) {
            JOptionPane.showMessageDialog(this, "Appointment added successfully!");
            loadAppointments(); // Reload the appointments table
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add appointment!");
        }
    }

    private String getPatientIdByName(String patientName) {
        // Fetch the patient ID based on the selected name
        String patientId = null;
        String sql = "SELECT id FROM Patients WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patientName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                patientId = resultSet.getString("id");
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch patient ID: " + e.getMessage());
        }
        return patientId;
    }

    private void updateAppointment() {
        if (selectedAppointmentId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to update.");
            return;
        }

        String patientName = (String) patientComboBox.getSelectedItem();  // Get selected patient name
        String doctorName = (String) doctorComboBox.getSelectedItem();    // Get selected doctor name
        String timeSlot = timeSlotField.getText();

        String patientId = getPatientIdByName(patientName);  // Get patient ID from name

        AppointmentScheduler scheduler = new AppointmentScheduler();
        boolean success = scheduler.updateAppointment(selectedAppointmentId, patientId, doctorName, timeSlot);
        if (success) {
            JOptionPane.showMessageDialog(this, "Appointment updated successfully!");
            loadAppointments(); // Reload the appointments table
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update appointment!");
        }
    }

    private void deleteAppointment() {
        if (selectedAppointmentId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to delete.");
            return;
        }

        AppointmentScheduler scheduler = new AppointmentScheduler();
        boolean success = scheduler.deleteAppointmentById(selectedAppointmentId); // Delete by appointment ID
        if (success) {
            JOptionPane.showMessageDialog(this, "Appointment deleted successfully!");
            loadAppointments(); // Reload the appointments table
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete appointment!");
        }
    }

    private void loadAppointments() {
    String sql = "SELECT a.id, p.name AS patient_name, a.doctor_name, a.time_slot " +
                 "FROM Appointments a " +
                 "JOIN Patients p ON a.patient_id = p.id";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {

        // Clear the table before loading new data
        tableModel.setRowCount(0);

        // Iterate through the ResultSet and populate the table
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String patientName = resultSet.getString("patient_name");
            String doctorName = resultSet.getString("doctor_name");
            String timeSlot = resultSet.getString("time_slot");

            // Add the row to the table model
            tableModel.addRow(new Object[]{id, patientName, doctorName, timeSlot});
        }

    } catch (SQLException e) {
        System.err.println("Failed to load appointments: " + e.getMessage());
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentUI().setVisible(true));
    }
}

package ui;

import utils.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LabResultUI extends JFrame {
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JTextField testNameField;
    private JTextField resultValueField;
    private JTextField testDateField;
    private JTextField searchField;
    private JComboBox<String> patientIdComboBox;  // JComboBox to select existing Patient IDs

    public LabResultUI() {
        setTitle("Lab Results Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
        loadLabResults();
    }

    private void initializeComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Lab Results Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        JLabel searchLabel = new JLabel("Search (Patient ID/Name):");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchLabResults());
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Table panel
        String[] columnNames = {"Result ID", "Patient ID", "Patient Name", "Test Name", "Result Value", "Test Date"};
tableModel = new DefaultTableModel(columnNames, 0);

        resultsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(resultsTable);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(Color.WHITE);

        JLabel patientIdLabel = new JLabel("Patient ID:");
        patientIdComboBox = new JComboBox<>();
        loadPatientIds();  // Populate the JComboBox with existing Patient IDs
        JLabel testNameLabel = new JLabel("Test Name:");
        testNameField = new JTextField();
        JLabel resultValueLabel = new JLabel("Result Value:");
        resultValueField = new JTextField();
        JLabel testDateLabel = new JLabel("Test Date (YYYY-MM-DD):");
        testDateField = new JTextField();

        formPanel.add(patientIdLabel);
        formPanel.add(patientIdComboBox);
        formPanel.add(testNameLabel);
        formPanel.add(testNameField);
        formPanel.add(resultValueLabel);
        formPanel.add(resultValueField);
        formPanel.add(testDateLabel);
        formPanel.add(testDateField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("Add");
        addButton.addActionListener(e -> addLabResult());
        JButton editButton = createStyledButton("Edit");
        editButton.addActionListener(e -> editLabResult());
        JButton deleteButton = createStyledButton("Delete", Color.RED);
        deleteButton.addActionListener(e -> deleteLabResult());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Combine components
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    private JButton createStyledButton(String text) {
        return createStyledButton(text, new Color(52, 73, 94));
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void loadPatientIds() {
        String sql = "SELECT id FROM Patients";  // Assuming you have a Patients table
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String patientId = resultSet.getString("id");
                patientIdComboBox.addItem(patientId);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patient IDs: " + e.getMessage());
        }
    }

    private void loadLabResults() {
    String sql = "SELECT LabResults.result_id, LabResults.patient_id, Patients.name AS patient_name, LabResults.test_name, LabResults.result_value, LabResults.test_date "
               + "FROM LabResults "
               + "JOIN Patients ON LabResults.patient_id = Patients.id";  // Join with Patients table to get patient name

    try (Connection connection = DatabaseConnection.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {

        tableModel.setRowCount(0); // Clear table before loading data
        while (resultSet.next()) {
            int resultId = resultSet.getInt("result_id");
            String patientId = resultSet.getString("patient_id");
            String patientName = resultSet.getString("patient_name"); // Get patient name
            String testName = resultSet.getString("test_name");
            String resultValue = resultSet.getString("result_value");
            String testDate = resultSet.getString("test_date");

            // Add row to table with patient name included
            tableModel.addRow(new Object[]{resultId, patientId, patientName, testName, resultValue, testDate});
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading lab results: " + e.getMessage());
    }
}

    private void addLabResult() {
        String patientId = (String) patientIdComboBox.getSelectedItem();  // Automatically use selected Patient ID
        String testName = testNameField.getText();
        String resultValue = resultValueField.getText();
        String testDate = testDateField.getText();

        String sql = "INSERT INTO LabResults (patient_id, test_name, result_value, test_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patientId);
            statement.setString(2, testName);
            statement.setString(3, resultValue);
            statement.setString(4, testDate);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Lab result added successfully!");
            loadLabResults();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding lab result: " + e.getMessage());
        }
    }

    private void editLabResult() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            return;
        }

        int resultId = (int) tableModel.getValueAt(selectedRow, 0);
        String patientId = (String) patientIdComboBox.getSelectedItem();  // Automatically use selected Patient ID
        String testName = testNameField.getText();
        String resultValue = resultValueField.getText();
        String testDate = testDateField.getText();

        String sql = "UPDATE LabResults SET patient_id = ?, test_name = ?, result_value = ?, test_date = ? WHERE result_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patientId);
            statement.setString(2, testName);
            statement.setString(3, resultValue);
            statement.setString(4, testDate);
            statement.setInt(5, resultId);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Lab result updated successfully!");
            loadLabResults();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating lab result: " + e.getMessage());
        }
    }

    private void deleteLabResult() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }

        int resultId = (int) tableModel.getValueAt(selectedRow, 0);

        String sql = "DELETE FROM LabResults WHERE result_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, resultId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Lab result deleted successfully!");
            loadLabResults();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting lab result: " + e.getMessage());
        }
    }

    private void searchLabResults() {
        String searchText = searchField.getText();
        String sql = "SELECT * FROM LabResults WHERE patient_id LIKE ? OR test_name LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + searchText + "%");
            statement.setString(2, "%" + searchText + "%");
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear table before loading data
            while (resultSet.next()) {
                int resultId = resultSet.getInt("result_id");
                String patientId = resultSet.getString("patient_id");
                String testName = resultSet.getString("test_name");
                String resultValue = resultSet.getString("result_value");
                String testDate = resultSet.getString("test_date");
                tableModel.addRow(new Object[]{resultId, patientId, testName, resultValue, testDate});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading lab results: " + e.getMessage());
        }
    }
}

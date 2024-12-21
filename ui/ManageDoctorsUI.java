package ui;

import utils.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManageDoctorsUI extends JFrame {
    private JTable doctorsTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField specializationField;
    private JTextField phoneField;
    private JTextField emailField;

    public ManageDoctorsUI() {
        setTitle("Manage Doctors");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
        loadDoctors();
    }

    private void initializeComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Manage Doctors");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table panel
        String[] columnNames = {"ID", "Name", "Specialization", "Phone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        doctorsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(doctorsTable);

        // Form panel for doctor details
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Doctor Name:");
        nameField = new JTextField();
        JLabel specializationLabel = new JLabel("Specialization:");
        specializationField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(specializationLabel);
        formPanel.add(specializationField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("Add");
        addButton.addActionListener(e -> addDoctor());
        JButton editButton = createStyledButton("Edit");
        editButton.addActionListener(e -> editDoctor());
        JButton deleteButton = createStyledButton("Delete", Color.RED);
        deleteButton.addActionListener(e -> deleteDoctor());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Combine components
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

    private void loadDoctors() {
        String sql = "SELECT * FROM doctors";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            tableModel.setRowCount(0); // Clear table before loading data
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                tableModel.addRow(new Object[]{id, name, specialization, phone, email});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading doctors: " + e.getMessage());
        }
    }

    private void addDoctor() {
        // Validate required fields
        if (nameField.getText().isEmpty() || specializationField.getText().isEmpty() ||
            phoneField.getText().isEmpty() || emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        // Automatically generate the next doctor ID
        String doctorId = generateNextDoctorId();

        // Get the name, specialization, phone, and email from the user input
        String name = nameField.getText();
        String specialization = specializationField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        String sql = "INSERT INTO doctors (id, name, specialization, phone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, doctorId);
            statement.setString(2, name);
            statement.setString(3, specialization);
            statement.setString(4, phone);
            statement.setString(5, email);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor added successfully!");
            loadDoctors();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding doctor: " + e.getMessage());
        }
    }

    private String generateNextDoctorId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(id, 2) AS UNSIGNED)) FROM doctors WHERE id LIKE 'D%'";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                int nextId = resultSet.getInt(1) + 1;
                return String.format("D%03d", nextId);
            } else {
                return "D001";
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error generating doctor ID: " + e.getMessage());
            return null;
        }
    }

    private void editDoctor() {
        // Validate required fields
        if (nameField.getText().isEmpty() || specializationField.getText().isEmpty() ||
            phoneField.getText().isEmpty() || emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        int selectedRow = doctorsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String name = nameField.getText();
        String specialization = specializationField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        String sql = "UPDATE doctors SET name = ?, specialization = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, specialization);
            statement.setString(3, phone);
            statement.setString(4, email);
            statement.setString(5, id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor updated successfully!");
            loadDoctors();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating doctor: " + e.getMessage());
        }
    }

    private void deleteDoctor() {
        int selectedRow = doctorsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);

        String sql = "DELETE FROM doctors WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor deleted successfully!");
            loadDoctors();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting doctor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageDoctorsUI().setVisible(true));
    }
}

package medicalclinicmanagement.clinic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class MedicalClinicGUI extends JFrame {

    private DatabaseConnection db;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public MedicalClinicGUI() {
        // Initialize Database Connection
        db = DatabaseConnection.getInstance();

        // Main Frame Setup
        setTitle("Medical Clinic Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Light Gray Background
        setContentPane(mainPanel);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Sidebar Panel
        JPanel sidebarPanel = createSidebarPanel();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Content Panel
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Add cards to cardPanel
        cardPanel.add(createDashboardPanel(), "Dashboard"); // Dashboard Panel
        cardPanel.add(createTablePanel("patients", "Patients"), "Patients");
        cardPanel.add(createTablePanel("doctors", "Doctors"), "Doctors");
        cardPanel.add(createTablePanel("appointments", "Appointments"), "Appointments");
        cardPanel.add(createTablePanel("billing", "Billing"), "Billing");
        cardPanel.add(createTablePanel("medical_history", "Medical History"), "Medical History");

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Show Dashboard by Default
        cardLayout.show(cardPanel, "Dashboard");

        // Show Frame
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185)); // Flat Blue
        headerPanel.setPreferredSize(new Dimension(1200, 80));
        headerPanel.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Medical Clinic Management System");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
        headerPanel.add(headerLabel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(52, 73, 94)); // Dark Blue-Gray
        sidebarPanel.setPreferredSize(new Dimension(200, 720));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        String[] buttonLabels = { "Dashboard", "Patients", "Doctors", "Appointments", "Billing", "Medical History" };
        for (String label : buttonLabels) {
            JButton button = createStyledButton(label);
            sidebarPanel.add(button);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return sidebarPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> cardLayout.show(cardPanel, text));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(44, 62, 80));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
        });
        return button;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BorderLayout());
        dashboardPanel.setBackground(Color.WHITE);
        dashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome to the Medical Clinic Management System!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(41, 128, 185)); // Flat Blue
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dashboardPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Metrics Panel (GridLayout for cards)
        JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        metricsPanel.setBackground(Color.WHITE);
        metricsPanel.setBorder(new EmptyBorder(20, 0, 0, 0)); // Add some space below the welcome message

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            metricsPanel.add(createDashboardCard("Total Patients", getCountFromTable(conn, "patients")));
            metricsPanel.add(createDashboardCard("Upcoming Appointments", getCountFromTable(conn, "appointments")));
            metricsPanel.add(createDashboardCard("Total Revenue", getSumFromColumn(conn, "billing", "amount")));
            metricsPanel.add(createDashboardCard("Active Doctors", getCountFromTable(conn, "doctors")));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error fetching dashboard data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        dashboardPanel.add(metricsPanel, BorderLayout.CENTER);

        return dashboardPanel;
    }

    private JPanel createDashboardCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(236, 240, 241)); // Light Gray
        card.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(44, 62, 80)); // Dark Gray
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel, BorderLayout.NORTH);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(new Color(41, 128, 185)); // Flat Blue
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private String getCountFromTable(Connection conn, String tableName) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM " + tableName;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return String.valueOf(rs.getInt("count"));
            }
        }
        return "0";
    }

    private String getSumFromColumn(Connection conn, String tableName, String columnName) throws SQLException {
        String query = "SELECT SUM(" + columnName + ") AS total FROM " + tableName;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return "$" + rs.getDouble("total");
            }
        }
        return "$0";
    }

    private JPanel createTablePanel(String tableName, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        // Fetch and Populate Data
        populateCustomTable(tableName, model);

        // Hide the ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(Color.WHITE);

        JButton insertButton = createStyledButton("Insert");
        JButton editButton = createStyledButton("Edit");
        JButton deleteButton = createStyledButton("Delete");

        actionPanel.add(insertButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        // Button Actions
        insertButton.addActionListener(e -> openInsertDialog(tableName, model));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) model.getValueAt(selectedRow, 0); // Retrieve the hidden ID
                openEditDialog(tableName, id, table, model); // Pass JTable and model to the dialog
            } else {
                JOptionPane.showMessageDialog(this, "Please select a record to edit.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) model.getValueAt(selectedRow, 0); // Retrieve the hidden ID
                deleteRecord(tableName, id);
                model.removeRow(selectedRow); // Remove from UI
            } else {
                JOptionPane.showMessageDialog(this, "Please select a record to delete.");
            }
        });

        return panel;
    }

    private void populateCustomTable(String tableName, DefaultTableModel model) {
        String query;
        Vector<String> columnNames = new Vector<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement()) {

            // Custom Query and Column Names
            switch (tableName) {
                case "patients":
                    query = """
                                SELECT id AS `Patient ID`,
                                       name AS `Patient Name`,
                                       age AS `Age`,
                                       gender AS `Gender`,
                                       contact AS `Contact`
                                FROM patients
                            """;
                    columnNames.add("Patient ID"); // ID column for internal use
                    columnNames.add("Patient Name");
                    columnNames.add("Age");
                    columnNames.add("Gender");
                    columnNames.add("Contact");
                    break;

                case "doctors":
                    query = """
                                SELECT id AS `Doctor ID`,
                                       name AS `Doctor Name`,
                                       specialty AS `Specialty`,
                                       contact AS `Contact`
                                FROM doctors
                            """;
                    columnNames.add("Doctor ID"); // ID column for internal use
                    columnNames.add("Doctor Name");
                    columnNames.add("Specialty");
                    columnNames.add("Contact");
                    break;

                case "appointments":
                    query = """
                                SELECT a.id AS `Appointment ID`,
                                       p.name AS `Patient Name`,
                                       d.name AS `Doctor Name`,
                                       a.appointment_date AS `Appointment Date`,
                                       a.notes AS `Notes`
                                FROM appointments a
                                JOIN patients p ON a.patient_id = p.id
                                JOIN doctors d ON a.doctor_id = d.id
                            """;
                    columnNames.add("Appointment ID"); // ID column for internal use
                    columnNames.add("Patient Name");
                    columnNames.add("Doctor Name");
                    columnNames.add("Appointment Date");
                    columnNames.add("Notes");
                    break;

                case "billing":
                    query = """
                                SELECT b.id AS `Billing ID`,
                                       p.name AS `Patient Name`,
                                       b.amount AS `Amount`,
                                       b.billing_date AS `Billing Date`
                                FROM billing b
                                JOIN patients p ON b.patient_id = p.id
                            """;
                    columnNames.add("Billing ID"); // ID column for internal use
                    columnNames.add("Patient Name");
                    columnNames.add("Amount");
                    columnNames.add("Billing Date");
                    break;

                case "medical_history":
                    query = """
                                SELECT mh.id AS `History ID`,
                                       p.name AS `Patient Name`,
                                       mh.history_details AS `History Details`
                                FROM medical_history mh
                                JOIN patients p ON mh.patient_id = p.id
                            """;
                    columnNames.add("History ID"); // ID column for internal use
                    columnNames.add("Patient Name");
                    columnNames.add("History Details");
                    break;

                default:
                    query = "SELECT * FROM " + tableName;
                    ResultSetMetaData metaData = stmt.executeQuery(query).getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames.add(metaData.getColumnName(i));
                    }
            }

            // Execute Query and Populate Data
            ResultSet rs = stmt.executeQuery(query);
            model.setColumnIdentifiers(columnNames);

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnNames.size(); i++) { // Include ID
                    row.add(rs.getObject(i));
                }
                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String validateFields(ArrayList<JTextField> fields, ArrayList<String> columnNames) {
        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            String value = fields.get(i).getText().trim();

            switch (columnName.toLowerCase()) {
                case "gender":
                    if (!value.equalsIgnoreCase("Male") && !value.equalsIgnoreCase("Female")) {
                        return "Gender must be 'Male' or 'Female'.";
                    }
                    break;

                case "age":
                    try {
                        int age = Integer.parseInt(value);
                        if (age <= 0) {
                            return "Age must be a positive integer.";
                        }
                    } catch (NumberFormatException e) {
                        return "Age must be a valid integer.";
                    }
                    break;

                case "contact":
                    if (!value.matches("\\d+")) {
                        return "Contact must be a numeric value.";
                    }
                    if (value.length() != 10) {
                        return "Contact must be a 10-digit number.";
                    }
                    break;

                default:
                    // No validation for other fields
                    break;
            }
        }

        return ""; // No errors
    }

    private void openInsertDialog(String tableName, DefaultTableModel model) {
        JDialog insertDialog = new JDialog(this, "Insert New Record", true);
        insertDialog.setSize(400, 300);
        insertDialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        ArrayList<JTextField> fields = new ArrayList<>();
        ArrayList<String> columnNames = new ArrayList<>();

        // Fetch columns dynamically (skipping "id")
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM " + tableName + " LIMIT 1";
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    for (int i = 2; i <= columnCount; i++) { // Skip ID
                        String columnName = metaData.getColumnName(i);
                        columnNames.add(columnName);

                        JLabel label = new JLabel(columnName);
                        JTextField textField = new JTextField();
                        formPanel.add(label);
                        formPanel.add(textField);

                        fields.add(textField);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching columns: " + e.getMessage());
            return;
        }

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Perform validation
            String validationError = validateFields(fields, columnNames);
            if (!validationError.isEmpty()) {
                JOptionPane.showMessageDialog(insertDialog, validationError, "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert into database
            try (Connection conn = DatabaseConnection.getInstance().getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(buildInsertQuery(tableName, columnNames),
                            Statement.RETURN_GENERATED_KEYS)) {

                for (int i = 0; i < fields.size(); i++) {
                    pstmt.setString(i + 1, fields.get(i).getText());
                }

                pstmt.executeUpdate();

                // Retrieve generated ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);

                        // Add new row to table model
                        Vector<Object> row = new Vector<>();
                        row.add(generatedId);
                        for (JTextField field : fields) {
                            row.add(field.getText());
                        }
                        model.addRow(row);
                    }
                }

                JOptionPane.showMessageDialog(insertDialog, "Record inserted successfully!");
                insertDialog.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(insertDialog, "Error inserting record: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel());
        formPanel.add(saveButton);

        insertDialog.add(formPanel);
        insertDialog.setVisible(true);
    }

    private String buildInsertQuery(String tableName, ArrayList<String> columnNames) {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");

        for (int i = 0; i < columnNames.size(); i++) {
            query.append(columnNames.get(i));
            if (i < columnNames.size() - 1)
                query.append(", ");
        }

        query.append(") VALUES (");
        for (int i = 0; i < columnNames.size(); i++) {
            query.append("?");
            if (i < columnNames.size() - 1)
                query.append(", ");
        }
        query.append(")");
        return query.toString();
    }

    private void openEditDialog(String tableName, int id, JTable table, DefaultTableModel model) {
        JDialog editDialog = new JDialog(this, "Edit Record", true);
        editDialog.setSize(400, 300);
        editDialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        ArrayList<JTextField> fields = new ArrayList<>();
        ArrayList<String> columnNames = new ArrayList<>();

        // Fetch current data for the record
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM " + tableName + " WHERE id = " + id;
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    for (int i = 2; i <= columnCount; i++) { // Skip ID
                        String columnName = metaData.getColumnName(i);
                        columnNames.add(columnName);

                        JLabel label = new JLabel(columnName);
                        JTextField textField = new JTextField(rs.getString(columnName));
                        formPanel.add(label);
                        formPanel.add(textField);

                        fields.add(textField);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching record: " + e.getMessage());
            return;
        }

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Perform validation
            String validationError = validateFields(fields, columnNames);
            if (!validationError.isEmpty()) {
                JOptionPane.showMessageDialog(editDialog, validationError, "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update record in the database
            try (Connection conn = DatabaseConnection.getInstance().getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(buildUpdateQuery(tableName, columnNames, id))) {

                for (int i = 0; i < fields.size(); i++) {
                    pstmt.setString(i + 1, fields.get(i).getText());
                }

                pstmt.executeUpdate();

                // Update the table model
                for (int i = 0; i < fields.size(); i++) {
                    model.setValueAt(fields.get(i).getText(), table.getSelectedRow(), i + 1);
                }

                JOptionPane.showMessageDialog(editDialog, "Record updated successfully!");
                editDialog.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(editDialog, "Error updating record: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel());
        formPanel.add(saveButton);

        editDialog.add(formPanel);
        editDialog.setVisible(true);
    }

    private String buildUpdateQuery(String tableName, ArrayList<String> columnNames, int id) {
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(tableName).append(" SET ");

        for (int i = 0; i < columnNames.size(); i++) {
            query.append(columnNames.get(i)).append(" = ?");
            if (i < columnNames.size() - 1) {
                query.append(", ");
            }
        }

        query.append(" WHERE id = ").append(id);
        return query.toString();
    }

    private void deleteRecord(String tableName, int id) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement()) {

            // Delete dependent records if any
            if ("patients".equals(tableName)) {
                String deleteAppointmentsQuery = "DELETE FROM appointments WHERE patient_id = " + id;
                String deleteBillingQuery = "DELETE FROM billing WHERE patient_id = " + id;
                String deleteHistoryQuery = "DELETE FROM medical_history WHERE patient_id = " + id;

                stmt.executeUpdate(deleteAppointmentsQuery);
                stmt.executeUpdate(deleteBillingQuery);
                stmt.executeUpdate(deleteHistoryQuery);
            } else if ("doctors".equals(tableName)) {
                String deleteAppointmentsQuery = "DELETE FROM appointments WHERE doctor_id = " + id;
                stmt.executeUpdate(deleteAppointmentsQuery);
            }

            // Delete the main record
            String deleteQuery = "DELETE FROM " + tableName + " WHERE id = " + id;
            stmt.executeUpdate(deleteQuery);

            JOptionPane.showMessageDialog(this, "Record deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage());
        }
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(41, 128, 185)); // Flat Blue
        footerPanel.setPreferredSize(new Dimension(1200, 40));

        JLabel footerLabel = new JLabel(
                "© 2024 Medical Clinic Management System | For Selected Labs in Software Engineering Project");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);

        return footerPanel;
    }
}

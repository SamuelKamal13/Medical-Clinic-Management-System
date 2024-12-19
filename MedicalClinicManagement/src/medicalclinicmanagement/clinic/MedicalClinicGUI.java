package medicalclinicmanagement.clinic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
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

        String[] buttonLabels = {"Dashboard", "Patients", "Doctors", "Appointments", "Billing", "Medical History"};
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

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement()) {

            String query;
            Vector<String> columnNames = new Vector<>();

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
                    columnNames.add("Patient ID");
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
                    columnNames.add("Doctor ID");
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
                    columnNames.add("Appointment ID");
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
                    columnNames.add("Billing ID");
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
                    columnNames.add("History ID");
                    columnNames.add("Patient Name");
                    columnNames.add("History Details");
                    break;

                default:
                    query = "SELECT * FROM " + tableName; // Default for other tables
            }

            ResultSet rs = stmt.executeQuery(query);

            // Set Column Names
            if (!columnNames.isEmpty()) {
                model.setColumnIdentifiers(columnNames);
            } else {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                        columnNames.add(metaData.getColumnName(i));
                }
                model.setColumnIdentifiers(columnNames);
            }

            // Populate Table Rows
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnNames.size(); i++) {
                    row.add(rs.getObject(i));
                }
                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data from " + title + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(41, 128, 185)); // Flat Blue
        footerPanel.setPreferredSize(new Dimension(1200, 40));

        JLabel footerLabel = new JLabel("© 2024 Medical Clinic Management System | For Selected Labs in Software Engineering Project");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);

        return footerPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicalClinicGUI::new);
    }
}

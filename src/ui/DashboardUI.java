package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardUI extends JFrame {

    public DashboardUI() {
        setTitle("Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(41, 128, 185)); // Unified Theme Color
        add(mainPanel);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout()); // Use BorderLayout for precise alignment
        headerPanel.setBackground(new Color(31, 97, 141));
        headerPanel.setPreferredSize(new Dimension(1200, 80));
        JLabel headerLabel = new JLabel("Medical Clinic Management System");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Add left padding
        headerPanel.add(headerLabel, BorderLayout.WEST); // Align to the left
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Sidebar Panel
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(52, 73, 94));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(250, 800));

        // Create buttons for sidebar
        JButton managePatientsButton = createSidebarButton("Manage Patients");
        JButton manageDoctorsButton = createSidebarButton("Manage Doctors"); // New button for Manage Doctors
        JButton appointmentsButton = createSidebarButton("Appointments");
        JButton labResultsButton = createSidebarButton("Lab Results");
        JButton logoutButton = createSidebarButton("Logout");

        // Action Listeners for buttons
        managePatientsButton.addActionListener(e -> new ManagePatientsUI().setVisible(true));
        manageDoctorsButton.addActionListener(e -> new ManageDoctorsUI().setVisible(true)); // Action for Manage Doctors
        appointmentsButton.addActionListener(e -> new AppointmentUI().setVisible(true));
        labResultsButton.addActionListener(e -> new LabResultUI().setVisible(true));
        logoutButton.addActionListener(e -> {
            new LoginUI().setVisible(true);
            dispose();
        });

        // Add buttons to sidebar
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(managePatientsButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(manageDoctorsButton); // Adding Manage Doctors button
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(appointmentsButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(labResultsButton);
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(logoutButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("Welcome to the Dashboard!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(41, 128, 185));
        contentPanel.add(welcomeLabel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(31, 97, 141));
        footerPanel.setPreferredSize(new Dimension(1200, 40));
        JLabel footerLabel = new JLabel("© 2024 Medical Clinic Management System | For Selected Labs in Software Engineering Project");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40)); // Adjusted size for better consistency
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false); // Disable default gradient
        button.setOpaque(true); // Enable solid background

        // Add hover effect for darker color
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(44, 62, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 73, 94));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardUI().setVisible(true));
    }
}

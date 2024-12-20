/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;

public class AppointmentUI extends JFrame {

    public AppointmentUI() {
        setTitle("Manage Appointments");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
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

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        JLabel patientIdLabel = new JLabel("Patient ID:");
        JTextField patientIdField = new JTextField();
        JLabel doctorIdLabel = new JLabel("Doctor ID:");
        JTextField doctorIdField = new JTextField();
        JLabel timeSlotLabel = new JLabel("Time Slot:");
        JTextField timeSlotField = new JTextField();

        formPanel.add(patientIdLabel);
        formPanel.add(patientIdField);
        formPanel.add(doctorIdLabel);
        formPanel.add(doctorIdField);
        formPanel.add(timeSlotLabel);
        formPanel.add(timeSlotField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton scheduleButton = new JButton("Schedule Appointment");
        scheduleButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        scheduleButton.setBackground(new Color(33, 150, 243));
        scheduleButton.setForeground(Color.WHITE);
        scheduleButton.setFocusPainted(false);

        buttonPanel.add(scheduleButton);

        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentUI().setVisible(true));
    }
}

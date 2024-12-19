package medicalclinicmanagement.clinic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3366/medicalclinic?autoReconnect=true&useSSL=false";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "root"; // Replace with your MySQL password

    private DatabaseConnection() {
        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("Database Driver not found! " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Failed to connect to database! " + e.getMessage());
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else {
            try {
                if (instance.getConnection() == null || instance.getConnection().isClosed()) {
                    System.out.println("Reconnecting to database...");
                    instance.connect();
                }
            } catch (SQLException e) {
                System.err.println("Error checking database connection: " + e.getMessage());
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection: " + e.getMessage());
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import utils.DatabaseConnection;
import utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    public boolean validateLogin(String username, String password) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "SELECT password_hash FROM Users WHERE username = ?";
        System.out.println("Executing SQL Query: " + sql + " with username: " + username);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String storedHash = resultSet.getString("password_hash");
            String enteredHash = PasswordUtils.hashPassword(password);
            System.out.println("Stored Hash: " + storedHash + ", Entered Hash: " + enteredHash);
            return storedHash.equals(enteredHash);
        }
        return false;
    }
}

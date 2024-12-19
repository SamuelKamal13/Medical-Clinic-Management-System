/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package medicalclinicmanagement.clinic;

/**
 *
 * @author Samoueel
 */

public class UserFactory {
    public static User createUser(String role, String name, int id) {
        switch (role.toLowerCase()) {
            case "doctor":
                return new Doctor(name, id);
            case "receptionist":
                return new Receptionist(name, id);
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package medicalclinicmanagement.clinic;

/**
 *
 * @author Samoueel
 */

public class Receptionist extends User {
    Receptionist(String name, int id) {
        super(name, id);
    }

    @Override
    public void displayRole() {
        System.out.println("I am a Receptionist: " + name);
    }
}

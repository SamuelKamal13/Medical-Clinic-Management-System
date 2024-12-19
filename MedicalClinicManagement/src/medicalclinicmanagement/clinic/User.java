/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package medicalclinicmanagement.clinic;

/**
 *
 * @author Samoueel
 */

public abstract class User {
    String name;
    int id;

    User(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public abstract void displayRole();
}

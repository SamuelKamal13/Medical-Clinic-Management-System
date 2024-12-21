/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import models.*;

public class DoctorFactory {
    public static Doctor createDoctor(String specialization, String id, String name) {
        switch (specialization) {
            case "Cardiologist":
                return new Cardiologist(id, name);
            case "Neurologist":
                return new Neurologist(id, name);
            case "General Practitioner":
                return new GeneralPractitioner(id, name);
            default:
                throw new IllegalArgumentException("Unknown specialization: " + specialization);
        }
    }
}

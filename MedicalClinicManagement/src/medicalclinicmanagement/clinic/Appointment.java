/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package medicalclinicmanagement.clinic;

/**
 *
 * @author Samoueel
 */

import java.util.Date;

public class Appointment {
    private String patientName;
    private String doctorName;
    private Date date;
    private String notes;

    private Appointment(AppointmentBuilder builder) {
        this.patientName = builder.patientName;
        this.doctorName = builder.doctorName;
        this.date = builder.date;
        this.notes = builder.notes;
    }

    public static class AppointmentBuilder {
        private String patientName;
        private String doctorName;
        private Date date;
        private String notes;

        public AppointmentBuilder(String patientName, String doctorName) {
            this.patientName = patientName;
            this.doctorName = doctorName;
        }

        public AppointmentBuilder setDate(Date date) {
            this.date = date;
            return this;
        }

        public AppointmentBuilder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public Appointment build() {
            return new Appointment(this);
        }
    }

    @Override
    public String toString() {
        return "Appointment [patientName=" + patientName + ", doctorName=" + doctorName + ", date=" + date + ", notes=" + notes + "]";
    }
}

package com.example.OnlineMedicalAppointment.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId; // Import ZoneId

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

import java.sql.Timestamp; // Use java.sql.Timestamp

/**
 * Represents an appointment in the Online Medical Appointment system.
 * Stores appointment details such as patient, doctor, time, and status.
 */
public class Appointment {

        private int scheduleID;
        private int patientID; // User ID who made the appointment
        private int doctorID; // User ID of the doctor
        private LocalDate bookedOn; // Date when the appointment was booked
        private LocalDateTime appointmentTime;
        private String status; // e.g., "Pending", "Approved", "Rejected", "Cancelled", "Held"

        /**
         * Default constructor.
         */
        public Appointment() {
        }

        /**
         * Constructs an Appointment with patient, doctor, time, and status.
         * @param madeBy patient ID
         * @param doctorID doctor ID
         * @param appointmentTime appointment time
         * @param status appointment status
         */
        public Appointment(int madeBy, 
        int doctorID, 
        java.time.LocalDateTime appointmentTime,
        String status) {
            this.patientID = madeBy;
            this.doctorID = doctorID;
            this.appointmentTime = appointmentTime;
            this.status = status;
            this.bookedOn = LocalDate.now(); // Set bookedOn to current date
        }
        /**
         * Constructs an Appointment with schedule ID and time as epoch millis.
         * @param scheduleID schedule ID
         * @param madeBy patient ID
         * @param doctorID doctor ID
         * @param appointmentTime appointment time in epoch millis
         * @param status appointment status
         */
        public Appointment(int scheduleID,
        int madeBy,
        int doctorID,
        Timestamp appointmentTime,
        Timestamp bookedOn,
        String status) {
            this.scheduleID = scheduleID;
            this.patientID = madeBy;
            this.doctorID = doctorID;
            this.bookedOn = bookedOn.toLocalDateTime().toLocalDate();
            this.appointmentTime = appointmentTime.toLocalDateTime();
        }
        /**
         * Constructs an Appointment with patient, doctor, and time.
         * @param patientID patient ID
         * @param doctorID doctor ID
         * @param appointmentTime appointment time
         * This constructor sets the status to "Pending".
         */
        public Appointment(int patientID, int doctorID, Timestamp appointmentTime, Timestamp bookedOn ){
            this.patientID = patientID;
            this.doctorID = doctorID;
            this.bookedOn = bookedOn.toLocalDateTime().toLocalDate();
            this.appointmentTime = appointmentTime.toLocalDateTime();
            this.status = "Pending"; 
        }

        /**
         * Gets the schedule ID.
         * @return the schedule ID
         */
        public int getScheduleID() {
            return scheduleID;
        }

        /**
         * Sets the schedule ID.
         * @param scheduleID the schedule ID
         */
        public void setScheduleID(int scheduleID) {
            this.scheduleID = scheduleID;
        }

        /**
         * Gets the patient ID.
         * @return the patient ID
         */
        public int getPatientID() {
            return patientID;
        }

        /**
         * Sets the patient ID.
         * @param madeBy the patient ID
         */
        public void setPatientID(int madeBy) {
            this.patientID = madeBy;
        }

        /**
         * Gets the doctor ID.
         * @return the doctor ID
         */
        public int getDoctorID() {
            return doctorID;
        }

        /**
         * Sets the doctor ID.
         * @param doctorID the doctor ID
         */
        public void setDoctorID(int doctorID) {
            this.doctorID = doctorID;
        }

        /**
         * Gets the doctor's full name.
         * @return the doctor's full name
         */
        public String getDoctorName() {
            User doctor = DatabaseAccessor.getUserByID(doctorID);
            return doctor.getFName() + " " + doctor.getLName();
        }

        /**
         * Gets the appointment time.
         * @return the appointment time
         */
        public java.time.LocalDateTime getAppointmentTime() {
            return appointmentTime;
        }

        /**
         * Sets the appointment time.
         * @param startTime the appointment time
         */
        public void setAppointmentTime(java.time.LocalDateTime startTime) {
            this.appointmentTime = startTime;
        }

        /**
         * Gets the status of the appointment.
         * @return the status
         */
        public String getStatus() {
            return status;
        }

        /**
         * Sets the status of the appointment.
         * @param status the status
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * Returns a string representation of the Appointment.
         * @return string representation
         */
        @Override
        public String toString() {
            return "Appointment{" +
                   "scheduleID=" + scheduleID +
                   ", patientName=" + DatabaseAccessor.getUserByID(patientID)+
                   ", doctor=" + DatabaseAccessor.getUserByID(doctorID).getFName() + DatabaseAccessor.getUserByID(doctorID).getLName()  +
                   ", appointmentTime=" + appointmentTime +
                   ", bookedOn=" + bookedOn +
                   ", status='" + status + '\'' +
                   '}';
        }
}

package com.example.OnlineMedicalAppointment.model;

import java.time.Instant;
import java.time.LocalDateTime;

public class Appointment {

        private int scheduleID;
        private int patientID; // User ID who made the appointment
        private int doctorID; // User ID of the doctor
        private java.time.LocalDateTime appointmentTime;
        private String status; // e.g., "Pending", "Approved", "Rejected", "Cancelled", "Held"

        // Constructor
        public Appointment() {
        }

        public Appointment(int madeBy, 
        int doctorID, 
        java.time.LocalDateTime appointmentTime,
        String status) {
            this.patientID = madeBy;
            this.doctorID = doctorID;
            this.appointmentTime = appointmentTime;
            this.status = status;
        }
        public Appointment(int scheduleID,
        int madeBy, 
        int doctorID,
        Long appointmentTime,
        String status) {
            this.scheduleID = scheduleID;
            this.patientID = madeBy;
            this.doctorID = doctorID;
            this.appointmentTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(appointmentTime), java.time.ZoneOffset.systemDefault());
            this.status = status;
        }

        // Getters and Setters
        public int getScheduleID() {
            return scheduleID;
        }

        public void setScheduleID(int scheduleID) {
            this.scheduleID = scheduleID;
        }

        public int getMadeBy() {
            return patientID;
        }

        public void setMadeBy(int madeBy) {
            this.patientID = madeBy;
        }

        public int getDoctorID() {
            return doctorID;
        }

        public void setDoctorID(int doctorID) {
            this.doctorID = doctorID;
        }

        public java.time.LocalDateTime getAppointmentTime() {
            return appointmentTime;
        }

        public void setAppointmentTime(java.time.LocalDateTime startTime) {
            this.appointmentTime = startTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Appointment{" +
                   "scheduleID=" + scheduleID +
                   ", madeBy=" + patientID +
                   ", doctorID=" + doctorID +
                   ", appointmentTime=" + appointmentTime +
                   ", status='" + status + '\'' +
                   '}';
        }
}

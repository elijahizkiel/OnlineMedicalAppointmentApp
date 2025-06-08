package com.example.OnlineMedicalAppointment.model;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

/**
 * Represents a Doctor user in the Online Medical Appointment system.
 * Stores doctor-specific information and provides doctor-related functionality.
 * Extends DatabaseAccessor and implements User interface.
 */
public class Doctor extends DatabaseAccessor implements User {

    /**
     * Unique identifier for the doctor.
     */
    private int doctorsID;

    /**
     * First name of the doctor.
     */
    private String FName;

    /**
     * Last name of the doctor.
     */
    private String LName;

    /**
     * Username chosen by the doctor.
     */
    private String username;

    /**
     * Password for the doctor's account.
     */
    private String password;

    /**
     * Type of user (should be "Doctor").
     */
    private String userType = "Doctor"; 

    /**
     * Doctor's specialty.
     */
    private String specialty;

    /**
     * Phone number of the doctor.
     */
    private String phoneNumber;

    /**
     * Constructs a Doctor with the specified details.
     * 
     * @param FName First name of the doctor
     * @param LName Last name of the doctor
     * @param username Username of the doctor
     * @param password Password of the doctor
     * @param userType Type of user (should be "Doctor")
     * @param specialty Doctor's specialty
     * @param phoneNumber Doctor's phone number
     */
    public Doctor(String FName, String LName, String username, String password, String userType, String specialty, String phoneNumber) {

    }    
    /**
     * Default constructor.
     */
    public Doctor() {
        // Default constructor
    }
    
    /**
     * Constructs a Doctor with the specified details including ID.
     * 
     * @param doctorsID Doctor's ID
     * @param FName First name
     * @param LName Last name
     * @param username Username
     * @param userType User type
     * @param specialty Specialty
     * @param phoneNumber Phone number
     */
    public Doctor(int userID, String FName, String LName, String username, String password, String userType, String specialty, String phoneNumber) {
        this.doctorsID = userID;
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.phoneNumber = phoneNumber;
        this.specialty = specialty;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getUserID() {
        return doctorsID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFName() {
        return FName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFName(String FName) {
        this.FName = FName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLName() {
        return LName;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setLName(String LName) {
        this.LName = LName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return username;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return password;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserType() {
        return userType;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * Gets the specialty of the doctor.
     * @return the specialty
     */
    @Override
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Sets the specialty of the doctor.
     * @param specialty the specialty
     */
    @Override
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    /**
     * Gets the phone number of the doctor.
     * @return the phone number
     */
    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the doctor.
     * @param phoneNumber the phone number
     */
    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Updates the user information in the database.
     * 
     * @param user The user to update
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateUser(User user) {
        
        return super.updateUser(user);
    }

    /**
     * Updates this doctor's information in the database.
     * 
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateUser() {
        return super.updateUser(this);
    }
    /**
     * Deletes the user from the database.
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteUser() {        
        return super.deleteUser(this.doctorsID);
    }
    
    /**
     * Returns a string representation of the Doctor object.
     * 
     * @return String representation of the doctor
     */
    @Override
    public String toString(){
        return "User{" +
                "userID=" + doctorsID +
                ", FName='" + FName + '\'' +
                ", LName='" + LName + '\'' +
                ", username='" + username + '\'' +
                ", userType='" + userType + '\'' +
                ", specialty='" + specialty + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
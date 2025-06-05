package com.example.OnlineMedicalAppointment.model;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

/**
 * Represents an Admin user in the Online Medical Appointment system.
 * Provides admin-specific functionality and user information.
 * Extends DatabaseAccessor and implements User interface.
 */
public class Admin extends DatabaseAccessor implements User {
    private int adminID;
    private String FName;
    private String LName;
    private String username;
    private String password;
    private String userType;
    private String specialty;
    private String phoneNumber;

    /**
     * Constructs an Admin with all properties.
     * @param FName first name
     * @param LName last name
     * @param username username
     * @param password password
     * @param userType user type
     * @param specialty specialty
     * @param phoneNumber phone number
     */
    public Admin( String FName, String LName, String username, String password, String userType, String specialty, String phoneNumber) {
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.specialty = specialty;
        this.phoneNumber = phoneNumber;
    }
    

    /**
     * Constructs an Admin with all properties except userType and specialty.
     * @param adminID admin ID
     * @param FName first name
     * @param LName last name
     * @param username username
     * @param password password
     * @param phoneNumber phone number
     */
    public Admin(int adminID, String FName, String LName, String username, String password, String phoneNumber) {
        this.adminID = adminID;
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.userType = "Admin"; // Default userType for Admin
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Constructs an Admin with all properties except password, userType, and specialty.
     * @param adminID admin ID
     * @param FName first name
     * @param LName last name
     * @param username username
     * @param phoneNumber phone number
     */
    public Admin(int adminID, String FName, String LName, String username, String phoneNumber) {
        this.adminID = adminID;
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.userType = "Admin"; // Default userType for Admin
        this.phoneNumber = phoneNumber;
    }

    /**
     * Default constructor.
     */
    public Admin() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public int getUserID() {
        return adminID;
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
     * {@inheritDoc}
     */
    @Override
    public String getSpecialty() {
        return specialty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Deletes the user from the database.
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteUser() {        
        return super.deleteUser(this.adminID);
    }
    /**
     * Updates the user information in the database.
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateUser() {
        return super.updateUser(this);
    }
    /**
     * Updates the user information in the database.
     * @param user The user to update
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateUser(User user) {
        
        return super.updateUser(user);
    }
}
package com.example.OnlineMedicalAppointment.model;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

public class Admin extends DatabaseAccessor implements User {
    private int adminID;
    private String FName;
    private String LName;
    private String username;
    private String password;
    private String userType = "Admin";
    private String specialty;
    private String phoneNumber;

    // Constructor with all properties
    public Admin( String FName, String LName, String username, String password, String userType, String specialty, String phoneNumber) {
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.specialty = specialty;
        this.phoneNumber = phoneNumber;
    }
    

    public Admin(int adminID, String FName, String LName, String username, String password, String phoneNumber) {
        this.adminID = adminID;
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    // Constructor with all properties but not password, no userType or specialty
    public Admin(int adminID, String FName, String LName, String username, String phoneNumber) {
        this.adminID = adminID;
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    // Default constructor
    public Admin() {}
    @Override
    public int getUserID() {
        return adminID;
    }
    @Override
    public String getFName() {
        return FName;
    }
    @Override
    public void setFName(String FName) {
        this.FName = FName;
    }
    @Override
    public String getLName() {
        return LName;
    }

    @Override
    public void setLName(String LName) {
        this.LName = LName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUserType() {
        return userType;
    }

    @Override
    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String getSpecialty() {
        return specialty;
    }

    @Override
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
package com.example.OnlineMedicalAppointment.model;



import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

public class Doctor extends DatabaseAccessor implements User {

    private int doctorsID;
    private String FName;
    private String LName;
    private String username;
    private String password;
    private String userType = "Doctor"; 
    private String specialty;
    private String phoneNumber;

    public Doctor(String FName, String LName, String username, String password, String userType, String specialty, String phoneNumber) {

        }    
    public Doctor() {
        // Default constructor
    }
    
    public Doctor(int doctorsID, String FName, String LName,  String username, String userType, String specialty, String phoneNumber) {
        this.doctorsID = doctorsID;
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.userType = userType;
        this.specialty = specialty;
        this.phoneNumber = phoneNumber;    
    }

    @Override
    public int getUserID() {
        return doctorsID;
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
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUserType() {
        return userType;
    }
    @Override
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
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
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
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

    

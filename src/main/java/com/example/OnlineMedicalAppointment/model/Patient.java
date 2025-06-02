package com.example.OnlineMedicalAppointment.model;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

public class Patient extends  DatabaseAccessor implements User{

    /**
     * Constructor for Patient user.
     * 
     * @param userID    the user ID
     * @param FName     the first name
     * @param LName     the last name
     * @param username  the username
     * @param password  the password
     * @param userType  the user type (always "Patient")
     * @param phoneNumber the phone number
     */
    private int userID;
    private String FName;
    private String LName;
    private String username;
    private String password;
    private String userType = "Patient"; 
    private String specialty= null; // Not applicable for Patient, but included for interface compliance
    private String phoneNumber;


    public Patient(int userID, String FName, String LName, String username, String password, String phoneNumber) {
        this.userID = userID;
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;    
    }

    public Patient(int userID, String FName, String LName, String username, String phoneNumber) {
        this.userID = userID;
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.phoneNumber = phoneNumber;    
    }
    public Patient( String FName, String LName, String username, String password, String phoneNumber) {
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;    
    }
    public Patient() {}

    @Override
    public int getUserID(){
        return userID;
    }

    @Override
    public String getFName(){
        return FName;
    }
   
    @Override
    public void setFName(String FName){
        this.FName = FName;
    }
   
    @Override
    public String getLName(){
        return LName;
    }
   
    @Override
    public void setLName(String LName){
        this.LName = LName;
    }
   
    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public void setUsername(String username){
        this.username = username;
    }

    @Override
    public void setPassword(String password){
        this.password = password;
    }
     
    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUserType(){
        return userType;
    }
   
    @Override
    public void setUserType(String userType){
        this.userType = userType;
    };
   
    @Override
    public String getSpecialty(){
        return specialty;
    };

    @Override
    public void setSpecialty(String specialty){
        this.specialty = specialty;
    };

    @Override
    public String getPhoneNumber(){
            return phoneNumber;
        }

    @Override
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    
    public void update() {
        super.updateUser(this);    
    }

    
    public void deleteUser() {        
        super.deleteUser(this.userID);
    }        
}

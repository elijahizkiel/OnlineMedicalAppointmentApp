package com.example.OnlineMedicalAppointment.model;

import com.example.OnlineMedicalAppointment.database.DatabaseAccessor;

/**
 * Represents a Patient user in the Online Medical Appointment system.
 * Extends DatabaseAccessor and implements User interface.
 */
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


    /**
     * Constructs a Patient with all details.
     * @param userID the user ID
     * @param FName the first name
     * @param LName the last name
     * @param username the username
     * @param password the password
     * @param phoneNumber the phone number
     */
    public Patient(int userID, String FName, String LName, String username, String password, String phoneNumber) {
        this.userID = userID;
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;    
    }

    /**
     * Constructs a Patient without password.
     * @param userID the user ID
     * @param FName the first name
     * @param LName the last name
     * @param username the username
     * @param phoneNumber the phone number
     */
    public Patient(int userID, String FName, String LName, String username, String phoneNumber) {
        this.userID = userID;
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.phoneNumber = phoneNumber;    
    }

    /**
     * Constructs a Patient without userID.
     * @param FName the first name
     * @param LName the last name
     * @param username the username
     * @param password the password
     * @param phoneNumber the phone number
     */
    public Patient( String FName, String LName, String username, String password, String phoneNumber) {
        this.FName = FName;
        this.LName = LName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;    
    }

    /**
     * Default constructor.
     */
    public Patient() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public int getUserID(){
        return userID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFName(){
        return FName;
    }
   
    /**
     * {@inheritDoc}
     */
    @Override
    public void setFName(String FName){
        this.FName = FName;
    }
   
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLName(){
        return LName;
    }
   
    /**
     * {@inheritDoc}
     */
    @Override
    public void setLName(String LName){
        this.LName = LName;
    }
   
    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername(){
        return username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPassword(String password){
        this.password = password;
    }
     
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword(){
        return password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserType(){
        return userType;
    }
   
    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserType(String userType){
        this.userType = userType;
    };
   
    /**
     * {@inheritDoc}
     */
    @Override
    public String getSpecialty(){
        return specialty;
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSpecialty(String specialty){
        this.specialty = specialty;
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPhoneNumber(){
            return phoneNumber;
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Updates this patient's information in the database.
     */
    public void update() {
        super.updateUser(this);    
    }

    /**
     * Deletes this patient from the database.
     */
    public boolean deleteUser() {        
        return super.deleteUser(this.userID);
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
    public boolean updateUser() {
        return super.updateUser(this);
    }
}

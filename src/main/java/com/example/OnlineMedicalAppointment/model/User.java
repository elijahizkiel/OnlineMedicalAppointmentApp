package com.example.OnlineMedicalAppointment.model;

public interface User {
    int getUserID();

    String getFName();

    void setFName(String FName);

    String getLName();

    void setLName(String LName);

    String getUsername();

    void setUsername(String username);

    void setPassword(String password);
    String getPassword();
    String getUserType();

    void setUserType(String userType);

    String getSpecialty();

    void setSpecialty(String specialty);

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);
}

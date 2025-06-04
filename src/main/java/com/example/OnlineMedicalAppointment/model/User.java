package com.example.OnlineMedicalAppointment.model;

/**
 * Interface representing a user in the Online Medical Appointment system.
 */
public interface User {
    /**
     * Gets the user ID.
     * @return the user ID
     */
    int getUserID();

    /**
     * Gets the first name.
     * @return the first name
     */
    String getFName();

    /**
     * Sets the first name.
     * @param FName the first name
     */
    void setFName(String FName);

    /**
     * Gets the last name.
     * @return the last name
     */
    String getLName();

    /**
     * Sets the last name.
     * @param LName the last name
     */
    void setLName(String LName);

    /**
     * Gets the username.
     * @return the username
     */
    String getUsername();

    /**
     * Sets the username.
     * @param username the username
     */
    void setUsername(String username);

    /**
     * Sets the password.
     * @param password the password
     */
    void setPassword(String password);

    /**
     * Gets the password.
     * @return the password
     */
    String getPassword();

    /**
     * Gets the user type.
     * @return the user type
     */
    String getUserType();

    /**
     * Sets the user type.
     * @param userType the user type
     */
    void setUserType(String userType);

    /**
     * Gets the specialty (for doctors).
     * @return the specialty
     */
    String getSpecialty();

    /**
     * Sets the specialty (for doctors).
     * @param specialty the specialty
     */
    void setSpecialty(String specialty);

    /**
     * Gets the phone number.
     * @return the phone number
     */
    String getPhoneNumber();

    /**
     * Sets the phone number.
     * @param phoneNumber the phone number
     */
    void setPhoneNumber(String phoneNumber);

    boolean updateUser();
    boolean deleteUser();

}

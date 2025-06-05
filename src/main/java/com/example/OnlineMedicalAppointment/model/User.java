package com.example.OnlineMedicalAppointment.model;

/**
 * User interface for the Online Medical Appointment system.
 * Defines common user-related methods for all user types.
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
     * Gets the password.
     * @return the password
     */
    String getPassword();

    /**
     * Sets the password.
     * @param password the password
     */
    void setPassword(String password);

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
     * Gets the specialty (if applicable).
     * @return the specialty
     */
    String getSpecialty();

    /**
     * Sets the specialty (if applicable).
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

    /**
     * Updates the user information in the database.
     * @return true if update was successful, false otherwise
     */
    boolean updateUser();

    /**
     * Deletes the user from the database.
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteUser();

    /**
     * Updates the user information in the database.
     * @param user The user to update
     * @return true if update was successful, false otherwise
     */
    boolean updateUser(User user);
}

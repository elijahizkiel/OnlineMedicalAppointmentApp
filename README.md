# Online Medical Appointment Application

## Project Description

The Online Medical Appointment Application is a desktop application built using Java and SQLite. It facilitates the process of booking and managing medical appointments between patients and doctors. The application provides different interfaces based on the user type (Patient, Doctor, Admin) and includes features for user authentication, appointment management, and an AI-powered chat assistant.

## Features

*   **User Authentication:** Secure signup and login for different user types (Patient, Doctor, Admin).
*   **Role-Based Dashboards:** Customized views and functionalities for Patients, Doctors, and Admins.
    *   **Patient Dashboard:** View doctor lists, book appointments, access chat-room, manage profile.
    *   **Doctor Dashboard:** View scheduled appointments, manage availability/schedule, manage profile.
    *   **Admin Dashboard:** View application statistics (appointments, user count), manage user lists.
*   **Appointment Booking:** Patients can search for doctors by name or specialty and book appointments based on doctor availability. Includes calendar view and availability checks.
*   **Scheduling:** Doctors can define and manage their available time slots.
*   **Chat-Room:** An integrated chat feature where users can interact with an AI bot (planned integration with Google Gemini) for health-related queries and schedule information.
*   **User Profile Management:** Users can view and edit their profile information (name, phone number) and change their password.
*   **Logout Functionality:** Securely log out of the application.
*   **SQLite Database:** Simple, file-based database for storing user information, messages, and schedules.

## Benefits

*   **Convenience:** Provides an easy-to-use platform for patients to find and book appointments and for doctors to manage their schedules.
*   **Organization:** Centralizes appointment and user data, improving management efficiency.
*   **Accessibility:** Desktop application provides a dedicated interface for users.
*   **AI Assistance:** The planned AI chat feature can offer quick answers to health questions and provide information based on available data.

## Technologies Used

*   **Java:** The core programming language for the application logic and UI.
*   **Swing / JavaFX (UI Library):** Used for building the graphical user interface. (Note: The current implementation uses Swing placeholders).
*   **SQLite:** A lightweight, file-based database used for data storage.
*   **SQLite JDBC Driver:** Java Database Connectivity driver for interacting with the SQLite database.
*   **Maven:** Build automation tool for dependency management and project build lifecycle.
*   **Google Gemini API (Planned):** For the AI chatbot functionality.

## Getting Started

1.  **Clone the repository:** (Assuming you are using Git)
    ```bash
    git clone <repository_url>
    cd OnlineMedicalAppointment
    ```
2.  **Build the project:** Use Maven to build the project and download dependencies.
    ```bash
    mvn clean install
    ```
3.  **Run the application:**
    ```bash
    mvn exec:java
    ```
    Alternatively, you can run the `Main` class directly from your IDE after building.

## Database

The application uses an SQLite database named `medical_appointment.db`. The database schema includes tables for `users_table`, `Messages`, and `Schedules`. The `DatabaseInitializer` class ensures these tables are created when the application starts if they don't already exist.

## Future Enhancements

*   Full implementation of Patient Booking, Doctor Scheduling, Chat-Room, and Admin Dashboard features.
*   Integration with Google Gemini API.
*   Improved UI design and user experience.
*   Enhanced security (e.g., password hashing).
*   More robust error handling and input validation.
*   Advanced search and filtering options.
*   Appointment reminders and notifications.

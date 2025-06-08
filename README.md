# Online Medical Appointment Application

## Project Description

The Online Medical Appointment Application is a modern Java Swing desktop application with an SQLite backend. It streamlines booking and managing medical appointments between patients and doctors, and provides role-based dashboards for Patients, Doctors, and Admins. The application features user authentication, appointment management, real-time chat, and an AI-powered assistant (Gemini).

## Features

- **User Authentication:** Secure signup and login for Patients, Doctors, and Admins.
- **Role-Based Dashboards:**
  - **Patient Dashboard:** View doctors, book appointments, chat with doctors, access AI assistant, manage profile.
  - **Doctor Dashboard:** View schedule, manage appointments, chat with patients, access AI assistant, manage profile.
  - **Admin Dashboard:** View application statistics, manage users, monitor system activity.
- **Appointment Booking:** Patients can search for doctors by name or specialty and book appointments based on real-time availability. Includes calendar view and availability checks.
- **Doctor Scheduling:** Doctors can set and manage their available time slots and view upcoming appointments.
- **Chat-Room:** Real-time chat between patients and doctors, with searchable user list and dynamic chat room creation.
- **AI Assistant:** Integrated Gemini AI assistant for medical queries and support.
- **User Profile Management:** Edit personal information and change password.
- **Accessibility:** Modern, accessible UI with scrollable panels and improved contrast.
- **Admin Tools:** User management, system activity monitoring, and graphical statistics.

## Technology Stack
- **Java 17+** (Swing for UI)
- **SQLite** (JDBC for database access)
- **Maven** (dependency management)
- **JUnit 5** (unit testing)
- **Google Gemini SDK** (AI assistant integration)

## Project Structure

```
OnlineMedicalAppointment/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   │               └── OnlineMedicalAppointment/
│   │                   ├── model/
│   │                   ├── ui/
│   │                   └── database/
│   └── test/
│       └── ... (tests)
├── pom.xml
├── online_medical_appointment.db
├── README.md
├── DOCUMENTATION.md
├── Project detail.md
└── ...
```

## How to Run

1. Ensure you have Java 17+ and Maven installed.
2. Clone the repository and navigate to the project root.
3. Run `mvn clean install` to build the project.
4. Run the application using your IDE or with `mvn exec:java -Dexec.mainClass="com.example.OnlineMedicalAppointment.Main"`.

## Testing
- Unit tests are located in `src/test/java/com/example/OnlineMedicalAppointment/model/` and `ui/`.
- Run tests with `mvn test`.

## Documentation
- See `DOCUMENTATION.md` and `Project detail.md` for detailed class and architecture documentation, including a UML diagram.

## Status
- All major features implemented and tested.
- UI and accessibility improvements complete.
- Gemini AI assistant integrated.
- Real-time chat and dynamic chat room creation enabled.
- Project is ready for further enhancements and deployment.

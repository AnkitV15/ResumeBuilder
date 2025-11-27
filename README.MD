# Resume Builder API

This project provides a RESTful API for building and managing resumes. It allows users to create, retrieve, update, and delete resume data, including personal information, education, work experience, skills, and projects.

## Features

* **User Authentication:** Secure user registration and login.
* **Resume Management:** Create, read, update, and delete resumes.
* **Section Management:** Add, update, and remove various resume sections (e.g., education, experience, skills).
* **Data Validation:** Ensures data integrity and consistency.
* **Scalable Architecture:** Designed for easy expansion and maintenance.

## Technologies Used

* **Backend:** Java, Spring boot
* **Database:** Mongodb
* **Authentication:** JWT (JSON Web Tokens)
* **Payment Provide:** Razorpay
* **Deployment:** Docker (planned)

## Getting Started

### Prerequisites

* Java Development Kit (JDK) 17 or higher
* Maven 3.6+
* MongoDB instance (local or cloud-based)

### Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/AnkitV15/ResumeBuilder.git
    cd resume-builder-api
    ```

2. **Build the project:**

    ```bash
    mvn clean install
    ```

3. **Configuration**

   This application is configured using environment variables, which are defined in `src/main/resources/application.yaml`. You must set these variables in your terminal session before launching the application.

   #### Required Variables
   *   `MONGODB_URI`: The full connection string for your MongoDB database.
   *   `JWT_SECRET`: A strong secret key for signing JWT tokens.
   *   `RAZORPAY_KEY_ID`: Your Razorpay API Key ID.
   *   `RAZORPAY_KEY_SECRET`: Your Razorpay API Key Secret.
   *   `MAIL_HOST`: The hostname of your SMTP mail server.
   *   `MAIL_PORT`: The port of your SMTP mail server.
   *   `MAIL_USERNAME`: The username for the email server.
   *   `MAIL_PASSWORD`: The password for the email server.
   *   `MAIL_FROM`: The email address that messages will be sent from.
   *   `CLOUDINARY_CLOUD_NAME`: Your Cloudinary cloud name.
   *   `CLOUDINARY_API_KEY`: Your Cloudinary API key.
   *   `CLOUDINARY_API_SECRET`: Your Cloudinary API secret.


   #### How to Set Variables

   Below are examples of how to set the variables in different shells.

   **PowerShell (Windows)**
   ```powershell
   $env:MONGODB_URI = "mongodb://localhost:27017/resumebuilder"
   $env:JWT_SECRET = "your-super-secret-key"
   # ... set other variables
   ```

   **Command Prompt (Windows)**
   ```cmd
   set MONGODB_URI="mongodb://localhost:27017/resumebuilder"
   set JWT_SECRET="your-super-secret-key"
   # ... set other variables
   ```

   **Bash (Linux/macOS)**
   ```bash
   export MONGODB_URI="mongodb://localhost:27017/resumebuilder"
   export JWT_SECRET="your-super-secret-key"
   # ... set other variables
   ```

### Running the Application

```bash
java -jar target/resumebuilderapi-0.0.1-SNAPSHOT.jar
```

Alternatively, you can run it directly from your IDE (e.g., IntelliJ IDEA, Eclipse) as a Spring Boot application.

## API Endpoints

(This section will be updated with details about the API endpoints as they are developed.)
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

   This application is configured using environment variables. You must set these variables in your terminal session before launching the application.

   #### Required Variables

   *   `MONGODB_URI`: The full connection string for your MongoDB database.
   *   `MAIL_USERNAME`: The username for the email (SMTP) server.
   *   `MAIL_PASSWORD`: The password for the email (SMTP) server.
   *   `JWT_SECRET`: A strong secret key for signing JWT tokens.
   *   `RAZORPAY_KEY_ID`: Your Razorpay API Key ID.
   *   `RAZORPAY_KEY_SECRET`: Your Razorpay API Key Secret.

   #### How to Set Variables

   Below are examples of how to set the variables in different shells.

   **PowerShell (Windows)**
   ```powershell
   $env:MONGODB_URI = "mongodb://localhost:27017/resumebuilder"
   $env:MAIL_USERNAME = "your-mail-username"
   $env:MAIL_PASSWORD = "your-mail-password"
   $env:JWT_SECRET = "your-super-secret-key"
   $env:RAZORPAY_KEY_ID = "your_razorpay_key_id"
   $env:RAZORPAY_KEY_SECRET = "your_razorpay_key_secret"
   ```

   **Command Prompt (Windows)**
   ```cmd
   set MONGODB_URI="mongodb://localhost:27017/resumebuilder"
   set MAIL_USERNAME="your-mail-username"
   set MAIL_PASSWORD="your-mail-password"
   set JWT_SECRET="your-super-secret-key"
   set RAZORPAY_KEY_ID="your_razorpay_key_id"
   set RAZORPAY_KEY_SECRET="your_razorpay_key_secret"
   ```

   **Bash (Linux/macOS)**
   ```bash
   export MONGODB_URI="mongodb://localhost:27017/resumebuilder"
   export MAIL_USERNAME="your-mail-username"
   export MAIL_PASSWORD="your-mail-password"
   export JWT_SECRET="your-super-secret-key"
   export RAZORPAY_KEY_ID="your_razorpay_key_id"
   export RAZORPAY_KEY_SECRET="your_razorpay_key_secret"
   ```

### Running the Application

```bash
java -jar target/resumebuilderapi-0.0.1-SNAPSHOT.jar
```

Alternatively, you can run it directly from your IDE (e.g., IntelliJ IDEA, Eclipse) as a Spring Boot application.

## API Endpoints

(This section will be updated with details about the API endpoints as they are developed.)

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
    git clone https://github.com/your-username/resumebuilderapi.git
    cd resume-builder-api
    ```

2. **Build the project:**

    ```bash
    mvn clean install
    ```

3. **Configure environment variables:**

    Create an `application.properties` file (or `application.yml`) in `src/main/resources` and add the following:

    ```bash
    # MongoDB Configuration
    spring.data.mongodb.uri=mongodb://localhost:27017/resumebuilder

    # JWT Secret Key
    jwt.secret=your_super_secret_key

    # Razorpay Configuration
    razorpay.key_id=your_razorpay_key_id
    razorpay.key_secret=your_razorpay_key_secret
    ```

    Replace the placeholder values with your actual MongoDB connection string, a strong JWT secret key, and your Razorpay API credentials.

### Running the Application

```bash
java -jar target/resumebuilderapi-0.0.1-SNAPSHOT.jar
```

Alternatively, you can run it directly from your IDE (e.g., IntelliJ IDEA, Eclipse) as a Spring Boot application.

## API Endpoints

(This section will be updated with details about the API endpoints as they are developed.)

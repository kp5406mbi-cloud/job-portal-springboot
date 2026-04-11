# 🚀 Job Portal Backend Application

A scalable backend system built using **Spring Boot** and **PostgreSQL** to support job posting and application workflows with secure authentication and efficient data handling.

---

## 🌐 Live Demo

👉 https://job-portal-springboot-sek1.onrender.com

> ⚠️ Note: The application is hosted on Render free tier and may take ~30–60 seconds to load after inactivity due to cold start.

---

## 📂 GitHub Repository

👉 https://github.com/kp5406mbi-cloud/job-portal-springboot

---

## 🛠️ Tech Stack

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate

### Database

* PostgreSQL

### Tools & Deployment

* Git
* Postman
* Render

---

## ⚙️ Features

* 🔐 **Authentication & Authorization**

  * Role-based access control (USER / RECRUITER)
  * Secure endpoint-level authorization using Spring Security

* 📄 **Job Management**

  * Create, update, delete job postings
  * Pagination, sorting, and filtering of job listings

* 📬 **Application System**

  * Apply to jobs
  * Track application status

* 🧠 **Exception Handling**

  * Centralized error handling using `@ControllerAdvice`
  * Standardized HTTP responses (400, 401, 404, 409, 500)

* 🛡️ **Data Integrity**

  * Unique constraints on user email
  * Validation for user registration

* ⚡ **Optimized Database Access**

  * Efficient queries using JPA/Hibernate
  * Reduced redundant database calls

* 🏗️ **Architecture**

  * MVC-based layered architecture (Controller → Service → Repository)

---

## 📁 Project Structure

```
src/
 ├── controller/
 ├── service/
 ├── repository/
 ├── entity/
 ├── dto/
 ├── exception/
 └── resources/
```

---

---

## 🚀 API Endpoints

### Base URL


---

### 🔹 Public APIs

| Method | Endpoint        | Description            |
|--------|---------------|------------------------|
| GET    | /api/jobs     | Fetch all jobs (paginated) |
| GET    | /api/jobs/{id} | Get job by ID         |

---

### 🔹 Authentication APIs

| Method | Endpoint        | Description        |
|--------|---------------|--------------------|
| POST   | /register     | Register new user  |
| POST   | /login        | Login user         |

---

### 🔹 Protected APIs (Role-Based)

| Method | Endpoint          | Role Required |
|--------|------------------|--------------|
| POST   | /api/jobs        | RECRUITER    |
| POST   | /api/apply/{id}  | USER         |

---

### 📌 Example Request

```http
GET /api/jobs

{
  "content": [
    {
      "id": 1,
      "title": "Software Engineer",
      "company": "Meta",
      "location": "Hyderabad",
      "salary": 150000
    }
  ],
  "totalPages": 5,
  "totalElements": 50
}



## ⚙️ Environment Variables

Set the following variables in your deployment platform:

```
SPRING_DATASOURCE_URL=your_db_url
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

---

## ▶️ Running Locally

### 1. Clone the repository

```
git clone https://github.com/kp5406mbi-cloud/job-portal-springboot.git
cd job-portal-springboot
```

### 2. Configure database

Update `application.properties` with your PostgreSQL credentials.

### 3. Run the application

```
./mvnw spring-boot:run
```

### 4. Access

```
http://localhost:8080
```

---

## 📌 Key Highlights

* Built scalable backend using Spring Boot & PostgreSQL
* Implemented secure role-based access control
* Designed REST APIs with pagination & filtering
* Centralized exception handling for clean error responses
* Optimized database interactions using JPA/Hibernate
* Deployed on Render with environment-based configuration

---

## 📈 Future Improvements

* JWT-based authentication
* Docker containerization
* Microservices architecture
* Caching with Redis

---

## 👨‍💻 Author

**Kumar Piyush**
📧 [kp5406.mbi@gmail.com](mailto:kp5406.mbi@gmail.com)
🔗 https://github.com/kp5406mbi-cloud

---

## ⭐ If you found this project useful, consider giving it a star!


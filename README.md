# PremierStudioApi

## 📌 Project Overview
PremierStudioApi is a backend system for managing events, allowing users to create, update, delete, and register for events. The system includes user authentication with JWT and supports pagination and searching for events.

---

## 🚀 Features
### 🎟️ Event Management
- **POST** `/api/events` → Create a new event.
- **PUT** `/api/events/:id` → Update event details by ID.
- **DELETE** `/api/events/:id` → Delete an event by ID.
- **GET** `/api/events` → Fetch all events (supports pagination).
- **GET** `/api/events/:id` → Fetch details of a single event by ID.
- **GET** `/api/events/:id/attendees` → Fetch attendees for a specific event.
- **GET** `/api/events/search` → Search for events by name, date, or location.

### 👤 User Management
- **POST** `/api/users/register` → Register a new user.

### 📝 Event Registration
- **POST** `/api/events/:id/register` → Register a user for an event by event ID.

### 🔐 Authentication
- **POST** `/api/auth/login` → Authenticate a user and return a JWT token.
- **POST** `/api/auth/logout` → Log out the authenticated user.

---

## 🛠️ Installation & Setup

### Prerequisites
Ensure you have the following installed:
- Java 17+
- Spring Boot
- Gradle
- MySQL (running on port `3306`)

### Configuration
- Update `application.properties` or `application.yml` with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/premierstudio
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```
- Change the JWT secret key in `application.properties`:
```properties
jwt.secret=your_secret_key_here
```

### Install Dependencies & Run the Application
```sh
git clone https://github.com/yourusername/PremierStudioApi.git
cd PremierStudioApi
gradle build
gradle bootRun
```

---

## 📄 API Documentation
API documentation is available via **Swagger**.

After running the application, visit:
- 📌 **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- 📌 **OpenAPI Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 📦 Project Structure
```
PremierStudioApi/
│── src/
│   ├── main/
│   │   ├── java/com/premierstudio/
│   │   │   ├── controllers/
│   │   │   ├── models/
│   │   │   ├── repositories/
│   │   │   ├── security/
│   │   │   ├── services/
│   │   │   ├── config/
│   ├── resources/
│   │   ├── application.properties
│── build.gradle
│── README.md
```



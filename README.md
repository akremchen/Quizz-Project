# Quizz Project

A distributed quiz platform built with Spring Boot, React, PostgreSQL, Kafka, Eureka, Spring Cloud Config, and Docker.

The platform allows users to create and play quizzes, earn points and badges, follow favorite categories, receive notifications, and unlock premium quizzes.

## Features

### User Management

- User registration and login
- JWT-based authentication
- Role-based authorization for users and administrators
- Profile and password management
- Favorite quiz categories

### Quiz Management

- Create, update, delete, and publish quizzes
- Administrator-only premium quiz creation
- Play published quizzes
- Submit answers and calculate results
- Store user attempt history
- Unlock premium quizzes using achievement points

### Achievements

- Award points after quiz completion
- Deduct points when unlocking premium quizzes
- Automatically award badges
- Display each user's points and earned badges

### Notifications

- Notify interested users when a quiz is published in one of their favorite categories
- Notify users when points or badges are earned
- Display unread notifications
- Mark notifications as read

### Infrastructure

- Eureka service discovery
- Spring Cloud Config Server
- Nginx API Gateway
- Kafka-based asynchronous communication
- Database per service
- Docker Compose orchestration

## Architecture

| Component | Responsibility |
|---|---|
| API Gateway | Routes frontend requests to the appropriate microservice |
| Discovery Service | Registers and discovers service instances using Eureka |
| Config Service | Provides centralized configuration |
| User Service | Authentication, users, profiles, and favorite categories |
| Quiz Service | Quiz management, submissions, attempts, and premium unlocking |
| Achievement Service | Points, badges, and premium-point deductions |
| Notification Service | User notifications generated from Kafka events |
| Kafka | Asynchronous communication between services |
| React Frontend | User interface for all platform features |

## Service Communication

Synchronous communication uses REST and OpenFeign with Eureka service names.

Asynchronous communication uses Kafka:

| Topic | Producer | Consumer |
|---|---|---|
| `quiz-completed` | Quiz Service | Achievement Service |
| `quiz-published` | Quiz Service | Notification Service |
| `points-earned` | Achievement Service | Notification Service |
| `badge-earned` | Achievement Service | Notification Service |

## Ports

| Service | Port |
|---|---:|
| React Frontend | 5173 |
| API Gateway | 8080 |
| Quiz Service | 8082 |
| Achievement Service | 8083 |
| Notification Service | 8084 |
| User Service | 8085 |
| Discovery Service | 8761 |
| Config Service | 8888 |
| Kafka | 9092 |
| Quiz PostgreSQL | 5433 |
| Achievement PostgreSQL | 5434 |
| Notification PostgreSQL | 5435 |
| User PostgreSQL | 5436 |

## Technology Stack

### Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Cloud Netflix Eureka
- Spring Cloud Config
- Spring Cloud OpenFeign
- Apache Kafka
- PostgreSQL
- JWT authentication
- Gradle Kotlin DSL

### Frontend

- React
- Vite
- Axios
- Lucide React

### Infrastructure

- Docker
- Docker Compose
- Nginx

## Running the Project

### Prerequisites

Install:

- Docker Desktop
- Node.js and npm
- Git

### Start the Backend

From the project root:

```bash
docker compose up -d --build
```

Check the containers:

```bash
docker compose ps
```

The Eureka dashboard is available at:

```text
http://localhost:8761
```

The Config Server can be tested with:

```text
http://localhost:8888/application/default
```

The API Gateway is available at:

```text
http://localhost:8080
```

### Start the Frontend

Navigate to the frontend directory:

```bash
cd quiz-ui
npm install
npm run dev
```

Open:

```text
http://localhost:5173
```

The frontend sends its API requests through:

```text
http://localhost:8080/api
```

## Useful Docker Commands

Start all containers:

```bash
docker compose up -d
```

Rebuild a specific service:

```bash
docker compose up -d --build --force-recreate --no-deps <service-name>
```

Restart the Nginx gateway after recreating an upstream service:

```bash
docker compose restart gateway
```

View logs:

```bash
docker compose logs -f <service-name>
```

Stop the platform:

```bash
docker compose down
```

## Security

The User Service generates JWTs containing:

- User ID
- Username
- Email
- Role

The frontend sends the token with protected requests:

```http
Authorization: Bearer <token>
```

All protected microservices validate the JWT using the same configured secret. User-specific endpoints derive the current user from the token rather than accepting a user ID from the frontend.

## Configuration

Centralized service configurations are stored in:

```text
config-repository/
```

The business services load their configuration from Config Service during startup.

Environment-specific values such as database URLs, Kafka addresses, and the JWT secret can be provided through Docker environment variables.

## Current Status

The complete platform is integrated and functional:

- All four business microservices are running
- Services register successfully with Eureka
- Centralized configuration is provided by Config Server
- Requests are routed through the Nginx gateway
- JWT authentication and authorization are active
- Kafka event processing works across services
- Quiz publishing notifications work
- Points and badges are awarded after quiz completion
- Premium quiz unlocking deducts points
- Attempt history, favorites, notifications, and profile management are available
- The React frontend is connected to all backend services

## Team Members

- Akrem Cheniour — 564828
- Enkhjin Erkhembayar — 597263
- Raneem Alboush — 593973
- Vasilena Kapincheva — 589678

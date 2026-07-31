
# Quizz Project

Distributed quiz platform built with Spring Boot, REST APIs, Kafka, and microservices.

## Services

- Discovery Service
- API Gateway
- User Service
- Quiz Service
- Achievement Service
- Notification Service

## Planned Architecture

- Spring Boot microservices
- REST communication through API Gateway
- Kafka for asynchronous events
- Database per service
- Eureka for service discovery
- Config Server for centralized configuration

## Ports

| Service | Port |
|---|---:|
| API Gateway | 8080 |
| Discovery Service | 8761 |
| Config Service | 8888 |
| User Service | 8085 |
| Quiz Service | 8082 |
| Achievement Service | 8083 |
| Notification Service | 8084 |
| Kafka | 9092 |

## Team Members
- Akrem Cheniour (564828)
- Enkhjin Erkhembayar (597263)
- Raneem Alboush (593973)
- Vasilena Kapincheva (589678)


## Current Status
We have all 4 microservices (`quiz-service`, `notification-service`, `achievement-service`, and `user-service`) are up, running, and working perfectly together behind the Nginx gateway.

## In Progress / Next Steps
1. **User Service Logic:** Finalizing the actual business logic, REST controllers, and database interactions for user registration and authentication.
2. **Frontend Integration:** Connecting the React/Vite UI to the newly running `user-service` endpoints.
3. **Core Infrastructure:** Building and integrating the **Eureka Discovery Service** and **Config Server** to complete the planned microservice architecture.

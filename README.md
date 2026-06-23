
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
| User Service | 8081 |
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
We have 3 microservices (`quiz-service`, `notification-service` and `achievement-service`) are up, running, and working perfectly together behind the Nginx gateway.
**In Progress:** We are currently implementing the fourth service (`user-service`).

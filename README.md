# ✈️ AI Airline — Production-Grade Airline Management System

> A fintech-quality airline backend built with Java 17 and Spring Boot 3.2.
> Demonstrates enterprise patterns like JWT security, Redis caching, Resilience4j fault tolerance,
> dynamic pricing, a full loyalty programme, and predictive delay detection.

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red?style=flat-square&logo=redis)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=flat-square&logo=docker)](https://docs.docker.com/compose/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

---

## What this project demonstrates

This is not a tutorial project. Every architectural decision mirrors real production
systems in the financial services and airline industry.

| Pattern | Implementation |
|---|---|
| JWT authentication | HS256 access + refresh tokens, BCrypt password hashing |
| Redis caching | 7 named caches with individual TTLs (2 min → 1 hour) |
| Circuit breaker | Resilience4j — payment gateway + external flight data |
| Bulkhead isolation | BookingService capped at 50 concurrent calls |
| Retry with backoff | Exponential backoff on payment and flight endpoints |
| Redis rate limiting | Sliding-window limiter per user/IP per endpoint |
| Dynamic pricing | Occupancy surge (+20–35%), last-minute premium, DB rules |
| Event-driven design | Spring ApplicationEvent — async, non-blocking side-effects |
| Optimistic locking | @Version on all entities — prevents double-booking |
| Flyway migrations | Version-controlled schema, reproducible across environments |
| Testcontainers | Integration tests against real PostgreSQL 16 |
| Observability | Micrometer, Prometheus, Grafana, MDC request tracing |

---

## Tech stack

```
Java 17          Spring Boot 3.2.5     Spring Security 6
PostgreSQL 16    Redis 7               Flyway 10
Resilience4j 2   SpringDoc OpenAPI     Micrometer / Prometheus
Testcontainers   JUnit 5 / Mockito     Docker Compose
```

---

## Architecture — 12 feature branches

Each branch is a self-contained, deployable slice of the system.
Clone any branch to study a specific pattern in isolation.

```
main                              ← fully integrated build
├── feature-authentication        ← JWT, register, login, refresh
├── feature-flight-management     ← search, status, airports
├── feature-caching               ← Redis 7-cache config, TTLs
├── feature-rate-limiting         ← Redis sliding-window limiter
├── feature-dynamic-pricing-engine← occupancy surge, rule engine
├── feature-loyalty               ← miles earn/burn, tier upgrades
├── feature-booking-engine        ← lifecycle, events, check-in
├── feature-payment-processing    ← card, miles, miles+cash, FX
├── feature-baggage               ← check-in, tracking, fees
├── feature-predictive-delay-detection← rule-based ML simulation
├── feature-flight-recommendation-system← personalised scoring
└── feature-observability         ← AOP timing, MDC tracing
```

---

## Domain model

```
User ──────── Booking ──────── Flight ──────── Airport
  │               │                │
  │           Payment          Aircraft
  │               │
  │        BaggageItem
  │
MilesTransaction
FlightRecommendation
PricingRule
```

---

## Quickstart

### Prerequisites

- Java 17+
- Docker & Docker Compose
- Git

### Run with Docker Compose

```bash
git clone https://github.com/YOUR_USERNAME/skylink-airline.git
cd skylink-airline
docker-compose -f docker/docker-compose.yml up -d
```

The app starts at `http://localhost:8080/api`

| Service | URL |
|---|---|
| Swagger UI | http://localhost:8080/api/swagger-ui.html |
| Health check | http://localhost:8080/api/actuator/health |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 (admin / admin123) |

### Run locally

```bash
# Start infrastructure
docker-compose -f docker/docker-compose.yml up -d postgres redis

# Run the app
./gradlew bootRun
```

---

## API — quick tour

### Register + login

```http
POST /api/auth/register
Content-Type: application/json

{
  "firstName": "Vishnu",
  "lastName": "Reddy",
  "email": "vishnu@skylink.com",
  "password": "Password@123"
}
```

```http
POST /api/auth/login
Content-Type: application/json

{ "email": "vishnu@skylink.com", "password": "Password@123" }
```

### Search flights

```http
POST /api/flights/search
Content-Type: application/json
Authorization: Bearer {token}

{
  "origin": "JFK",
  "destination": "LAX",
  "departureDate": "2025-08-15",
  "cabinClass": "ECONOMY",
  "passengers": 1
}
```

### Create a booking

```http
POST /api/bookings
Authorization: Bearer {token}

{
  "flightId": "uuid-here",
  "cabinClass": "ECONOMY",
  "paymentMethod": "CREDIT_CARD",
  "milesToRedeem": 0
}
```

### Get dynamic price

```http
GET /api/pricing/flight/{flightId}?cabinClass=BUSINESS
Authorization: Bearer {token}
```

Response includes `appliedRules`, `occupancyPct`, and `priceTrend`.

---

## Seeded data

Flyway migrations seed the database on first run:

- 12 airports — JFK, LAX, ORD, DFW, ATL, LHR, CDG, DXB, SIN, HND, SYD, YYZ
- 6 aircraft — B737-800, A320-200, B777-300ER, A380-800, B787-9, A350-900
- 5 pricing rules — last-minute premium, early bird, occupancy surge, transatlantic
- 5 sample flights — SK101 through SK501
- Admin account — admin@skylink.com / Admin@123

---

## Testing

```bash
# Unit tests
./gradlew test

# Test coverage report (HTML)
./gradlew jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

Integration tests use Testcontainers and spin up a real PostgreSQL 16 instance.

---

## Project structure

```
ai-airline/
├── docker/
│   ├── Dockerfile            # Multi-stage build, non-root user
│   ├── docker-compose.yml    # App + Postgres + Redis + Prometheus + Grafana
│   └── prometheus.yml
└── src/main/java/com/skylink/airline/
    ├── config/               # Security, Redis, JPA, OpenAPI, FilterConfig
    ├── controller/           # 11 REST controllers
    ├── dto/                  # 16 request/response DTOs with validation
    ├── entity/               # 11 JPA entities + 9 enums
    ├── event/                # Domain events (BookingCreated, CheckIn, etc.)
    ├── exception/            # GlobalExceptionHandler + 6 custom exceptions
    ├── filter/               # Redis rate limiting filter
    ├── repository/           # 9 Spring Data JPA repositories
    ├── security/             # JWT provider + filter + UserDetailsService
    └── service/impl/         # 10 services with Resilience4j patterns
```

---

## Key design decisions

**Why @Modifying JPQL for seat decrement?**
Atomic DB-level decrement returns 0 if no seat is available, preventing
double-booking without distributed locks.

**Why @Bulkhead on BookingService?**
Caps concurrent booking requests at 50, preventing DB connection pool
exhaustion under load spikes — exactly how production airline systems work.

**Why @Async event listeners?**
Email/notification failures never roll back a booking transaction.
Side-effects are decoupled from the core booking path.

**Why Redis INCR for rate limiting?**
Atomic increment + TTL is the standard pattern for sliding-window rate limiting.
Works correctly across multiple app instances behind a load balancer.

---

## Background + motivation

This project was built to demonstrate the backend patterns-> The airline domain provides
a rich context for showing: loyalty programme mechanics, dynamic pricing,
fault-tolerant payment processing, and real-time seat availability — all
patterns that transfer directly to fintech.

---

## Author

**Vishnu**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=flat-square&logo=linkedin)](https://linkedin.com/in/YOUR_PROFILE)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black?style=flat-square&logo=github)](https://github.com/YOUR_USERNAME)

---

## License

MIT — use freely for learning and portfolio purposes.

# CryptoListing Platform

CryptoListing is a full-stack application that lets investors explore cryptocurrencies, filter the catalog, and manage their own listings. The backend is delivered with Spring Boot, while the frontend uses React and Vite. Authentication relies on JWT with role-based access.

## Project Structure

- `backend/` – Spring Boot REST API with Spring Security, JPA, JWT authentication, and validation.
- `frontend/` – React + TypeScript single-page application built with Vite and React Query.
- `Jenkinsfile` – Declarative pipeline to build, test, and prepare for quality analysis.
- `pom.xml` – Maven aggregator coordinating backend and frontend builds via the `frontend-maven-plugin`.

## Key Features

- Secure authentication & registration with BCrypt-hashed passwords and JWT issuance.
- CRUD operations on crypto assets with ownership enforcement.
- Filtering criteria (name, category, market cap range) backed by JPA Specifications.
- Global exception handling with consistent API error payloads.
- Seed data for quick demos (admin account + Bitcoin listing).
- React dashboard for browsing, filtering, and managing assets in real time.

## Running Locally

```bash
# From repository root
mvn -pl backend -am spring-boot:run

# In a second terminal for the frontend
cd frontend
npm install
npm run dev
```

Backend runs on `http://localhost:8080`, frontend on `http://localhost:5173` with a proxy for `/api` requests.

## Testing & Coverage

```bash
mvn -pl backend -am clean verify
```

- Unit tests cover domain services, JWT provider, and controllers (via MockMvc integration tests).
- JaCoCo enforces a 70% line coverage threshold during the Maven `verify` phase.

## Continuous Integration (Jenkins)

The included `Jenkinsfile` expects configured JDK 21 and Node.js 20 installations. Stages:

1. **Backend Tests** – `mvn clean verify` with unit + integration tests and JaCoCo coverage check.
2. **Frontend Install** – `npm install` executed in the `frontend` directory.
3. **Frontend Build** – `npm run build` to ensure the SPA compiles successfully.
4. **Quality Gate Placeholder** – hook for SonarQube or other quality checks.

JUnit test results are always archived from both Surefire and Failsafe reports.

## SonarQube & Code Quality

- SonarLint/SonarQube compliance ensured by meaningful naming, limited side effects, and clean architecture.
- To integrate SonarQube, configure scanner access and extend the Jenkins `Quality Gate` stage accordingly (e.g., run `mvn sonar:sonar` with proper credentials).

## Demo Accounts

- **Admin:** `admin` / `admin123!`

Register additional accounts via the `/api/auth/register` endpoint or the frontend registration page.

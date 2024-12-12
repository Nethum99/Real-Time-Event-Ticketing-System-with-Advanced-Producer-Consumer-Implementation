
# Real-Time Event Ticketing System with Advanced Producer-Consumer Implementation

## Overview

The **Real-Time Event Ticketing System** leverages advanced programming concepts like multi-threading and concurrency to simulate a real-world ticketing process. It integrates a producer-consumer model where vendors (producers) generate tickets, and customers (consumers) retrieve them. This system also features a **Real-Time Analytics Dashboard** for monitoring ticket operations dynamically.

---

## Key Features

- **Producer-Consumer Pattern**
  - Vendors produce tickets, and customers consume tickets concurrently.
  - Efficient resource management using Java’s concurrent utilities.
- **Real-Time Logs via WebSocket**
  - Logs production and consumption of tickets in real-time on the frontend.
- **Customizable Configuration**
  - Configure total tickets, vendor/customer rates, and ticket pool capacity.
- **Thread-Safe Operations**
  - Synchronizes updates to the shared ticket pool using Java’s locking mechanisms.
- **Real-Time Analytics Dashboard**
  - Displays live ticket data and historical trends using Chart.js.
- **Log File Generation**
  - Stores event logs in a file for offline analysis.

---

## Technologies Used

### Backend

- **Java**: Implements multi-threaded producer-consumer processes.
- **Spring Boot**: Exposes RESTful services and WebSocket endpoints.
- **WebSocket with STOMP Protocol**: Enables real-time communication between backend and frontend.
- **Concurrent Data Structures**:
  - `ConcurrentLinkedQueue`: Manages ticket pool for thread-safe operations.
  - `ReentrantLock`: Ensures thread synchronization.
- **PostgreSQL**: Manages persistent storage for configuration and ticket data.
- **File Logging**: Logs system activities using Java IO.

### Frontend

- **React.js**: Develops the real-time analytics dashboard.
- **SockJS and Stomp.js**: Establishes WebSocket connections.
- **Chart.js**: Visualizes ticket data trends in real-time.

### Development Tools

- **IDE**: IntelliJ IDEA / Eclipse (backend development).
- **Node.js**: Manages frontend development.
- **npm**: Handles React.js dependencies.
- **Web Browser**: Tests and debugs the frontend.

---

## Project Architecture

### Backend

- **Producer-Consumer Implementation**
  - Vendors (producers) add tickets to a shared ticket pool.
  - Customers (consumers) retrieve tickets from the pool.
  - Multi-threaded operations handle ticket generation and retrieval.
- **WebSocket Communication**
  - Streams real-time logs and ticket data updates to the frontend.
- **Database Interaction**
  - Stores configuration parameters and ticket data in PostgreSQL.

### Frontend

- **User Interface**
  - Provides an input form for configuration parameters.
  - Displays real-time logs and ticket counts dynamically.
- **Real-Time Chart**
  - Visualizes total tickets and pool availability over time using Chart.js.

---

## Setup Instructions

### Prerequisites

1. Install **PostgreSQL** and configure a database.
2. Install **Node.js** and **npm**.
3. Clone this repository.

### Backend Setup

1. Navigate to the backend folder:
   ```bash
   cd backend
   ```
2. Open the project in your preferred IDE (IntelliJ IDEA/Eclipse).
3. Ensure Maven dependencies are loaded.
4. Configure `application.properties` to connect to your PostgreSQL database.
5. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

### Frontend Setup

1. Navigate to the frontend folder:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm start
   ```
4. Open your browser and navigate to:
   ```
   http://localhost:3006
   ```

---

## Database Schema

### Configuration Table

| Column                    | Type | Description                         |
| ------------------------- | ---- | ----------------------------------- |
| id                        | INT  | Primary key                         |
| total\_tickets            | INT  | Total tickets available             |
| max\_capacity             | INT  | Maximum capacity of the ticket pool |
| ticket\_release\_rate     | INT  | Rate at which tickets are produced  |
| customer\_retrieval\_rate | INT  | Rate at which tickets are consumed  |
| active\_customers         | INT  | Number of active customers          |
| active\_vendors           | INT  | Number of active vendors            |

---

## API Endpoints

### Configuration Endpoints

- **POST** `/api/configuration/start`
  - Starts the ticketing system with user-defined parameters.
- **GET** `/api/configuration`
  - Retrieves all saved configurations.

### WebSocket Endpoints

- **/logs/ticket**: Streams real-time log data.
- **/counts/ticket**: Streams ticket count updates.

---

## Known Issues

- Ensure WebSocket connections are not blocked by firewalls.
- If logs are not displayed, verify backend WebSocket endpoint availability.

---

## Future Improvements

- Add user authentication for secure configurations.
- Enhance the dashboard with more analytics features.
- Optimize multi-threaded operations for large-scale usage.

---

## Contributors

-Nethum Mihiranga

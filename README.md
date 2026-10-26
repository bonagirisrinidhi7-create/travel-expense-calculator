# Travel Expense Calculator

A full-stack travel budgeting and expense management web application. Designed to allow travellers and trip organizers to log trips, itemize transit, accommodation, and dining expenses, view instant real-time cost previews, and store records reliably in a MySQL database with authoritative backend calculation guarantees.

---

## Features

- **Trip Itinerary Management**: Create, view, update, and delete trip records with destination name, trip duration (days), and traveller count.
- **Itemized Expense Tracking**: Segregate expenses into Travel / Flights, Accommodation / Lodging, and Food & Dining.
- **Real-Time Client-Side Preview**: Computes total estimated expenditure and cost per traveller instantaneously as numbers are entered.
- **Authoritative Server-Side Calculation**: Recalculates all totals and per-person splits on the backend using high-precision `BigDecimal` arithmetic and `RoundingMode.HALF_UP` rounding to guarantee data integrity.
- **Currency Localization**: Formats all monetary values into Indian Rupees (`₹`) adhering to `en-IN` numbering conventions (`Intl.NumberFormat`).
- **Comprehensive Validation**: Multi-tier validation preventing empty destinations, non-positive durations/travellers, and negative expense values.
- **Modern Responsive Dashboard**: Clean white background with blue and purple accents, metric overview cards, itemized badges, search filters, and smooth mobile/tablet adaptations.

---

## Technology Stack

### Frontend
- **Framework**: React.js 19
- **Build Tool**: Vite 8
- **HTTP Client**: Axios
- **Styling**: Vanilla CSS with custom properties, glassmorphism headers, responsive grids, and subtle elevation shadows

### Backend
- **Framework**: Spring Boot 3.3.5
- **Language**: Java 21 LTS
- **Build Tool**: Apache Maven 3.9+
- **Persistence**: Spring Data JPA & Hibernate ORM
- **Validation**: Jakarta Bean Validation (`spring-boot-starter-validation`)
- **Driver**: MySQL Connector/J

### Database
- **Database Engine**: MySQL Server 8.0+
- **Dialect**: `org.hibernate.dialect.MySQLDialect`
- **Schema Management**: Hibernate auto-update (`ddl-auto=update`)

---

## Project Structure

```text
travel-expense-calculator/
├── .gitignore                          # Root Git ignore rules
├── README.md                           # Project documentation
├── backend/                            # Spring Boot REST API
│   ├── .env.example                    # Reference template for backend env vars
│   ├── pom.xml                         # Maven dependencies & plugins
│   └── src/
│       ├── main/
│       │   ├── java/com/example/travelexpense/
│       │   │   ├── TravelExpenseApplication.java
│       │   │   ├── controller/
│       │   │   │   └── TripController.java
│       │   │   ├── dto/
│       │   │   │   ├── TripRequestDTO.java
│       │   │   │   └── TripResponseDTO.java
│       │   │   ├── entity/
│       │   │   │   └── Trip.java
│       │   │   ├── exception/
│       │   │   │   ├── ErrorResponse.java
│       │   │   │   ├── GlobalExceptionHandler.java
│       │   │   │   └── ResourceNotFoundException.java
│       │   │   ├── repository/
│       │   │   │   └── TripRepository.java
│       │   │   └── service/
│       │   │       ├── TripService.java
│       │   │       └── TripServiceImpl.java
│       │   └── resources/
│       │       └── application.properties
│       └── test/java/com/example/travelexpense/
│           ├── DatabaseConnectionVerificationTest.java
│           ├── controller/
│           │   └── TripControllerTest.java
│           └── service/
│               └── TripServiceTest.java
└── frontend/                           # React.js + Vite Application
    ├── .env.example                    # Template for frontend env vars
    ├── .gitignore                      # Frontend-specific ignore rules
    ├── index.html                      # HTML entry point
    ├── package.json                    # Node dependencies & scripts
    ├── vite.config.js                  # Vite configuration
    └── src/
        ├── api/
        │   ├── client.js               # Axios instance configuration
        │   └── tripApi.js              # REST endpoints & currency formatting
        ├── components/
        │   ├── Navbar.jsx              # Application navigation & API status
        │   ├── NotificationToast.jsx   # Feedback alert banners
        │   ├── SummaryHeader.jsx       # Aggregate spend & trip metrics
        │   ├── TripCard.jsx            # Individual trip display & actions
        │   ├── TripForm.jsx            # Add/Edit form with live estimation
        │   └── TripList.jsx            # Responsive trip grid & search filter
        ├── App.jsx                     # Top-level state and orchestration
        ├── index.css                   # Complete responsive design system
        └── main.jsx                    # React DOM root mounting
```

---

## Prerequisites

Before running the project locally, ensure the following are installed:

- **Java Development Kit (JDK)**: Version 21 LTS (`java -version`)
- **Apache Maven**: Version 3.8+ or 3.9+ (`mvn -version`)
- **Node.js & npm**: Node 18+ or 20+ and npm 9+ (`node -v` and `npm -v`)
- **MySQL Server**: Version 8.0+ running on port `3306`

---

## MySQL Database Setup

1. Start your local MySQL service (e.g. through Windows Services, Docker, or MySQL Workbench).
2. Log into MySQL and create the target database:
   ```sql
   CREATE DATABASE IF NOT EXISTS travel_expense_db;
   ```
3. Tables (`trips`) will be automatically created and maintained by Hibernate upon Spring Boot startup.

---

## Environment Variable Configuration

> **IMPORTANT**: Spring Boot reads standard system and shell environment variables directly. It **does NOT** automatically load `.env` files without external libraries. You must configure these variables in your active terminal session before starting the backend.

### Variable Reference

| Variable | Description | Default if Unset |
| :--- | :--- | :--- |
| `PORT` | Spring Boot server port | `8080` |
| `CORS_ALLOWED_ORIGINS` | Permitted frontend origins (comma-separated) | `http://localhost:5173` |
| `DB_URL` | Optional full cloud JDBC connection string | *Constructed from parameters below* |
| `DB_HOST` | MySQL Server host address | `localhost` |
| `DB_PORT` | MySQL Server port | `3306` |
| `DB_NAME` | MySQL database name | `travel_expense_db` |
| `DB_USERNAME` | MySQL database user | `root` |
| `DB_PASSWORD` | MySQL password for the specified user | *empty string* |
| `DB_SSL_MODE` | MySQL SSL mode (`PREFERRED`, `REQUIRED`, etc.) | `PREFERRED` |
| `DB_USE_SSL` | Enable or disable SSL connection | `false` |
| `SHOW_SQL` | Toggle Hibernate SQL logging | `false` |

### Setting Environment Variables in Terminal

#### Windows (PowerShell):
```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your_actual_mysql_password"
```

#### macOS / Linux (Bash or Zsh):
```bash
export DB_USERNAME="root"
export DB_PASSWORD="your_actual_mysql_password"
```

#### Windows (Command Prompt `cmd.exe`):
```cmd
set DB_USERNAME=root
set DB_PASSWORD=your_actual_mysql_password
```

---

## Running the Application

### 1. Start the Backend (Spring Boot)

Open a terminal, set the database credentials, and start the Spring Boot service:

```powershell
cd backend
$env:DB_PASSWORD = "your_actual_mysql_password"
mvn spring-boot:run
```

- Backend REST API will start at: `http://localhost:8080`
- API Root Endpoint: `http://localhost:8080/api/trips`

To run the backend test suite:
```powershell
cd backend
mvn clean test
```

---

### 2. Start the Frontend (React + Vite)

Open a second terminal, install dependencies (if not already installed), and start the development server:

```powershell
cd frontend
npm install
npm run dev
```

- Frontend Web App will be accessible at: `http://localhost:5173`

To build the frontend for production:
```powershell
cd frontend
npm run build
```

---

## REST API Documentation

Base URL: `http://localhost:8080/api`

| Method | Endpoint | Description | Request Body | Response Status |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/trips` | Add a new trip with calculated totals | `TripRequestDTO` | `201 Created` |
| `GET` | `/trips` | Retrieve all trips ordered by newest first | *None* | `200 OK` |
| `GET` | `/trips/{id}` | Retrieve details for a single trip | *None* | `200 OK` / `404 Not Found` |
| `PUT` | `/trips/{id}` | Update trip and trigger authoritative recalculation | `TripRequestDTO` | `200 OK` / `404 Not Found` |
| `DELETE` | `/trips/{id}` | Delete a trip by ID | *None* | `204 No Content` / `404 Not Found` |

### Sample Request Payload (`TripRequestDTO`)
```json
{
  "destination": "Goa Beach Holiday",
  "numberOfDays": 4,
  "numberOfTravellers": 2,
  "travelExpense": 5000.00,
  "accommodationExpense": 7000.00,
  "foodExpense": 3000.00
}
```

### Sample Response Payload (`TripResponseDTO`)
```json
{
  "id": 1,
  "destination": "Goa Beach Holiday",
  "numberOfDays": 4,
  "numberOfTravellers": 2,
  "travelExpense": 5000.00,
  "accommodationExpense": 7000.00,
  "foodExpense": 3000.00,
  "totalCost": 15000.00,
  "costPerTraveller": 7500.00,
  "createdAt": "2026-10-26T19:52:03.319021",
  "updatedAt": "2026-10-26T19:52:03.319021"
}
```

---

## Calculation Rules

$$\text{Total Cost} = \text{Travel Expense} + \text{Accommodation Expense} + \text{Food Expense}$$

$$\text{Cost per Traveller} = \frac{\text{Total Cost}}{\text{Number of Travellers}} \quad (\text{RoundingMode.HALF\_UP, 2 decimal places})$$

---

## License

This project is open-source and available under the [MIT License](LICENSE).

# EntertainmentLists

Project - General Solution for Performing CRUD Operations on User Lists Derived from Database Data

---

## Features

-   Consume external APIs to get information on various types of entertainment content.
-   Manage lists of IDs associated with different services.
-   CRUD operations (Create, Read, Update and Delete).
-   Example implementations (Kitsu and TMDB).

---

## How to get started

To run the project, you need to have a Java Development Kit (JDK) installed, preferably **JDK 21**, and **Maven**.
The application uses an in-memory H2 database for development purposes.

You will also need to create a `.env` file in the `backend` directory containing your API keys for the external providers to work correctly.

---

## API endpoints

### Authentication
-   **POST /api/auth/register**
Register a new user.
-   **POST /api/auth/login**
Authenticate and receive a JWT token.

### Entertainment Entity
-   **GET /api/entertainment-entity**
Get a paginated list of entities from an external API (requires `provider` and `type` query parameters).
-   **GET /api/entertainment-entity/search**
Search for entities in an external API (requires `provider`, `type`, and `query` parameters).
-   **GET /api/entertainment-entity/{id}**
Get detailed information about a specific entity from an external API.

### Entertainment List
-   **GET /api/entertainment-list**
Get all entertainment lists for the authenticated user.
-   **POST /api/entertainment-list**
Create a new entertainment list. Requires a JSON body.
-   **GET /api/entertainment-list/{id}**
Get a specific list by ID, including its stored entities.
-   **PUT /api/entertainment-list/{id}**
Update an existing entertainment list. Requires a JSON body.
-   **DELETE /api/entertainment-list/{id}**
Delete an entertainment list.
-   **POST /api/entertainment-list/{id}/entities/{entityId}**
Add an entity to a specific list.
-   **DELETE /api/entertainment-list/{id}/entities/{entityId}**
Remove an entity from a specific list.

---

## Technologies used

### Backend
-   **Framework:** Spring Boot
-   **Language:** Java
-   **Database:** H2 (in-memory)

### Frontend
-   **Framework:** React + Vite
-   **Language:** TypeScript
-   **Styling:** Tailwind CSS

### General
-   **Version control:** Git

---

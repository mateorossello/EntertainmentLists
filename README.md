# EntertainmentLists

Practice project - General solution for performing CRUD operations on user lists derived from database data, built with Java

---

## Features

-   Consume external APIs to get information on various types of entertainment content.
-   Manage lists of IDs associated with different services.
-   CRUD operations (Create, Read, Update and Delete).
-   Example implementation for the Kitsu API.

---

## How to get started

To run the project, you need to have a Java Development Kit (JDK) installed, preferably **JDK 17**, and **Maven**.
The application uses an in-memory H2 database for development purposes.

---

## API endpoints

-   **GET /api/entertainment-entity/{id}?type=...**
Get information about an entity from an external API.
-   **GET /api/entertainment-list**
Get all entertainment lists.
-   **GET /api/entertainment-list/{id}**
Get a specific list by ID, with detailed information.
-   **POST /api/entertainment-list**
Create a new entertainment list. Requires a **request body (JSON)** with the data and accept **query parameters**.
-   **PUT /api/entertainment-list/{id}**
Update an existing entertainment list. Requires a **request body (JSON)** with the updated data.
-   **DELETE /api/entertainment-list/{id}**
Delete a entertainment list.

---

## Technologies used

-   **Framework:** Spring Boot
-   **Language:** Java
-   **Database:** H2 (in-memory)
-   **Version control:** Git

---

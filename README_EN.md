[_Magyar Verzió_](README.md)
# Spring's Sky Travel - Project

## Description

This project simulates a travel agency called Spring's Sky Travel. It manages organized trips and their associated
reservations, which also include a list of participants.

---

## Structure

### Journey Entity

The `Journey` entity has the following attributes:

* __id__ (Long, generated)
* __destination__ (String, @NotBlank)
* __description__ (String, @NotBlank)
* __method__ (Enum, @NotNull)
* __departureDate__ (LocalDate, @FutureOrPresent)
* __number_of_nights__ (int, @Positive)
* __price_per_participant__ (int, @Positive)
* __reservations__ (List of `Reservation`)

Endpoints:

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `"/api/journeys"` | retrieves all journeys |
| GET | `"/api/journeys/{id}"` | retrieves a journey by `id` |
| GET | `"/api/journeys/destinations"` | retrieves the set of destinations |
| POST | `"/api/journeys"` | creates a new journey |
| PUT | `"/api/journeys/{id}"` | updates a journey by `id` |
| DELETE | `"/api/journeys/{id}"` | deletes a journey by `id` |

The call to retrieve all entities can be parameterized: you can specify a date to search for active journeys from (`after`), and
you can request sorting by price in ascending/descending order (`priceOrderBy=desc/asc`).

Another operating principle is that past journeys cannot be created, and upon deletion, all reservations associated with the journey are also deleted.

---

### Reservation Entity

The `Reservation` entity has the following attributes:

* __id__ (Long, generated)
* __contactPerson__ (String, @NotBlank)
* __requestedService__ (Enum, @NotNull)
* __fullPrice__ (int, calculated)
* __participants__ (List of `Participant`, @Size(min = 1))
* __journey__ (`Journey`)

The `Participant` class attributes:

* __name__ (String, @NotBlank)
* __age__ (int, @Positive)

There is a bidirectional, one-to-many relationship between the `Journey` and `Reservation` entities.

Endpoints:

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `"/api/reservations"` | retrieves all reservations |
| GET | `"/api/reservations/{id}"` | retrieves a reservation by `id` |
| POST | `"/api/reservations"` | creates a reservation |
| POST | `"/api/reservations/{id}/participants"` | adds a participant to the reservation by `id` |
| PUT | `"/api/reservations/{id}"` | updates a reservation by `id` |
| DELETE | `"/api/reservations/{id}"` | deletes a reservation by `id` |

The call to retrieve all entities can be parameterized: you can specify a boolean value to search only for active reservations (`onlyActive`), and you can specify a number to search for reservations with fewer participants than that number (`groupSize`).

Another operating principle is that new reservations cannot be created for past journeys, and the full price is calculated and corrected during both creation and update. It is also required that deleting a reservation within a certain number of days before departure is not allowed.

---

## Technical Details

The project is a three-layer application (Repository, Service, Controller), using MariaDB as its database. Flyway is responsible for its migrations. A SwaggerUI interface has been integrated for API testing. Validation is handled by Hibernate Validation, while standardized error handling is provided by Zalando Problem.

Database structure:
![diagram](db.png)

The application can be started as a Jar, or a layered Docker container can be built from it using the provided Dockerfile. Furthermore, the necessary configuration file for Docker Compose is also included.

The reservation rules and pricing method can be extended by implementing or possibly expanding the `ReservationRuleSet` interface, allowing the business logic to be flexible within defined frames if needed later.

---
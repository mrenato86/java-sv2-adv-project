[_English Version_](README_EN.md)
# Spring's Sky Travel - Project

## Leírás

A projekt egy utazási irodát (Spring's Sky Travel) szimulál. Szervezett utazásokat, illetve az ezekhez kapcsolódó
foglalásokat kezeli, melyek utaslistát is tartalmaznak.

---

## Felépítés

### Journey entitás

A `Journey` entitás a következő attribútumokkal rendelkezik:

* __id__ (Long, generated)
* __destination__ (String, @NotBlank)
* __description__ (String, @NotBlank)
* __method__ (Enum, @NotNull)
* __departureDate__ (LocalDate, @FutureOrPresent)
* __number_of_nights__ (int, @Positive)
* __price_per_participant__ (int, @Positive)
* __reservations__ (List of `Reservation`)

Végpontok:

| HTTP metódus | Végpont                          | Leírás                                |
| ------------ | -------------------------------- | ------------------------------------- |
| GET          | `"/api/journeys"`                | lekérdezi az összes utazást           |
| GET          | `"/api/journeys/{id}"`           | lekérdez egy utazást `id` alapján     |
| GET          | `"/api/journeys/destinations"`   | lekérdez az úti célok halmazát        |
| POST         | `"/api/journeys"`                | létrehoz egy új utazást               |
| PUT          | `"/api/journeys/{id}"`           | frissít egy utazást `id` alapján      |
| DELETE       | `"/api/journeys/{id}"`           | töröl egy utazást `id` alapján        |

Az összes entitást lekérdező hívás paraméterezhető: megadható dátum, hogy mikortól élő utazásokat keressen (`after`), illetve
kérhető ár szerinti növekvő/csökkenő rendezés (`priceOrderBy=desc/asc`).

További működési elv, hogy nem hozható létre múltbéli utazás, törlés esetén pedig az utazáshoz tartozó összes foglalás is törlődik.

---

### Reservation entitás

A `Reservation` entitás a következő attribútumokkal rendelkezik:

* __id__ (Long, generated)
* __contactPerson__ (String, @NotBlank)
* __requestedService__ (Enum, @NotNull)
* __fullPrice__ (int, calculated)
* __participants__ (List of `Participant`, @Size(min = 1))
* __journey__ (`Journey`)

A `Participant` osztály attribútumai:

* __name__ (String, @NotBlank)
* __age__ (int, @Positive)

A `Journey` és a `Reservation` entitások között kétirányú, 1-n kapcsolat van.

Végpontok:

| HTTP metódus | Végpont                                 | Leírás                                        |
| ------------ | --------------------------------------- | --------------------------------------------- |
| GET          | `"/api/reservations"`                   | lekérdezi az összes foglalást                 |
| GET          | `"/api/reservations/{id}"`              | lekérdez egy foglalást `id` alapján           |
| POST         | `"/api/reservations"`                   | léterhoz egy foglalást                        |
| POST         | `"/api/reservations/{id}/participants"` | hozzáad egy utast a foglaláshoz `id` alapján  |
| PUT          | `"/api/reservations/{id}"`              | frissít egy foglalást `id` alapján            |
| DELETE       | `"/api/reservations/{id}"`              | töröl egy foglalást `id` alapján              |

Az összes entitást lekérdező hívás paraméterezhető: megadható egy logikai érték, hogy csak az aktív foglalásokat keresse (`onlyActive`), illetve megadható egy szám, hogy hány fő alatti foglalásokat keresünk (`groupSize`).

További működési elv, hogy új foglalás nem hozható létre múltbeli utazásra, illetve a teljes ár létrehozásnál, frissítésnél is kiszámolásra, javításra kerül. Előírás még, hogy foglalás törlése adott nappal az indulás előtt nem megengedett.

---

## Technológiai részletek

A projekt egy háromrétegű alkalmazás (Repository, Service, Controller), mely adatbázisként MariaDb-t használ, melynek migrációjáért a Flyway eszköz felelős. Beépítésre került az API tesztelésére szolgáló SwaggerUI felület. A validálást a Hibernate Validation végzi, míg a szabványos hibakezelést a Zalando Problem megoldása adja.

Az adatbázis felépítése:

![diagram](db.png)


Az alkalmazás indítható Jar-ként, vagy építhető belőle rétegezett Docker container a mellékelt a Dockerfile segítségével, továbbá Docker Compose-hoz is adva van a szükséges konfigurációs állomány.

A foglalások szabályait, és az árszámítás módját bővíteni lehet a `ReservationRuleSet` interfész implementálásval, esetleges bővítésével, így az üzleti logika adott keretek között rugalmas lehet, ha erre később igény van.

---

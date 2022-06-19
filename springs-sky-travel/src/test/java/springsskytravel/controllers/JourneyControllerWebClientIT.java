package springsskytravel.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import springsskytravel.commands.CreateJourneyCommand;
import springsskytravel.commands.UpdateJourneyCommand;
import springsskytravel.dtos.JourneyDto;
import springsskytravel.model.Journey;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from participants", "delete from reservations", "delete from journeys"})
class JourneyControllerWebClientIT {

    @Autowired
    WebTestClient webClient;

    JourneyDto testJourneyDto;

    @BeforeEach
    void setUp() {
        webClient.post()
                .uri("/api/journeys")
                .bodyValue(new CreateJourneyCommand("Hawaii", "Nice Vacation", Journey.Method.PLANE, LocalDate.now().plusMonths(8), 7, 100_000))
                .exchange();

        testJourneyDto = webClient.post()
                .uri("/api/journeys")
                .bodyValue(new CreateJourneyCommand("Rome", "Last minute", Journey.Method.BUS, LocalDate.now().plusDays(6), 1, 20_000))
                .exchange()
                .expectBody(JourneyDto.class)
                .returnResult()
                .getResponseBody();

        webClient.post()
                .uri("/api/journeys")
                .bodyValue(new CreateJourneyCommand("Hallstatt", "Romantic Weekend", Journey.Method.PLANE, LocalDate.now().plusMonths(3), 3, 80_000))
                .exchange();
    }

    @Test
    void testReadAllJourney() {
        webClient.get()
                .uri("/api/journeys")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(JourneyDto.class)
                .value(list -> assertThat(list)
                        .hasSize(3)
                        .extracting(JourneyDto::getDestination)
                        .containsOnly("Hawaii", "Rome", "Hallstatt"));
    }

    @Test
    void testReadAllJourneyWithParameters() {
        webClient.get()
                .uri(builder -> builder.path("/api/journeys")
                        .queryParam("after", LocalDate.now().plusMonths(2))
                        .queryParam("priceOrderBy", "asc")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(JourneyDto.class)
                .value(list -> assertThat(list)
                        .hasSize(2)
                        .extracting(JourneyDto::getPricePerParticipant)
                        .containsExactly(80_000, 100_000));
    }

    @Test
    void testReadJourney() {
        webClient.get()
                .uri("/api/journeys/{id}", testJourneyDto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JourneyDto.class)
                .value(journey -> assertThat(journey)
                        .extracting(JourneyDto::getDestination, JourneyDto::getPricePerParticipant)
                        .containsOnly("Rome", 20_000));
    }

    @Test
    void testReadDistinctDestinations() {
        webClient.post()
                .uri("/api/journeys")
                .bodyValue(new CreateJourneyCommand("Hallstatt", "-", Journey.Method.PLANE, LocalDate.now().plusMonths(3), 3, 80_000))
                .exchange();

        webClient.get()
                .uri("api/journeys/destinations")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String[].class)
                .isEqualTo(new String[]{"Hawaii", "Rome", "Hallstatt"});
    }

    @Test
    void testCreateJourney() {
        webClient.post()
                .uri("/api/journeys")
                .bodyValue(new CreateJourneyCommand("Hallstatt", "-", Journey.Method.PLANE, LocalDate.now().plusMonths(3), 3, 80_000))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JourneyDto.class)
                .value(journey -> assertThat(journey)
                        .extracting(JourneyDto::getDestination, JourneyDto::getDescription, JourneyDto::getNumberOfNights)
                        .containsExactly("Hallstatt", "-", 3));
    }

    @Test
    void testUpdateJourney() {
        webClient.put()
                .uri("/api/journeys/{id}", testJourneyDto.getId())
                .bodyValue(new UpdateJourneyCommand(2, 40_000))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JourneyDto.class)
                .value(journey -> assertThat(journey.getNumberOfNights())
                        .isEqualTo(2))
                .value(journey -> assertThat(journey.getPricePerParticipant())
                        .isEqualTo(40_000));
    }

    @Test
    void testDeleteJourney() {
        webClient.delete()
                .uri("/api/journeys/{id}", testJourneyDto.getId())
                .exchange()
                .expectStatus().isNoContent();
        webClient.get()
                .uri("/api/journeys/{id}", testJourneyDto.getId())
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
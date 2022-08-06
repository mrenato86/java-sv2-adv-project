package springsskytravel.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.violations.ConstraintViolationProblem;
import springsskytravel.commands.AddParticipantCommand;
import springsskytravel.commands.CreateJourneyCommand;
import springsskytravel.commands.CreateReservationCommand;
import springsskytravel.commands.UpdateReservationCommand;
import springsskytravel.dtos.ReservationDto;
import springsskytravel.model.Journey;
import springsskytravel.model.Reservation;
import springsskytravel.services.JourneyService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from participants", "delete from reservations", "delete from journeys"})
class ReservationControllerWebClientValidationIT {

    @Autowired
    WebTestClient webClient;

    @Autowired
    JourneyService journeyService;

    @Test
    void testReadReservationNotFound() {
        webClient.get()
                .uri("/api/reservations/{id}", 100)
                .exchange()
                .expectStatus().isNotFound();
        webClient.post()
                .uri("/api/reservations")
                .bodyValue(new CreateReservationCommand("Agent", Reservation.Service.NONE,
                        List.of(new AddParticipantCommand("Adult", 38)), 100L))
                .exchange()
                .expectStatus().isNotFound();
        webClient.post()
                .uri("/api/reservations/{id}/participants", 100)
                .bodyValue(new AddParticipantCommand("Friend", 20))
                .exchange()
                .expectStatus().isNotFound();
        webClient.put()
                .uri("/api/reservations/{id}", 100)
                .bodyValue(new UpdateReservationCommand(Reservation.Service.FULL_BOARD))
                .exchange()
                .expectStatus().isNotFound();
        webClient.delete()
                .uri("/api/reservations/{id}", 100)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateReservationWithInvalidDataNulls() {
        webClient.post()
                .uri("/api/reservations")
                .bodyValue(new CreateReservationCommand(null, null, null, null))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(problem -> assertThat(problem.getTitle())
                        .isEqualTo("Constraint Violation"))
                .value(problem -> assertThat(problem.getViolations())
                        .hasSize(4));
    }

    @Test
    void testCreateReservationWithInvalidDataEmptyString() {
        webClient.post()
                .uri("/api/reservations")
                .bodyValue(new CreateReservationCommand("", Reservation.Service.NONE,
                        List.of(new AddParticipantCommand("Adult", 38)), 1L))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(problem -> assertThat(problem.getTitle())
                        .isEqualTo("Constraint Violation"))
                .value(problem -> assertThat(problem.getViolations())
                        .hasSize(1));
    }

    @Test
    void testCreateReservationWithInvalidDataEmptyList() {
        webClient.post()
                .uri("/api/reservations")
                .bodyValue(new CreateReservationCommand("Agent", Reservation.Service.NONE, List.of(), 1L))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(problem -> assertThat(problem.getTitle())
                        .isEqualTo("Constraint Violation"))
                .value(problem -> assertThat(problem.getViolations())
                        .hasSize(1));
    }

    @Test
    void testCreateReservationWithInvalidDataInvalidParticipant() {
        webClient.post()
                .uri("/api/reservations")
                .bodyValue(new CreateReservationCommand("Agent", Reservation.Service.NONE,
                        List.of(new AddParticipantCommand("", -1)), 1L))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(problem -> assertThat(problem.getTitle())
                        .isEqualTo("Constraint Violation"))
                .value(problem -> assertThat(problem.getViolations())
                        .hasSize(2));
    }

    @Test
    void testCreateReservationToPastJourney() {
        long romeId = journeyService.createJourney(
                new CreateJourneyCommand("Rome", "Last minute", Journey.Method.BUS, LocalDate.now().minusDays(3), 1, 20_000)
        ).getId();

        CreateReservationCommand romeReservation = new CreateReservationCommand("Agent2", Reservation.Service.NONE,
                List.of(new AddParticipantCommand("Adult", 38)), romeId);

        webClient.post()
                .uri("/api/reservations")
                .bodyValue(romeReservation)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void testAddInvalidParticipant() {
        ReservationDto testReservationDto = createTestReservation();

        webClient.post()
                .uri("/api/reservations/{id}/participants", testReservationDto.getId())
                .bodyValue(new AddParticipantCommand("", -20))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(problem -> assertThat(problem.getTitle())
                        .isEqualTo("Constraint Violation"))
                .value(problem -> assertThat(problem.getViolations())
                        .hasSize(2));
    }

    @Test
    void testUpdateReservationWithInvalidData() {
        ReservationDto testReservationDto = createTestReservation();

        webClient.put()
                .uri("/api/reservations/{id}", testReservationDto.getId())
                .bodyValue(new UpdateReservationCommand(null))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(problem -> assertThat(problem.getTitle())
                        .isEqualTo("Constraint Violation"))
                .value(problem -> assertThat(problem.getViolations())
                        .hasSize(1));
    }

    @Test
    void testDeleteReservationNotAllowed() {
        ReservationDto testReservationDto = createTestReservation();

        webClient.delete()
                .uri("/api/reservations/{id}", testReservationDto.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

    private ReservationDto createTestReservation() {
        long romeId = journeyService.createJourney(
                new CreateJourneyCommand("Rome", "Last minute", Journey.Method.BUS, LocalDate.now().plusDays(3), 1, 20_000)
        ).getId();

        CreateReservationCommand romeReservation = new CreateReservationCommand("Agent2", Reservation.Service.NONE,
                List.of(new AddParticipantCommand("Adult", 38)), romeId);

        return webClient.post()
                .uri("/api/reservations")
                .bodyValue(romeReservation)
                .exchange()
                .expectBody(ReservationDto.class)
                .returnResult().getResponseBody();
    }


}
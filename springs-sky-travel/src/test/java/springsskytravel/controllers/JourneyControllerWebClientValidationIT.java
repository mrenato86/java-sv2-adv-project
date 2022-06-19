package springsskytravel.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.violations.ConstraintViolationProblem;
import springsskytravel.commands.CreateJourneyCommand;
import springsskytravel.commands.UpdateJourneyCommand;
import springsskytravel.dtos.JourneyDto;
import springsskytravel.model.Journey;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JourneyControllerWebClientValidationIT {

    @Autowired
    WebTestClient webClient;

    @Test
    void testReadJourneyNotFound() {
        webClient.get()
                .uri("/api/journeys/{id}", 100)
                .exchange()
                .expectStatus().isNotFound();
        webClient.put()
                .uri("/api/journeys/{id}", 100)
                .bodyValue(new UpdateJourneyCommand(2, 100))
                .exchange()
                .expectStatus().isNotFound();
        webClient.delete()
                .uri("/api/journeys/{id}", 100)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateJourneyWithInvalidDataNulls() {
        webClient.post()
                .uri("/api/journeys")
                .bodyValue(new CreateJourneyCommand(null, null, null, null, 5, 10))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(problem -> assertThat(problem.getTitle())
                        .isEqualTo("Constraint Violation"))
                .value(problem -> assertThat(problem.getViolations())
                        .hasSize(4));
    }

    @Test
    void testCreateJourneyWithInvalidDataEmptyStrings() {
        webClient.post()
                .uri("/api/journeys")
                .bodyValue(new CreateJourneyCommand("", "", null, null, 5, 10))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(problem -> assertThat(problem.getTitle())
                        .isEqualTo("Constraint Violation"))
                .value(problem -> assertThat(problem.getViolations())
                        .hasSize(4));
    }

    @Test
    void testCreateJourneyWithInvalidDataPastDate() {
        webClient.post()
                .uri("/api/journeys")
                .bodyValue(new CreateJourneyCommand("ok", "ok", Journey.Method.PLANE, LocalDate.now().minusYears(1), 5, 10))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(problem -> assertThat(problem.getTitle())
                        .isEqualTo("Constraint Violation"))
                .value(problem -> assertThat(problem.getViolations())
                        .hasSize(1));
    }

    @Test
    void testCreateJourneyWithInvalidDataNegativeNumbers() {
        webClient.post()
                .uri("/api/journeys")
                .bodyValue(new CreateJourneyCommand("ok", "ok", Journey.Method.PLANE, LocalDate.now().plusYears(1), -5, -10))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(problem -> assertThat(problem.getTitle())
                        .isEqualTo("Constraint Violation"))
                .value(problem -> assertThat(problem.getViolations())
                        .hasSize(2));
    }

    @Test
    void testUpdateJourneyWithInvalidData() {
        JourneyDto testJourneyDto = webClient
                .post()
                .uri("/api/journeys")
                .bodyValue(new CreateJourneyCommand("Rome", "Last minute", Journey.Method.BUS, LocalDate.now().plusDays(6), 1, 20_000))
                .exchange()
                .expectBody(JourneyDto.class)
                .returnResult().getResponseBody();

        assert testJourneyDto != null;
        webClient.put()
                .uri("/api/journeys/{id}", testJourneyDto.getId())
                .bodyValue(new UpdateJourneyCommand(-2, 0))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(problem -> assertThat(problem.getTitle())
                        .isEqualTo("Constraint Violation"))
                .value(problem -> assertThat(problem.getViolations())
                        .hasSize(2));
    }

}
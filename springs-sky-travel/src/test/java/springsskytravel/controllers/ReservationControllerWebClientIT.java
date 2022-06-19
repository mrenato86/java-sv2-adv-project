package springsskytravel.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import springsskytravel.commands.CreateJourneyCommand;
import springsskytravel.commands.CreateReservationCommand;
import springsskytravel.commands.UpdateReservationCommand;
import springsskytravel.commands.UpdateReservationParticipantsCommand;
import springsskytravel.dtos.ReservationDto;
import springsskytravel.model.Journey;
import springsskytravel.model.Participant;
import springsskytravel.model.Reservation;
import springsskytravel.services.JourneyService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from participants", "delete from reservations", "delete from journeys"})
class ReservationControllerWebClientIT {

    @Autowired
    WebTestClient webClient;

    @Autowired
    JourneyService journeyService;

    ReservationDto testReservationDto;

    @BeforeEach
    void setUp() {
        long hawaiiId = journeyService.createJourney(
                new CreateJourneyCommand("Hawaii", "Nice Vacation", Journey.Method.PLANE, LocalDate.now().plusMonths(8), 7, 100_000)
        ).getId();
        long romeId = journeyService.createJourney(
                new CreateJourneyCommand("Rome", "Last minute", Journey.Method.BUS, LocalDate.now().plusDays(3), 1, 20_000)
        ).getId();

        CreateReservationCommand hawaiiReservation = new CreateReservationCommand("Agent1", Reservation.Service.FULL_BOARD,
                List.of(new Participant("Child", 10), new Participant("Adult", 28)), hawaiiId);
        CreateReservationCommand romeReservation = new CreateReservationCommand("Agent2", Reservation.Service.NONE,
                List.of(new Participant("Adult", 38)), romeId);

        testReservationDto = webClient.post()
                .uri("/api/reservations")
                .bodyValue(hawaiiReservation)
                .exchange()
                .expectBody(ReservationDto.class)
                .returnResult()
                .getResponseBody();

        webClient.post()
                .uri("/api/reservations")
                .bodyValue(romeReservation)
                .exchange();

    }

    @Test
    void testReadAllReservation() {
        webClient.get()
                .uri("/api/reservations")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ReservationDto.class)
                .value(list -> assertThat(list)
                        .hasSize(2)
                        .extracting(ReservationDto::getContactPerson)
                        .containsOnly("Agent1", "Agent2"));
    }

    @Test
    void testReadAllReservationWithParameters() {
        webClient.get()
                .uri(builder -> builder.path("/api/reservations")
                        .queryParam("onlyActive", true)
                        .queryParam("groupSize", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ReservationDto.class)
                .value(list -> assertThat(list)
                        .hasSize(1)
                        .extracting(ReservationDto::getContactPerson)
                        .containsExactly("Agent2"));
    }

    @Test
    void testReadReservationById() {
        webClient.get()
                .uri("/api/reservations/{id}", testReservationDto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReservationDto.class)
                .value(reservation -> assertThat(reservation)
                        .extracting(ReservationDto::getContactPerson, ReservationDto::getRequestedService)
                        .containsOnly("Agent1", Reservation.Service.FULL_BOARD))
                .value(reservation -> assertTrue(reservation.getFullPrice() > 0));

    }

    @Test
    void testCreateReservation() {
        CreateReservationCommand newReservation = new CreateReservationCommand("Agent2", Reservation.Service.HALF_BOARD,
                List.of(new Participant("Adult", 19)), testReservationDto.getJourney().getId());

        webClient.post()
                .uri("/api/reservations")
                .bodyValue(newReservation)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ReservationDto.class)
                .value(reservation -> assertThat(reservation)
                        .extracting(ReservationDto::getContactPerson, ReservationDto::getRequestedService)
                        .containsOnly("Agent2", Reservation.Service.HALF_BOARD))
                .value(reservation -> assertTrue(reservation.getFullPrice() > 0));
    }

    @Test
    void testAddParticipantToReservation() {
        int fullPriceBeforeChange = testReservationDto.getFullPrice();

        webClient.post()
                .uri("/api/reservations/{id}/participants", testReservationDto.getId())
                .bodyValue(new UpdateReservationParticipantsCommand("Friend", 20))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ReservationDto.class)
                .value(reservation -> assertThat(reservation.getParticipants())
                        .hasSize(3)
                        .extracting(Participant::getName)
                        .contains("Child", "Adult", "Friend"))
                .value(reservation -> assertTrue(reservation.getFullPrice() > fullPriceBeforeChange));
    }

    @Test
    void testUpdateReservation() {
        int fullPriceBeforeChange = testReservationDto.getFullPrice();

        webClient.put()
                .uri("/api/reservations/{id}", testReservationDto.getId())
                .bodyValue(new UpdateReservationCommand(Reservation.Service.NONE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReservationDto.class)
                .value(reservation -> assertEquals(Reservation.Service.NONE, reservation.getRequestedService()))
                .value(reservation -> assertTrue(reservation.getFullPrice() < fullPriceBeforeChange));
    }

    @Test
    void testDeleteReservation() {
        webClient.delete()
                .uri("/api/reservations/{id}", testReservationDto.getId())
                .exchange()
                .expectStatus().isNoContent();
        webClient.get()
                .uri("/api/journeys/{id}", testReservationDto.getId())
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
package springsskytravel.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springsskytravel.model.Participant;
import springsskytravel.model.Reservation;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReservationRepositoryIT {
    @Autowired
    ReservationRepository repository;

    Reservation testReservation;

    @BeforeEach
    void setUp() {
        Reservation reservation = new Reservation("Kiss József", Reservation.Service.FULL_BOARD);
        reservation.addParticipant(new Participant("Child", 8));
        reservation.addParticipant(new Participant("Wife", 35));
        testReservation = repository.save(reservation);

        repository.save(new Reservation("John Doe", Reservation.Service.HALF_BOARD));
        repository.save(new Reservation("Tóth Béla", Reservation.Service.NONE));
    }

    @Test
    void testFindAll() {
        assertThat(repository.findAll())
                .hasSize(3)
                .extracting(Reservation::getContactPerson)
                .containsExactly("Kiss József", "John Doe", "Tóth Béla");
    }

    @Test
    void testFindById() {
        assertThat(repository.findById(testReservation.getId()))
                .get()
                .extracting(Reservation::getContactPerson)
                .isEqualTo("Kiss József");
    }

    @Test
    void testDeleteById() {
        repository.deleteById(testReservation.getId());

        assertThat(repository.findById(testReservation.getId()))
                .isNotPresent();
    }

    @Test
    void testDeleteAll() {
        repository.deleteAll();

        assertThat(repository.findAll())
                .isEmpty();
    }

    @Test
    void testGetReservationsWithParticipantsLessThan() {
        assertThat(repository.getReservationsWithParticipantsLessThan(Optional.of(1)))
                .hasSize(2);
    }
}

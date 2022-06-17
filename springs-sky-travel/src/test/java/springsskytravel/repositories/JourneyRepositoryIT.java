package springsskytravel.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springsskytravel.model.Journey;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JourneyRepositoryIT {

    @Autowired
    JourneyRepository repository;

    Journey testJourney;

    @BeforeEach
    void setUp() {
        testJourney = repository.save(new Journey("Hawaii", "Nice Vacation", Journey.Method.PLANE, LocalDate.parse("2022-08-21"), 7, 100_000));
        repository.save(new Journey("Rome", "Nice sightseeing", Journey.Method.BUS, LocalDate.parse("2022-07-21"), 1, 20_000));
        repository.save(new Journey("Hallstatt", "Romantic Weekend", Journey.Method.PLANE, LocalDate.parse("2022-08-01"), 3, 80_000));
    }

    @Test
    void testFindAll() {
        assertThat(repository.findAll())
                .hasSize(3)
                .extracting(Journey::getDestination)
                .containsExactly("Hawaii", "Rome", "Hallstatt");
    }

    @Test
    void testFindById() {
        assertThat(repository.findById(testJourney.getId()))
                .get()
                .extracting(Journey::getDestination)
                .isEqualTo("Hawaii");
    }

    @Test
    void testDeleteById() {
        repository.deleteById(testJourney.getId());

        assertThat(repository.findById(testJourney.getId()))
                .isNotPresent();
    }

    @Test
    void testDeleteAll() {
        repository.deleteAll();

        assertThat(repository.findAll())
                .isEmpty();
    }
}
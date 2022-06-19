package springsskytravel.services.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import springsskytravel.model.Journey;
import springsskytravel.model.Participant;
import springsskytravel.model.Reservation;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BasicReservationRuleSetTest {

    BasicReservationRuleSet ruleSet;
    Reservation reservation;
    Journey lastMinuteNoDeleteJourney;
    Journey normalJourney;
    Journey pastJourney;

    @BeforeEach
    void setUp() {
        ruleSet = new BasicReservationRuleSet();

        lastMinuteNoDeleteJourney = new Journey("Rome", "-", Journey.Method.BUS, LocalDate.now().plusDays(2), 1, 20_000);
        normalJourney = new Journey("Rome", "-", Journey.Method.BUS, LocalDate.now().plusDays(20), 1, 20_000);
        pastJourney = new Journey("Rome", "-", Journey.Method.BUS, LocalDate.now().minusDays(20), 1, 20_000);

        reservation = new Reservation("Kiss József", Reservation.Service.FULL_BOARD);
        reservation.addParticipant(new Participant("Child", 8));
        reservation.addParticipant(new Participant("Wife", 35));
    }

    @Test
    void testCanBeDeleted() {
        reservation.setJourney(normalJourney);
        assertTrue(ruleSet.canBeDeleted(reservation));
    }

    @Test
    void testCanBeDeletedCantDelete() {
        reservation.setJourney(lastMinuteNoDeleteJourney);
        assertFalse(ruleSet.canBeDeleted(reservation));
    }

    @Test
    void testCalculateFullPrice() {
        reservation.setJourney(normalJourney);
        assertEquals(40_000, ruleSet.calculateFullPrice(reservation));
    }

    @Test
    void testCalculateFullPriceLastMinute() {
        reservation.setJourney(lastMinuteNoDeleteJourney);
        assertEquals(30_400, ruleSet.calculateFullPrice(reservation));
    }

    @Test
    void testIsDepartureDateAccepted() {
        reservation.setJourney(normalJourney);
        assertTrue(ruleSet.isDepartureDateAccepted(reservation));
    }

    @Test
    void testIsDepartureDateAcceptedPastDate() {
        reservation.setJourney(pastJourney);
        assertFalse(ruleSet.isDepartureDateAccepted(reservation));
    }
}
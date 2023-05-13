package springsskytravel.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class ReservationToPastJourneyException extends AbstractThrowableProblem {
    public ReservationToPastJourneyException() {
        super(URI.create("reservations/reservation-denied"),
                "Forbidden",
                Status.FORBIDDEN,
                "Reservation to past not allowed, please contact support");
    }
}

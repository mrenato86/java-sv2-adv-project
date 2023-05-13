package springsskytravel.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class ReservationNotFoundException extends AbstractThrowableProblem {
    public ReservationNotFoundException(long id) {
        super(URI.create("reservations/not-found"),
                "Not Found",
                Status.NOT_FOUND,
                String.format("Reservation not found: %d", id));
    }
}

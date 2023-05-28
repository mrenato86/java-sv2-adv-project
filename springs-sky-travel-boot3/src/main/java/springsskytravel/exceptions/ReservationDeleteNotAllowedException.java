package springsskytravel.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class ReservationDeleteNotAllowedException extends AbstractThrowableProblem {
    public ReservationDeleteNotAllowedException(long id) {
        super(URI.create("reservations/delete-denied"),
                "Forbidden",
                Status.FORBIDDEN,
                "Reservation(id: %d) cannot be deleted, please contact support".formatted(id));
    }
}

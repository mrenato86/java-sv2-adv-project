package springsskytravel.exceptions;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class JourneyNotFoundException extends AbstractThrowableProblem {
    public JourneyNotFoundException(long id) {
        super(URI.create("journeys/not-found"),
                "Not Found",
                Status.NOT_FOUND,
                String.format("Journey not found: %d", id));
    }
}

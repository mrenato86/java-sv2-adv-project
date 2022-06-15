package springsskytravel.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springsskytravel.model.Journey;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public abstract class JourneyCommand {

    private String destination;
    private String description;
    private Journey.Method method;
    private LocalDate departureDate;
    private int numberOfNights;
    private int pricePerParticipant;
}

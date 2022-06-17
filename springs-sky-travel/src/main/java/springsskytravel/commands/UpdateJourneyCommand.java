package springsskytravel.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateJourneyCommand {
    @Positive(message = "Number of nights must be positive")
    private int numberOfNights;
    @Positive(message = "Price must be positive")
    private int pricePerParticipant;
}

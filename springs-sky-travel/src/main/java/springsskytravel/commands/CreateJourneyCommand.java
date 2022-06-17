package springsskytravel.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springsskytravel.model.Journey;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateJourneyCommand {

    @NotBlank(message = "Destination must be present")
    private String destination;
    @NotBlank(message = "Description must be present")
    private String description;
    @NotNull(message = "Journey method must be present")
    private Journey.Method method;
    @FutureOrPresent(message = "Departure date in the past is not allowed")
    private LocalDate departureDate;
    @Positive(message = "Number of nights must be positive")
    private int numberOfNights;
    @Positive(message = "Price must be positive")
    private int pricePerParticipant;

}

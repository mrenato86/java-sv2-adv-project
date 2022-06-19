package springsskytravel.commands;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Number of nights", example = "7")
    private int numberOfNights;

    @Positive(message = "Price must be positive")
    @Schema(description = "Price per participant", example = "100000")
    private int pricePerParticipant;
}

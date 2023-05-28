package springsskytravel.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springsskytravel.model.Journey;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateJourneyCommand {

    @NotBlank(message = "Destination must be present")
    @Schema(description = "Destination", example = "Rome")
    private String destination;

    @NotBlank(message = "Description must be present")
    @Schema(description = "Description", example = "Weekend")
    private String description;

    @NotNull(message = "Journey method must be present")
    @Schema(description = "Journey method", example = "PLANE")
    private Journey.Method method;

    @FutureOrPresent(message = "Departure date in the past is not allowed")
    @NotNull(message = "Departure date must be present")
    @Schema(description = "Date of departure", example = "2025-08-15")
    private LocalDate departureDate;

    @Positive(message = "Number of nights must be positive")
    @Schema(description = "Number of nights", example = "7")
    private int numberOfNights;

    @Positive(message = "Price must be positive")
    @Schema(description = "Price per participant", example = "100000")
    private int pricePerParticipant;

}

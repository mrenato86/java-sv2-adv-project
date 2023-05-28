package springsskytravel.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddParticipantCommand {

    @NotBlank(message = "Name must be provided")
    @Schema(description = "Name of participant", example = "John Doe")
    private String name;

    @PositiveOrZero(message = "Age cannot be negative")
    @Schema(description = "Age of participant", example = "21")
    private int age;
}

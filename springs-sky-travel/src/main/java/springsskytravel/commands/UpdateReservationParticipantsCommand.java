package springsskytravel.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateReservationParticipantsCommand {

    @NotBlank(message = "Name must be provided")
    private String name;
    @PositiveOrZero(message = "Age cannot be negative")
    private int age;
}

package springsskytravel.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springsskytravel.model.Reservation;

import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateReservationCommand {

    @NotNull(message = "Requested Service must be present")
    @Schema(description = "Type of service", example = "HALF_BOARD")
    private Reservation.Service requestedService;

}

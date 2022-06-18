package springsskytravel.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springsskytravel.model.Reservation;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateReservationCommand {

    @NotNull(message = "Requested Service must be present")
    private Reservation.Service requestedService;

}

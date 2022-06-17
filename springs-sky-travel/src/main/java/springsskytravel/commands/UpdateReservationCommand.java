package springsskytravel.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springsskytravel.model.Reservation;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateReservationCommand {

    private Reservation.Service requestedService;

}

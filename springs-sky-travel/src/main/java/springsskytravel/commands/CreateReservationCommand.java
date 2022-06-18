package springsskytravel.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springsskytravel.model.Participant;
import springsskytravel.model.Reservation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateReservationCommand {

    @NotBlank(message = "Contact Name must be provided")
    private String contactPerson;
    @NotNull(message = "Requested Service must be present")
    private Reservation.Service requestedService;
    @Size(min = 1, message = "At least one participant must be added")
    private List<Participant> participants;
    @NotNull(message = "Journey Id must be provided")
    private Long journeyId;
}

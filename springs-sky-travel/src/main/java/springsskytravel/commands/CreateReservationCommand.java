package springsskytravel.commands;

import springsskytravel.model.Journey;
import springsskytravel.model.Participant;
import springsskytravel.model.Reservation;

import java.util.List;

public class CreateReservationCommand extends ReservationCommand{
    public CreateReservationCommand(String contactPerson, Reservation.Service requestedService, List<Participant> participants, Journey journey) {
        super(contactPerson, requestedService, participants, journey);
    }
}

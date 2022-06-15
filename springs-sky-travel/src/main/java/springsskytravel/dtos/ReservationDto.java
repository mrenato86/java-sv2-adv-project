package springsskytravel.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springsskytravel.model.Journey;
import springsskytravel.model.Participant;
import springsskytravel.model.Reservation;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReservationDto {

    private Long id;
    private String contactPerson;
    private Reservation.Service requestedService;
    private int fullPrice;
    private List<Participant> participants;

    private Journey journey;
}

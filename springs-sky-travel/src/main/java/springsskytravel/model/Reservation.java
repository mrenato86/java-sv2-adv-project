package springsskytravel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "reservations")
public class Reservation {

    public enum Service {
        NONE, HALF_BOARD, FULL_BOARD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contact_person")
    private String contactPerson;

    @Enumerated(EnumType.STRING)
    @Column(name = "requested_service")
    private Service requestedService;

    @Column(name = "full_price")
    private int fullPrice;

    @ElementCollection
    @CollectionTable(name = "participants", joinColumns = @JoinColumn(name = "reservation_id"))
    @AttributeOverride(name = "name", column = @Column(name = "full_name"))
    private List<Participant> participants = new ArrayList<>();

    @ManyToOne
    private Journey journey;

    public Reservation(String contactPerson, Service requestedService) {
        this.contactPerson = contactPerson;
        this.requestedService = requestedService;
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }
}

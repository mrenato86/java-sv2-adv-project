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

    private enum Service {
        NONE, HALF_BOARD, FULL_BOARD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contactPerson;

    @Enumerated(EnumType.STRING)
    @Column(name = "requested_service")
    private Service requestedService;

    @Column(name = "full_price")
    private int fullPrice;

    @ElementCollection
    @CollectionTable(name = "participants", joinColumns = @JoinColumn(name = "reservation_id"))
    private List<Participant> participants = new ArrayList<>();

    @ManyToOne
    private Journey journey;
}
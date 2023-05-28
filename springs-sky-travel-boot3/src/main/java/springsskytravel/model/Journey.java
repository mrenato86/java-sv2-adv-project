package springsskytravel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "journeys")
public class Journey {

    public enum Method {
        PLANE, TRAIN, BUS, INDIVIDUAL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destination;

    @Column(name = "journey_description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "journey_method")
    private Method method;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "number_of_nights")
    private int numberOfNights;

    @Column(name = "price_per_participant")
    private int pricePerParticipant;

    @OneToMany(mappedBy = "journey", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations = new ArrayList<>();

    public Journey(String destination, String description, Method method, LocalDate departureDate, int numberOfNights, int pricePerParticipant) {
        this.destination = destination;
        this.description = description;
        this.method = method;
        this.departureDate = departureDate;
        this.numberOfNights = numberOfNights;
        this.pricePerParticipant = pricePerParticipant;
    }

    public void addReservation(Reservation reservation) {
        reservation.setJourney(this);
        this.reservations.add(reservation);
    }
}

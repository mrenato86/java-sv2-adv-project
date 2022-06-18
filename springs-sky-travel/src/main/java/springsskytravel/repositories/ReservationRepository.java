package springsskytravel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springsskytravel.model.Reservation;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("select r from Reservation r where (r.journey.departureDate >= CURRENT_DATE) and (:group is null or r.participants.size <= :group)")
    List<Reservation> getActiveReservationsWithParticipantsLessThan(Optional<Integer> group);

    @Query("select r from Reservation r where :group is null or r.participants.size <= :group")
    List<Reservation> getReservationsWithParticipantsLessThan(Optional<Integer> group);
}

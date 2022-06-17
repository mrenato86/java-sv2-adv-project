package springsskytravel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springsskytravel.model.Journey;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {

    @Query("select j from Journey j where (:after is null or j.departureDate >= :after) order by j.pricePerParticipant desc")
    List<Journey> getJourneysAfterDateOrderedPriceDesc(@Param("after") Optional<LocalDate> after);

    @Query("select j from Journey j where (:after is null or j.departureDate >= :after) order by j.pricePerParticipant")
    List<Journey> getJourneysAfterDateOrderedPriceAsc(@Param("after") Optional<LocalDate> after);

    @Query("select j from Journey j where (:after is null or j.departureDate >= :after)")
    List<Journey> getJourneysAfterDate(@Param("after") Optional<LocalDate> after);

}

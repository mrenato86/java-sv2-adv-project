package springsskytravel.services.rules;

import springsskytravel.model.Reservation;

public interface ReservationRuleSet {

    int calculateFullPrice(Reservation reservation);

    boolean canBeDeleted(Reservation reservation);

    boolean isDepartureDateAccepted(Reservation reservation);
}

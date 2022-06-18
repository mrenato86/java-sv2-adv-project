package springsskytravel.services.rules;

import springsskytravel.model.Reservation;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class BasicReservationRuleSet implements ReservationRuleSet {

    private static final int DELETE_NOT_ELIGIBLE = 3;
    private static final int LAST_MINUTE_ELIGIBLE = 7;
    private static final int YOUNG_AGE_LIMIT = 18;
    private static final int LAST_MINUTE_DISCOUNT = 30;
    private static final int YOUNG_DISCOUNT = 40;
    private static final int HALF_BOARD_PRICE_PER_DAY = 2000;
    private static final int FULL_BOARD_PRICE_PER_DAY = 4000;

    @Override
    public boolean canBeDeleted(Reservation reservation) {
        LocalDate departure = reservation.getJourney().getDepartureDate();
        long daysBetween = LocalDate.now().until(departure, DAYS);
        return daysBetween > DELETE_NOT_ELIGIBLE;
    }

    @Override
    public int calculateFullPrice(Reservation reservation) {
        int fullPrice = reservation.getParticipants().stream()
                .mapToInt(p -> getPriceByAge(p.getAge(), reservation.getJourney().getPricePerParticipant()))
                .sum();
        if (isEligibleToLastMinute(reservation)) {
            fullPrice = fullPrice * (100 - LAST_MINUTE_DISCOUNT) / 100;
        }
        return fullPrice + calculateBoardPrice(reservation);
    }

    private int getPriceByAge(int age, int price) {
        return age <= YOUNG_AGE_LIMIT ? (price * (100 - YOUNG_DISCOUNT)) / 100 : price;
    }

    private boolean isEligibleToLastMinute(Reservation reservation) {
        LocalDate departure = reservation.getJourney().getDepartureDate();
        long daysBetween = LocalDate.now().until(departure, DAYS);
        if (daysBetween < 0) {
            throw new IllegalArgumentException("Invalid calculation");
        }
        return daysBetween <= LAST_MINUTE_ELIGIBLE;
    }

    private int calculateBoardPrice(Reservation reservation) {
        int nights = reservation.getJourney().getNumberOfNights();
        int numOfParticipants = reservation.getParticipants().size();
        return switch (reservation.getRequestedService()) {
            case NONE -> 0;
            case HALF_BOARD -> HALF_BOARD_PRICE_PER_DAY * nights * numOfParticipants;
            case FULL_BOARD -> FULL_BOARD_PRICE_PER_DAY * nights * numOfParticipants;
        };
    }

}

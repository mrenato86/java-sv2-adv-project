package springsskytravel.commands;

import springsskytravel.model.Journey;

import java.time.LocalDate;

public class UpdateJourneyCommand extends JourneyCommand{
    public UpdateJourneyCommand(String destination, String description, Journey.Method method, LocalDate departureDate, int numberOfNights, int pricePerParticipant) {
        super(destination, description, method, departureDate, numberOfNights, pricePerParticipant);
    }
}

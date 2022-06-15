package springsskytravel.commands;

import springsskytravel.model.Journey;

import java.time.LocalDate;

public class CreateJourneyCommand extends JourneyCommand{

    public CreateJourneyCommand(String destination, String description, Journey.Method method, LocalDate departureDate, int numberOfNights, int pricePerParticipant) {
        super(destination, description, method, departureDate, numberOfNights, pricePerParticipant);
    }
}

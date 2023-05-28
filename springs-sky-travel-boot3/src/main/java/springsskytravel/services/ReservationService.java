package springsskytravel.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import springsskytravel.commands.CreateReservationCommand;
import springsskytravel.commands.UpdateReservationCommand;
import springsskytravel.commands.AddParticipantCommand;
import springsskytravel.dtos.ReservationDto;
import springsskytravel.exceptions.JourneyNotFoundException;
import springsskytravel.exceptions.ReservationDeleteNotAllowedException;
import springsskytravel.exceptions.ReservationNotFoundException;
import springsskytravel.exceptions.ReservationToPastJourneyException;
import springsskytravel.model.Journey;
import springsskytravel.model.Participant;
import springsskytravel.model.Reservation;
import springsskytravel.repositories.JourneyRepository;
import springsskytravel.repositories.ReservationRepository;
import springsskytravel.services.rules.ReservationRuleSet;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class ReservationService {

    private ReservationRepository reservationRepository;
    private JourneyRepository journeyRepository;
    private ModelMapper modelMapper;
    private ReservationRuleSet reservationRuleSet;

    public List<ReservationDto> readAllReservation(Optional<Boolean> onlyActive, Optional<Integer> groupSize) {
        List<Reservation> result;
        if (onlyActive.orElse(false)) {
            result = reservationRepository.getActiveReservationsWithParticipantsLessThan(groupSize);
        } else {
            result = reservationRepository.getReservationsWithParticipantsLessThan(groupSize);
        }
        return result.stream()
                .map(r -> modelMapper.map(r, ReservationDto.class))
                .collect(Collectors.toList());
    }

    public ReservationDto readReservationById(long id) {
        return modelMapper.map(fetchReservationById(id), ReservationDto.class);
    }

    public ReservationDto createReservation(CreateReservationCommand command) {
        Journey journey = journeyRepository.findById(command.getJourneyId())
                .orElseThrow(() -> new JourneyNotFoundException(command.getJourneyId()));
        Reservation reservation = modelMapper.map(command, Reservation.class);
        journey.addReservation(reservation);
        if (!reservationRuleSet.isDepartureDateAccepted(reservation)) {
            throw new ReservationToPastJourneyException();
        }
        reservation.calculateFullPrice(reservationRuleSet);
        return modelMapper.map(reservationRepository.save(reservation), ReservationDto.class);
    }

    public ReservationDto addParticipantToReservation(long id, AddParticipantCommand command) {
        Reservation toUpdate = fetchReservationById(id);
        toUpdate.addParticipant(modelMapper.map(command, Participant.class));
        toUpdate.calculateFullPrice(reservationRuleSet);
        return modelMapper.map(toUpdate, ReservationDto.class);
    }

    public ReservationDto updateReservationById(long id, UpdateReservationCommand command) {
        Reservation toUpdate = fetchReservationById(id);
        toUpdate.setRequestedService(command.getRequestedService());
        toUpdate.calculateFullPrice(reservationRuleSet);
        return modelMapper.map(toUpdate, ReservationDto.class);
    }

    public void deleteReservationById(long id) {
        if (!reservationRuleSet.canBeDeleted(fetchReservationById(id))) {
            throw new ReservationDeleteNotAllowedException(id);
        }
        reservationRepository.deleteById(id);
    }

    private Reservation fetchReservationById(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }
}

package springsskytravel.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springsskytravel.commands.CreateReservationCommand;
import springsskytravel.commands.UpdateReservationCommand;
import springsskytravel.commands.UpdateReservationParticipantsCommand;
import springsskytravel.dtos.ReservationDto;
import springsskytravel.services.ReservationService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private ReservationService reservationService;

    @GetMapping
    public List<ReservationDto> readAllReservation(Optional<Boolean> onlyActive, Optional<Integer> groupSize) {
        return reservationService.readAllReservation(onlyActive, groupSize);
    }

    @GetMapping("/{id}")
    public ReservationDto readReservationById(@PathVariable long id) {
        return reservationService.readReservationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto createReservation(@Valid @RequestBody CreateReservationCommand command) {
        return reservationService.createReservation(command);
    }

    @PostMapping("/{id}/participants")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto addParticipantToReservation(@PathVariable long id, @Valid @RequestBody UpdateReservationParticipantsCommand command) {
        return reservationService.addParticipantToReservation(id, command);
    }

    @PutMapping("/{id}")
    public ReservationDto updateReservation(@PathVariable long id, @Valid @RequestBody UpdateReservationCommand command) {
        return reservationService.updateReservationById(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable long id) {
        reservationService.deleteReservationById(id);
    }

}

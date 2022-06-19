package springsskytravel.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Web operations on Reservations")
public class ReservationController {

    private ReservationService reservationService;

    @GetMapping
    @Operation(summary = "Finds all reservations (optionally filtered) in a list")
    @ApiResponse(responseCode = "200", description = "Reservation query successful")
    public List<ReservationDto> readAllReservation(Optional<Boolean> onlyActive, Optional<Integer> groupSize) {
        return reservationService.readAllReservation(onlyActive, groupSize);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Finds one reservation by ID")
    @ApiResponse(responseCode = "200", description = "Reservation found")
    @ApiResponse(responseCode = "404", description = "Reservation not found")
    public ReservationDto readReservationById(@PathVariable long id) {
        return reservationService.readReservationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a reservation")
    @ApiResponse(responseCode = "201", description = "Reservation has been created")
    @ApiResponse(responseCode = "400", description = "Constraint violation")
    public ReservationDto createReservation(@Valid @RequestBody CreateReservationCommand command) {
        return reservationService.createReservation(command);
    }

    @PostMapping("/{id}/participants")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adds a participant to a reservation by ID")
    @ApiResponse(responseCode = "201", description = "Reservation has been updated")
    @ApiResponse(responseCode = "400", description = "Constraint violation")
    @ApiResponse(responseCode = "404", description = "Reservation not found")
    public ReservationDto addParticipantToReservation(@PathVariable long id, @Valid @RequestBody UpdateReservationParticipantsCommand command) {
        return reservationService.addParticipantToReservation(id, command);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Changes the state of a reservation by ID")
    @ApiResponse(responseCode = "200", description = "Reservation has been updated")
    @ApiResponse(responseCode = "400", description = "Constraint violation")
    @ApiResponse(responseCode = "404", description = "Reservation not found")
    public ReservationDto updateReservation(@PathVariable long id, @Valid @RequestBody UpdateReservationCommand command) {
        return reservationService.updateReservationById(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a reservation by ID")
    @ApiResponse(responseCode = "204", description = "Reservation has been deleted")
    @ApiResponse(responseCode = "403", description = "Reservation deletion refused")
    @ApiResponse(responseCode = "404", description = "Reservation not found")
    public void deleteReservation(@PathVariable long id) {
        reservationService.deleteReservationById(id);
    }

}

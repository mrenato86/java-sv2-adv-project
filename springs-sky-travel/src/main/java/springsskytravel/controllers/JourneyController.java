package springsskytravel.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springsskytravel.commands.CreateJourneyCommand;
import springsskytravel.commands.UpdateJourneyCommand;
import springsskytravel.dtos.JourneyDto;
import springsskytravel.services.JourneyService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/journeys")
@Tag(name = "Web operations on Journeys")
public class JourneyController {

    private JourneyService journeyService;

    @GetMapping
    @Operation(summary = "Finds all journeys (optionally filtered) in a list")
    @ApiResponse(responseCode = "200", description = "Journey query successful")
    public List<JourneyDto> readAllJourney(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> after, Optional<String> priceOrderBy) {
        return journeyService.readAllJourney(after, priceOrderBy);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Finds one journey by ID")
    @ApiResponse(responseCode = "200", description = "Journey found")
    @ApiResponse(responseCode = "404", description = "Journey not found")
    public JourneyDto readJourney(@PathVariable long id) {
        return journeyService.readJourneyById(id);
    }

    @GetMapping("/destinations")
    @Operation(summary = "Gets all distinct destinations of journeys")
    @ApiResponse(responseCode = "200", description = "Query success")
    public Set<String> readDistinctDestinations() {
        return journeyService.readDistinctDestinations();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a journey")
    @ApiResponse(responseCode = "201", description = "Journey has been created")
    @ApiResponse(responseCode = "400", description = "Constraint violation")
    public JourneyDto createJourney(@Valid @RequestBody CreateJourneyCommand command) {
        return journeyService.createJourney(command);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Changes the state of an exact journey")
    @ApiResponse(responseCode = "200", description = "Successful update")
    @ApiResponse(responseCode = "400", description = "Constraint violation")
    @ApiResponse(responseCode = "404", description = "Journey not found")
    public JourneyDto updateJourney(@PathVariable long id, @Valid @RequestBody UpdateJourneyCommand command) {
        return journeyService.updateJourneyById(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes one journey by ID")
    @ApiResponse(responseCode = "204", description = "Deletion of journey was successful")
    @ApiResponse(responseCode = "404", description = "Journey not found")
    public void deleteJourney(@PathVariable long id) {
        journeyService.deleteJourneyById(id);
    }
}

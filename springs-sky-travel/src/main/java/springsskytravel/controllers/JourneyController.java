package springsskytravel.controllers;

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

@AllArgsConstructor
@RestController
@RequestMapping("/api/journeys")
public class JourneyController {

    private JourneyService journeyService;

    @GetMapping
    public List<JourneyDto> readAllJourney(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> after, Optional<String> priceOrderBy) {
        return journeyService.readAllJourney(after, priceOrderBy);
    }

    @GetMapping("{id}")
    public JourneyDto readJourney(@PathVariable long id) {
        return journeyService.readJourneyById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JourneyDto createJourney(@Valid @RequestBody CreateJourneyCommand command) {
        return journeyService.createJourney(command);
    }

    @PutMapping("{id}")
    public JourneyDto updateJourney(@PathVariable long id, @Valid @RequestBody UpdateJourneyCommand command) {
        return journeyService.updateJourneyById(id, command);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJourney(@PathVariable long id) {
        journeyService.deleteJourneyById(id);
    }
}

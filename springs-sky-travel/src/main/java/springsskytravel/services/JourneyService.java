package springsskytravel.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import springsskytravel.commands.CreateJourneyCommand;
import springsskytravel.commands.UpdateJourneyCommand;
import springsskytravel.dtos.JourneyDto;
import springsskytravel.exceptions.JourneyNotFoundException;
import springsskytravel.model.Journey;
import springsskytravel.repositories.JourneyRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class JourneyService {

    private JourneyRepository journeyRepository;
    private ModelMapper modelMapper;

    public List<JourneyDto> readAllJourney(Optional<LocalDate> after, Optional<String> priceOrderBy) {
        List<Journey> result = switch (priceOrderBy.orElse("unordered")) {
            case "asc" -> journeyRepository.getJourneysAfterDateOrderedByPriceAsc(after);
            case "desc" -> journeyRepository.getJourneysAfterDateOrderedByPriceDesc(after);
            default -> journeyRepository.getJourneysAfterDate(after);
        };
        return result.stream()
                .map(j -> modelMapper.map(j, JourneyDto.class))
                .collect(Collectors.toList());
    }

    public JourneyDto readJourneyById(long id) {
        return modelMapper.map(fetchJourneyById(id), JourneyDto.class);
    }

    public Set<String> readDistinctDestinations() {
        return journeyRepository.getDistinctDestinations();
    }

    public JourneyDto createJourney(CreateJourneyCommand command) {
        Journey journey = journeyRepository.save(modelMapper.map(command, Journey.class));
        return modelMapper.map(journey, JourneyDto.class);
    }

    public JourneyDto updateJourneyById(long id, UpdateJourneyCommand command) {
        Journey toUpdate = fetchJourneyById(id);
        toUpdate.setNumberOfNights(command.getNumberOfNights());
        toUpdate.setPricePerParticipant(command.getPricePerParticipant());
        return modelMapper.map(toUpdate, JourneyDto.class);
    }

    public void deleteJourneyById(long id) {
        if (!journeyRepository.existsById(id)) {
            throw new JourneyNotFoundException(id);
        }
        journeyRepository.deleteById(id);
    }

    private Journey fetchJourneyById(long id) {
        return journeyRepository.findById(id)
                .orElseThrow(() -> new JourneyNotFoundException(id));
    }
}

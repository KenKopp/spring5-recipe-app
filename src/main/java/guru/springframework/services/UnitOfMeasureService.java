package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UnitOfMeasureService {
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureConverter;

    public UnitOfMeasureService(UnitOfMeasureRepository unitOfMeasureRepository,
                                UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureConverter) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureConverter = unitOfMeasureConverter;
    }

    public Set<UnitOfMeasureCommand> getAll() {
        Set<UnitOfMeasureCommand> unitOfMeasures = new HashSet<>();
        unitOfMeasureRepository.findAll().forEach(u -> unitOfMeasures.add(unitOfMeasureConverter.convert(u)));
        return unitOfMeasures;
    }
}

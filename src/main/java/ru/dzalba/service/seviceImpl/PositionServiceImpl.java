package ru.dzalba.service.seviceImpl;

import org.springframework.stereotype.Service;
import ru.dzalba.dto.PositionDto;
import ru.dzalba.models.Position;
import ru.dzalba.repository.PositionRepository;
import ru.dzalba.service.PositionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public PositionDto createPosition(PositionDto positionDTO) {
        validatePositionDto(positionDTO);
        Position position = new Position();
        position.setTitle(positionDTO.getTitle());
        position.setSalary(positionDTO.getSalary());
        positionRepository.save(position);
        return positionDTO;
    }

    @Override
    public List<PositionDto> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(position -> new PositionDto(position.getId(), position.getTitle(), position.getSalary()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PositionDto> getPositionById(int id) {
        validateId(id);
        return Optional.ofNullable(positionRepository.findById(id))
                .map(position -> new PositionDto(position.getId(), position.getTitle(), position.getSalary()));
    }

    @Override
    public Optional<PositionDto> updatePosition(PositionDto positionDTO) {
        validatePositionDto(positionDTO);
        Position position = positionRepository.findById(positionDTO.getId());
        if (position != null) {
            position.setTitle(positionDTO.getTitle());
            position.setSalary(positionDTO.getSalary());
            positionRepository.update(position);
            return Optional.of(positionDTO);
        }
        return Optional.empty();
    }

    @Override
    public boolean deletePosition(int id) {
        validateId(id);
        Position position = positionRepository.findById(id);
        if (position != null) {
            positionRepository.delete(position);
            return true;
        }
        return false;
    }

    private void validatePositionDto(PositionDto positionDTO) {
        if (positionDTO == null) {
            throw new IllegalArgumentException("PositionDTO must not be null");
        }
        if (positionDTO.getTitle() == null || positionDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title must not be null or empty");
        }
        if (positionDTO.getSalary() < 0) {
            throw new IllegalArgumentException("Salary must be non-negative");
        }
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than zero");
        }
    }
}

package ru.dzalba.service;

import org.springframework.stereotype.Service;
import ru.dzalba.dao.PositionDao;
import ru.dzalba.dto.PositionDto;
import ru.dzalba.models.Position;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PositionService {
    private final PositionDao positionDAO;

    public PositionService(PositionDao positionDAO) {
        this.positionDAO = positionDAO;
    }

    public PositionDto createPosition(PositionDto positionDTO) {
        Position position = new Position(positionDTO.getId(), positionDTO.getTitle(), positionDTO.getSalary());
        positionDAO.addPosition(position);
        return positionDTO;
    }

    public List<PositionDto> getAllPositions() {
        return positionDAO.getAllPositions().stream()
                .map(position -> new PositionDto(position.getId(), position.getTitle(), position.getSalary()))
                .collect(Collectors.toList());
    }

    public Optional<PositionDto> getPositionById(int id) {
        return positionDAO.getPositionById(id)
                .map(position -> new PositionDto(position.getId(), position.getTitle(), position.getSalary()));
    }

    public Optional<PositionDto> updatePosition(PositionDto positionDTO) {
        Position position = new Position(positionDTO.getId(), positionDTO.getTitle(), positionDTO.getSalary());
        positionDAO.updatePosition(position);
        return Optional.of(positionDTO);
    }

    public boolean deletePosition(int id) {
        Optional<Position> position = positionDAO.getPositionById(id);
        if (position.isPresent()) {
            positionDAO.deletePosition(id);
            return true;
        }
        return false;
    }
}

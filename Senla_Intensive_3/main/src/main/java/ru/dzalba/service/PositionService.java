package ru.dzalba.service;

import ru.dzalba.dao.PositionDAO;
import ru.dzalba.dto.PositionDTO;
import ru.dzalba.models.Position;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PositionService {

    private final PositionDAO positionDAO;

    public PositionService(PositionDAO positionDAO) {
        this.positionDAO = positionDAO;
    }

    public PositionDTO createPosition(PositionDTO positionDTO) {
        Position position = new Position(positionDTO.getId(), positionDTO.getTitle(), positionDTO.getSalary());
        positionDAO.addPosition(position);
        return positionDTO;
    }

    public List<PositionDTO> getAllPositions() {
        return positionDAO.getAllPositions().stream()
                .map(position -> new PositionDTO(position.getId(), position.getTitle(), position.getSalary()))
                .collect(Collectors.toList());
    }

    public Optional<PositionDTO> getPositionById(int id) {
        return positionDAO.getPositionById(id)
                .map(position -> new PositionDTO(position.getId(), position.getTitle(), position.getSalary()));
    }

    public Optional<PositionDTO> updatePosition(PositionDTO positionDTO) {
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

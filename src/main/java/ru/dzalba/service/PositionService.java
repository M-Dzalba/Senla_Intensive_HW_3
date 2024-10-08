package ru.dzalba.service;

import ru.dzalba.dto.PositionDto;

import java.util.List;
import java.util.Optional;

public interface PositionService {

    PositionDto createPosition(PositionDto positionDTO);

    List<PositionDto> getAllPositions();

    Optional<PositionDto> getPositionById(int id);

    Optional<PositionDto> updatePosition(PositionDto positionDTO);

    boolean deletePosition(int id);
}

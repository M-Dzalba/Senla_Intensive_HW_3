package ru.dzalba.dao;

import ru.dzalba.models.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDAO {

    private final List<Position> positions = new ArrayList<>();

    public void addPosition(Position position) {
        positions.add(position);
    }

    public List<Position> getAllPositions() {
        return new ArrayList<>(positions);
    }

    public Optional<Position> getPositionById(int id) {
        return positions.stream()
                .filter(position -> position.getId() == id)
                .findFirst();
    }

    public void updatePosition(Position position) {
        getPositionById(position.getId()).ifPresent(existingPosition -> {
            existingPosition.setTitle(position.getTitle());
            existingPosition.setSalary(position.getSalary());
        });
    }

    public void deletePosition(int id) {
        positions.removeIf(position -> position.getId() == id);
    }
}

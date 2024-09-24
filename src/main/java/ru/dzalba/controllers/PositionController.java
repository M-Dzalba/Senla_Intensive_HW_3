package ru.dzalba.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzalba.dto.PositionDto;
import ru.dzalba.service.PositionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping
    public ResponseEntity<PositionDto> createPosition(@RequestBody PositionDto positionDTO) {
        PositionDto createdPosition = positionService.createPosition(positionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPosition);
    }

    @GetMapping
    public ResponseEntity<List<PositionDto>> getAllPositions() {
        List<PositionDto> positions = positionService.getAllPositions();
        return ResponseEntity.ok(positions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionDto> getPositionById(@PathVariable int id) {
        Optional<PositionDto> position = positionService.getPositionById(id);
        return position.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PositionDto> updatePosition(@PathVariable int id, @RequestBody PositionDto positionDTO) {
        if (positionService.getPositionById(id).isPresent()) {
            positionDTO.setId(id);
            PositionDto updatedPosition = positionService.updatePosition(positionDTO).orElse(null);
            return updatedPosition != null
                    ? ResponseEntity.ok(updatedPosition)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable int id) {
        if (positionService.deletePosition(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
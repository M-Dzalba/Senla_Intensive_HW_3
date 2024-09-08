package ru.dzalba.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public PositionController(PositionService positionService, ObjectMapper objectMapper) {
        this.positionService = positionService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<String> createPosition(@RequestBody PositionDto positionDTO) {
        PositionDto createdPosition = positionService.createPosition(positionDTO);
        try {
            String json = objectMapper.writeValueAsString(createdPosition);
            return new ResponseEntity<>(json, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<String> getAllPositions() {
        List<PositionDto> positions = positionService.getAllPositions();
        try {
            String json = objectMapper.writeValueAsString(positions);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPositionById(@PathVariable int id) {
        Optional<PositionDto> position = positionService.getPositionById(id);
        if (position.isPresent()) {
            try {
                String json = objectMapper.writeValueAsString(position.get());
                return new ResponseEntity<>(json, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePosition(@PathVariable int id, @RequestBody PositionDto positionDTO) {
        if (positionService.getPositionById(id).isPresent()) {
            positionDTO.setId(id);
            Optional<PositionDto> updatedPosition = positionService.updatePosition(positionDTO);
            if (updatedPosition.isPresent()) {
                try {
                    String json = objectMapper.writeValueAsString(updatedPosition.get());
                    return new ResponseEntity<>(json, HttpStatus.OK);
                } catch (JsonProcessingException e) {
                    return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable int id) {
        if (positionService.deletePosition(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
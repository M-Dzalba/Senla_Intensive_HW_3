package ru.dzalba.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzalba.dto.ProjectParticipationDto;
import ru.dzalba.service.seviceImpl.ProjectParticipationServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/participations")
public class ProjectParticipationController {

    private final ProjectParticipationServiceImpl projectParticipationServiceImpl;
    private final ObjectMapper objectMapper;

    public ProjectParticipationController(ProjectParticipationServiceImpl projectParticipationServiceImpl, ObjectMapper objectMapper) {
        this.projectParticipationServiceImpl = projectParticipationServiceImpl;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<String> createProjectParticipation(@RequestBody ProjectParticipationDto participationDto) {
        try {
            ProjectParticipationDto createdParticipation = projectParticipationServiceImpl.createProjectParticipation(participationDto);
            String json = objectMapper.writeValueAsString(createdParticipation);
            return new ResponseEntity<>(json, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<String> getAllProjectParticipations() {
        List<ProjectParticipationDto> participations = projectParticipationServiceImpl.getAllProjectParticipations();
        try {
            String json = objectMapper.writeValueAsString(participations);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProjectParticipationById(@PathVariable int id) {
        Optional<ProjectParticipationDto> participation = projectParticipationServiceImpl.getProjectParticipationById(id);
        if (participation.isPresent()) {
            try {
                String json = objectMapper.writeValueAsString(participation.get());
                return new ResponseEntity<>(json, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProjectParticipation(@PathVariable int id, @RequestBody ProjectParticipationDto participationDto) {
        participationDto.setEmployeeId(id);
        boolean isUpdated = projectParticipationServiceImpl.updateProjectParticipation(participationDto);
        if (isUpdated) {
            try {
                String json = objectMapper.writeValueAsString(participationDto);
                return new ResponseEntity<>(json, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectParticipation(@PathVariable int id) {
        if (projectParticipationServiceImpl.deleteProjectParticipation(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

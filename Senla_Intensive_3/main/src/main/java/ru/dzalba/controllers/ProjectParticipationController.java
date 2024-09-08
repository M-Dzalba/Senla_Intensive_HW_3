package ru.dzalba.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzalba.dto.ProjectParticipationDto;
import ru.dzalba.service.ProjectParticipationService;

import java.util.Optional;

@RestController
@RequestMapping("/participations")
public class ProjectParticipationController {

    private final ProjectParticipationService participationService;
    private final ObjectMapper objectMapper;

    public ProjectParticipationController(ProjectParticipationService participationService, ObjectMapper objectMapper) {
        this.participationService = participationService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<String> addParticipation(@RequestBody ProjectParticipationDto participationDTO) {
        ProjectParticipationDto addedParticipation = participationService.addParticipation(participationDTO);
        try {
            String json = objectMapper.writeValueAsString(addedParticipation);
            return new ResponseEntity<>(json, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{employeeId}/{projectId}")
    public ResponseEntity<String> getParticipation(@PathVariable int employeeId, @PathVariable int projectId) {
        Optional<ProjectParticipationDto> participation = participationService.getParticipation(employeeId, projectId);
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

    @PutMapping("/{employeeId}/{projectId}")
    public ResponseEntity<String> updateParticipation(@PathVariable int employeeId, @PathVariable int projectId,
                                                      @RequestBody ProjectParticipationDto participationDTO) {
        if (participationService.getParticipation(employeeId, projectId).isPresent()) {
            participationDTO.setEmployeeId(employeeId);
            participationDTO.setProjectId(projectId);
            Optional<ProjectParticipationDto> updatedParticipation = participationService.updateParticipation(participationDTO);
            if (updatedParticipation.isPresent()) {
                try {
                    String json = objectMapper.writeValueAsString(updatedParticipation.get());
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

    @DeleteMapping("/{employeeId}/{projectId}")
    public ResponseEntity<Void> deleteParticipation(@PathVariable int employeeId, @PathVariable int projectId) {
        if (participationService.deleteParticipation(employeeId, projectId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

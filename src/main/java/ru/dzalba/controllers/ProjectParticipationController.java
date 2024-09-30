package ru.dzalba.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzalba.dto.ProjectParticipationDto;
import ru.dzalba.service.ProjectParticipationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/participations")
public class ProjectParticipationController {

    private final ProjectParticipationService projectParticipationService;

    @Autowired
    public ProjectParticipationController(ProjectParticipationService projectParticipationService) {
        this.projectParticipationService = projectParticipationService;
    }

    @PostMapping
    public ResponseEntity<?> createProjectParticipation(@RequestBody ProjectParticipationDto participationDto) {
        try {
            ProjectParticipationDto createdParticipation = projectParticipationService.createProjectParticipation(participationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdParticipation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjectParticipationDto>> getAllProjectParticipations() {
        List<ProjectParticipationDto> participations = projectParticipationService.getAllProjectParticipations();
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectParticipationDto> getProjectParticipationById(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<ProjectParticipationDto> participation = projectParticipationService.getProjectParticipationById(id);
        return participation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectParticipationDto> updateProjectParticipation(@PathVariable Integer id, @RequestBody ProjectParticipationDto participationDto) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        participationDto.setEmployeeId(id);
        boolean isUpdated = projectParticipationService.updateProjectParticipation(participationDto);
        return isUpdated
                ? ResponseEntity.ok(participationDto)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectParticipation(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        if (projectParticipationService.deleteProjectParticipation(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

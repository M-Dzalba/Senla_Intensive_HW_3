package ru.dzalba.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzalba.dto.ProjectDto;
import ru.dzalba.service.seviceImpl.ProjectServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectServiceImpl projectServiceImpl;
    private final ObjectMapper objectMapper;

    public ProjectController(ProjectServiceImpl projectServiceImpl, ObjectMapper objectMapper) {
        this.projectServiceImpl = projectServiceImpl;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<String> createProject(@RequestBody ProjectDto projectDTO) {
        ProjectDto createdProject = projectServiceImpl.createProject(projectDTO);
        try {
            String json = objectMapper.writeValueAsString(createdProject);
            return new ResponseEntity<>(json, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<String> getAllProjects() {
        List<ProjectDto> projects = projectServiceImpl.getAllProjects();
        try {
            String json = objectMapper.writeValueAsString(projects);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProjectById(@PathVariable int id) {
        Optional<ProjectDto> project = projectServiceImpl.getProjectById(id);
        if (project.isPresent()) {
            try {
                String json = objectMapper.writeValueAsString(project.get());
                return new ResponseEntity<>(json, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                return new ResponseEntity<>("Error serializing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProject(@PathVariable int id, @RequestBody ProjectDto projectDTO) {
        if (projectServiceImpl.getProjectById(id).isPresent()) {
            projectDTO.setId(id);
            Optional<ProjectDto> updatedProject = projectServiceImpl.updateProject(projectDTO);
            if (updatedProject.isPresent()) {
                try {
                    String json = objectMapper.writeValueAsString(updatedProject.get());
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
    public ResponseEntity<Void> deleteProject(@PathVariable int id) {
        if (projectServiceImpl.deleteProject(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

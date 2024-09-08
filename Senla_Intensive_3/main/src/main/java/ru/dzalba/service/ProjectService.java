package ru.dzalba.service;

import org.springframework.stereotype.Service;
import ru.dzalba.dao.ProjectDao;
import ru.dzalba.dto.ProjectDto;
import ru.dzalba.models.Project;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectDao projectDAO;

    public ProjectService(ProjectDao projectDAO) {
        this.projectDAO = projectDAO;
    }

    public ProjectDto createProject(ProjectDto projectDTO) {
        Project project = new Project(projectDTO.getId(), projectDTO.getName(), projectDTO.getDescription(),
                projectDTO.getStartDate(), projectDTO.getEndDate());
        projectDAO.addProject(project);
        return projectDTO;
    }

    public List<ProjectDto> getAllProjects() {
        return projectDAO.getAllProjects().stream()
                .map(project -> new ProjectDto(project.getId(), project.getName(), project.getDescription(),
                        project.getStartDate(), project.getEndDate()))
                .collect(Collectors.toList());
    }

    public Optional<ProjectDto> getProjectById(int id) {
        return projectDAO.getProjectById(id)
                .map(project -> new ProjectDto(project.getId(), project.getName(), project.getDescription(),
                        project.getStartDate(), project.getEndDate()));
    }

    public Optional<ProjectDto> updateProject(ProjectDto projectDTO) {
        Project project = new Project(projectDTO.getId(), projectDTO.getName(), projectDTO.getDescription(),
                projectDTO.getStartDate(), projectDTO.getEndDate());
        projectDAO.updateProject(project);
        return Optional.of(projectDTO);
    }

    public boolean deleteProject(int id) {
        Optional<Project> project = projectDAO.getProjectById(id);
        if (project.isPresent()) {
            projectDAO.deleteProject(id);
            return true;
        }
        return false;
    }
}
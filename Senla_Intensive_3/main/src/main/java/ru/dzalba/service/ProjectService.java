package ru.dzalba.service;

import ru.dzalba.dao.ProjectDAO;
import ru.dzalba.dto.ProjectDTO;
import ru.dzalba.models.Project;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectService {

    private final ProjectDAO projectDAO;

    public ProjectService(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = new Project(projectDTO.getId(), projectDTO.getName(), projectDTO.getDescription(),
                projectDTO.getStartDate(), projectDTO.getEndDate());
        projectDAO.addProject(project);
        return projectDTO;
    }

    public List<ProjectDTO> getAllProjects() {
        return projectDAO.getAllProjects().stream()
                .map(project -> new ProjectDTO(project.getId(), project.getName(), project.getDescription(),
                        project.getStartDate(), project.getEndDate()))
                .collect(Collectors.toList());
    }

    public Optional<ProjectDTO> getProjectById(int id) {
        return projectDAO.getProjectById(id)
                .map(project -> new ProjectDTO(project.getId(), project.getName(), project.getDescription(),
                        project.getStartDate(), project.getEndDate()));
    }

    public Optional<ProjectDTO> updateProject(ProjectDTO projectDTO) {
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

package ru.dzalba.dao;

import org.springframework.stereotype.Component;
import ru.dzalba.models.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProjectDAO {
    private final List<Project> projects = new ArrayList<>();

    public void addProject(Project project) {
        projects.add(project);
    }

    public List<Project> getAllProjects() {
        return new ArrayList<>(projects);
    }

    public Optional<Project> getProjectById(int id) {
        return projects.stream()
                .filter(project -> project.getId() == id)
                .findFirst();
    }

    public void updateProject(Project project) {
        getProjectById(project.getId()).ifPresent(existingProject -> {
            existingProject.setName(project.getName());
            existingProject.setDescription(project.getDescription());
            existingProject.setStartDate(project.getStartDate());
            existingProject.setEndDate(project.getEndDate());
        });
    }

    public void deleteProject(int id) {
        projects.removeIf(project -> project.getId() == id);
    }
}

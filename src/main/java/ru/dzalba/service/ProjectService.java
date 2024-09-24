package ru.dzalba.service;

import ru.dzalba.dto.ProjectDto;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    ProjectDto createProject(ProjectDto dto);

    List<ProjectDto> getAllProjects();

    Optional<ProjectDto> getProjectById(int id);

    Optional<ProjectDto> updateProject(ProjectDto projectDTO);

    List<ProjectDto> findProjectsByName(String name);

    boolean deleteProject(int id);
}

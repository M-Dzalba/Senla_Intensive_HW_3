package ru.dzalba.service;

import ru.dzalba.dto.ProjectParticipationDto;

import java.util.List;
import java.util.Optional;

public interface ProjectParticipationService {

    ProjectParticipationDto createProjectParticipation(ProjectParticipationDto dto);

    List<ProjectParticipationDto> getAllProjectParticipations();

    Optional<ProjectParticipationDto> getProjectParticipationById(int id);

    boolean updateProjectParticipation(ProjectParticipationDto dto);

    boolean deleteProjectParticipation(int id);
}

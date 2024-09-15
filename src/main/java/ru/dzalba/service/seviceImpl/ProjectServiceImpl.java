package ru.dzalba.service.seviceImpl;

import org.springframework.stereotype.Service;
import ru.dzalba.dto.ProjectDto;
import ru.dzalba.dto.ProjectParticipationDto;
import ru.dzalba.models.Employee;
import ru.dzalba.models.Project;
import ru.dzalba.models.ProjectParticipation;
import ru.dzalba.repository.ProjectRepository;
import ru.dzalba.service.ProjectService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectDto createProject(ProjectDto dto) {
        validateProjectDto(dto);

        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());

        Set<ProjectParticipation> participations = dto.getProjectParticipations().stream()
                .map(participationDto -> new ProjectParticipation(
                        new Employee(participationDto.getEmployeeId()),
                        project,
                        participationDto.getRole(),
                        participationDto.getStartDate(),
                        participationDto.getEndDate()
                ))
                .collect(Collectors.toSet());
        project.setProjectParticipations(participations);

        projectRepository.save(project);

        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                project.getProjectParticipations().stream()
                        .map(participation -> new ProjectParticipationDto(
                                participation.getEmployee().getId(),
                                participation.getProject().getId(),
                                participation.getRole(),
                                participation.getStartDate(),
                                participation.getEndDate()
                        ))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(p -> new ProjectDto(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getProjectParticipations().stream()
                                .map(participation -> new ProjectParticipationDto(
                                        participation.getEmployee().getId(),
                                        participation.getProject().getId(),
                                        participation.getRole(),
                                        participation.getStartDate(),
                                        participation.getEndDate()
                                ))
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectDto> getProjectById(int id) {
        Project project = projectRepository.findById(id).orElseThrow();
        return Optional.of(new ProjectDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                project.getProjectParticipations().stream()
                        .map(participation -> new ProjectParticipationDto(
                                participation.getEmployee().getId(),
                                participation.getProject().getId(),
                                participation.getRole(),
                                participation.getStartDate(),
                                participation.getEndDate()
                        ))
                        .collect(Collectors.toSet())
        ));
    }

    @Override
    public Optional<ProjectDto> updateProject(ProjectDto projectDTO) {
        Optional<Project> existingProject = projectRepository.findById(projectDTO.getId());

        if (existingProject.isPresent()) {
            Project project = existingProject.get();
            project.setName(projectDTO.getName());
            project.setDescription(projectDTO.getDescription());
            project.setStartDate(projectDTO.getStartDate());
            project.setEndDate(projectDTO.getEndDate());


            Set<ProjectParticipation> participations = projectDTO.getProjectParticipations().stream()
                    .map(participationDto -> new ProjectParticipation(
                            new Employee(participationDto.getEmployeeId()),
                            project,
                            participationDto.getRole(),
                            participationDto.getStartDate(),
                            participationDto.getEndDate()
                    ))
                    .collect(Collectors.toSet());
            project.setProjectParticipations(participations);

            projectRepository.save(project);

            return Optional.of(new ProjectDto(
                    project.getId(),
                    project.getName(),
                    project.getDescription(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getProjectParticipations().stream()
                            .map(participation -> new ProjectParticipationDto(
                                    participation.getEmployee().getId(),
                                    participation.getProject().getId(),
                                    participation.getRole(),
                                    participation.getStartDate(),
                                    participation.getEndDate()
                            ))
                            .collect(Collectors.toSet())
            ));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<ProjectDto> findProjectsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty");
        }

        return projectRepository.findByName(name).stream()
                .map(p -> new ProjectDto(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getProjectParticipations().stream()
                                .map(participation -> new ProjectParticipationDto(
                                        participation.getEmployee().getId(),
                                        participation.getProject().getId(),
                                        participation.getRole(),
                                        participation.getStartDate(),
                                        participation.getEndDate()
                                ))
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteProject(int id) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isPresent()) {
            projectRepository.delete(project.get());
            return true;
        } else {
            return false;
        }
    }

    private void validateProjectDto(ProjectDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ProjectDto must not be null");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name must not be null or empty");
        }
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Project description must not be null or empty");
        }
        if (dto.getStartDate() == null) {
            throw new IllegalArgumentException("Start date must not be null");
        }
        if (dto.getEndDate() != null && dto.getEndDate().before(dto.getStartDate())) {
            throw new IllegalArgumentException("End date must not be before start date");
        }
    }
}
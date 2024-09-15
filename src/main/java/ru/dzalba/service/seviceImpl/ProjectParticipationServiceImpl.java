package ru.dzalba.service.seviceImpl;

import org.springframework.stereotype.Service;
import ru.dzalba.dto.ProjectParticipationDto;
import ru.dzalba.models.Employee;
import ru.dzalba.models.Project;
import ru.dzalba.models.ProjectParticipation;
import ru.dzalba.repository.EmployeeRepository;
import ru.dzalba.repository.ProjectParticipationRepository;
import ru.dzalba.repository.ProjectRepository;
import ru.dzalba.service.ProjectParticipationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProjectParticipationServiceImpl implements ProjectParticipationService {

    private final ProjectParticipationRepository projectParticipationRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    public ProjectParticipationServiceImpl(
            ProjectParticipationRepository projectParticipationRepository,
            ProjectRepository projectRepository,
            EmployeeRepository employeeRepository
    ) {
        this.projectParticipationRepository = projectParticipationRepository;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    @Override
    public ProjectParticipationDto createProjectParticipation(ProjectParticipationDto dto) {
        validateProjectParticipationDto(dto);

        Project project = projectRepository.findById(dto.getProjectId());
        if (project == null) {
            throw new IllegalArgumentException("Project with ID " + dto.getProjectId() + " not found.");
        }

        Employee employee = employeeRepository.findById(dto.getEmployeeId());
        if (employee == null) {
            throw new IllegalArgumentException("Employee with ID " + dto.getEmployeeId() + " not found.");
        }

        ProjectParticipation projectParticipation = new ProjectParticipation();
        projectParticipation.setRole(dto.getRole());
        projectParticipation.setProject(project);
        projectParticipation.setEmployee(employee);
        projectParticipation.setStartDate(dto.getStartDate());
        projectParticipation.setEndDate(dto.getEndDate());

        projectParticipationRepository.save(projectParticipation);

        return new ProjectParticipationDto(
                projectParticipation.getEmployee().getId(),
                projectParticipation.getProject().getId(),
                projectParticipation.getRole(),
                projectParticipation.getStartDate(),
                projectParticipation.getEndDate()
        );
    }

    @Override
    public List<ProjectParticipationDto> getAllProjectParticipations() {
        return projectParticipationRepository.findAll().stream()
                .map(pp -> new ProjectParticipationDto(
                        pp.getEmployee().getId(),
                        pp.getProject().getId(),
                        pp.getRole(),
                        pp.getStartDate(),
                        pp.getEndDate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectParticipationDto> getProjectParticipationById(int id) {
        ProjectParticipation pp = projectParticipationRepository.findById(id);
        if (pp != null) {
            return Optional.of(new ProjectParticipationDto(
                    pp.getEmployee().getId(),
                    pp.getProject().getId(),
                    pp.getRole(),
                    pp.getStartDate(),
                    pp.getEndDate()
            ));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public boolean updateProjectParticipation(ProjectParticipationDto dto) {
        ProjectParticipation pp = projectParticipationRepository.findById(dto.getEmployeeId());
        if (pp == null) {
            return false;
        }

        Project project = projectRepository.findById(dto.getProjectId());
        if (project == null) {
            throw new IllegalArgumentException("Project with ID " + dto.getProjectId() + " not found.");
        }

        Employee employee = employeeRepository.findById(dto.getEmployeeId());
        if (employee == null) {
            throw new IllegalArgumentException("Employee with ID " + dto.getEmployeeId() + " not found.");
        }

        pp.setRole(dto.getRole());
        pp.setProject(project);
        pp.setEmployee(employee);
        pp.setStartDate(dto.getStartDate());
        pp.setEndDate(dto.getEndDate());

        projectParticipationRepository.update(pp);

        return true;
    }

    @Transactional
    @Override
    public boolean deleteProjectParticipation(int id) {
        ProjectParticipation pp = projectParticipationRepository.findById(id);
        if (pp != null) {
            projectParticipationRepository.delete(pp);
            return true;
        }
        return false;
    }

    private void validateProjectParticipationDto(ProjectParticipationDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ProjectParticipationDto must not be null");
        }
        if (dto.getRole() == null || dto.getRole().isEmpty()) {
            throw new IllegalArgumentException("Role must not be null or empty");
        }
        if (dto.getProjectId() <= 0) {
            throw new IllegalArgumentException("Invalid project ID");
        }
        if (dto.getEmployeeId() <= 0) {
            throw new IllegalArgumentException("Invalid employee ID");
        }
        if (dto.getStartDate() == null) {
            throw new IllegalArgumentException("Start date must not be null");
        }
    }
}

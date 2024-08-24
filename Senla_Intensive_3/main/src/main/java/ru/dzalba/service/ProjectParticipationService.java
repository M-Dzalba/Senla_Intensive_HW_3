package ru.dzalba.service;

import org.springframework.stereotype.Service;
import ru.dzalba.dao.ProjectParticipationDAO;
import ru.dzalba.dto.ProjectParticipationDTO;
import ru.dzalba.models.ProjectParticipation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectParticipationService {
    private final ProjectParticipationDAO participationDAO;

    public ProjectParticipationService(ProjectParticipationDAO participationDAO) {
        this.participationDAO = participationDAO;
    }

    public ProjectParticipationDTO addParticipation(ProjectParticipationDTO participationDTO) {
        ProjectParticipation participation = new ProjectParticipation(participationDTO.getEmployeeId(), participationDTO.getProjectId(),
                participationDTO.getRole(), participationDTO.getStartDate(), participationDTO.getEndDate());
        participationDAO.addParticipation(participation);
        return participationDTO;
    }

    public List<ProjectParticipationDTO> getAllParticipations() {
        return participationDAO.getAllParticipations().stream()
                .map(p -> new ProjectParticipationDTO(p.getEmployeeId(), p.getProjectId(), p.getRole(),
                        p.getStartDate(), p.getEndDate()))
                .collect(Collectors.toList());
    }

    public Optional<ProjectParticipationDTO> getParticipation(int employeeId, int projectId) {
        return participationDAO.getParticipation(employeeId, projectId)
                .map(p -> new ProjectParticipationDTO(p.getEmployeeId(), p.getProjectId(), p.getRole(),
                        p.getStartDate(), p.getEndDate()));
    }

    public Optional<ProjectParticipationDTO> updateParticipation(ProjectParticipationDTO participationDTO) {
        ProjectParticipation participation = new ProjectParticipation(participationDTO.getEmployeeId(), participationDTO.getProjectId(),
                participationDTO.getRole(), participationDTO.getStartDate(), participationDTO.getEndDate());
        participationDAO.updateParticipation(participation);
        return Optional.of(participationDTO);
    }

    public boolean deleteParticipation(int employeeId, int projectId) {
        Optional<ProjectParticipation> participation = participationDAO.getParticipation(employeeId, projectId);
        if (participation.isPresent()) {
            participationDAO.deleteParticipation(employeeId, projectId);
            return true;
        }
        return false;
    }
}

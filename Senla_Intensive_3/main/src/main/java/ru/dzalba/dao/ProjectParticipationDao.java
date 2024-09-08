package ru.dzalba.dao;

import org.springframework.stereotype.Component;
import ru.dzalba.models.ProjectParticipation;

import java.util.*;

@Component
public class ProjectParticipationDao {
    private final List<ProjectParticipation> participations = new ArrayList<>();

    public void addParticipation(ProjectParticipation participation) {
        participations.add(participation);
    }

    public List<ProjectParticipation> getAllParticipations() {
        return new ArrayList<>(participations);
    }

    public Optional<ProjectParticipation> getParticipation(int employeeId, int projectId) {
        return Optional.ofNullable(participations)
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(participation -> Objects.equals(participation.getEmployeeId(),employeeId)
                        && Objects.equals(participation.getProjectId(),projectId))
                .findFirst();
    }

    public void updateParticipation(ProjectParticipation participation) {
        getParticipation(participation.getEmployeeId(), participation.getProjectId()).ifPresent(existingParticipation -> {
            existingParticipation.setRole(participation.getRole());
            existingParticipation.setStartDate(participation.getStartDate());
            existingParticipation.setEndDate(participation.getEndDate());
        });
    }

    public void deleteParticipation(int employeeId, int projectId) {
        participations.removeIf(p -> p.getEmployeeId() == employeeId && p.getProjectId() == projectId);
    }
}

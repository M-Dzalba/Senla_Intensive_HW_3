package ru.dzalba.repository;

import org.springframework.stereotype.Repository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ru.dzalba.models.Employee;
import ru.dzalba.models.Project;
import ru.dzalba.models.ProjectParticipation;

import java.util.List;

@Repository
public class ProjectParticipationRepository extends AbstractRepository<ProjectParticipation> {

    @PersistenceContext
    private EntityManager entityManager;

    public ProjectParticipationRepository() {
        super(ProjectParticipation.class);
    }

    public List<ProjectParticipation> findByRole(String role) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProjectParticipation> criteriaQuery = criteriaBuilder.createQuery(ProjectParticipation.class);
        Root<ProjectParticipation> projectParticipationRoot = criteriaQuery.from(ProjectParticipation.class);

        Predicate rolePredicate = criteriaBuilder.equal(projectParticipationRoot.get("role"), role);
        criteriaQuery.where(rolePredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<ProjectParticipation> findByProject(Project project) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProjectParticipation> criteriaQuery = criteriaBuilder.createQuery(ProjectParticipation.class);
        Root<ProjectParticipation> projectParticipationRoot = criteriaQuery.from(ProjectParticipation.class);

        Predicate projectPredicate = criteriaBuilder.equal(projectParticipationRoot.get("project"), project);
        criteriaQuery.where(projectPredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<ProjectParticipation> findByEmployee(Employee employee) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProjectParticipation> criteriaQuery = criteriaBuilder.createQuery(ProjectParticipation.class);
        Root<ProjectParticipation> projectParticipationRoot = criteriaQuery.from(ProjectParticipation.class);

        Predicate employeePredicate = criteriaBuilder.equal(projectParticipationRoot.get("employee"), employee);
        criteriaQuery.where(employeePredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}

package ru.dzalba.repository;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ru.dzalba.models.Project;

@Repository
public class ProjectRepository extends AbstractRepository<Project> {

    @PersistenceContext
    private EntityManager entityManager;

    public ProjectRepository() {
        super(Project.class);
    }

    public List<Project> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> projectRoot = criteriaQuery.from(Project.class);

        Predicate namePredicate = criteriaBuilder.like(projectRoot.get("name"), "%" + name + "%");
        criteriaQuery.where(namePredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
